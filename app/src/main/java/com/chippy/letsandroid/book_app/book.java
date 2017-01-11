package com.chippy.letsandroid.book_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class book extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    int c =0;
    TextView tv;
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url;
    ArrayList<HashMap<String, String>> bookList;
    private  String base ="https://www.googleapis.com/books/v1/volumes?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        tv = (TextView) findViewById(android.R.id.empty);
        Bundle extra = getIntent().getExtras();
        String data = extra.getString("key");
        url =base+data;
        boolean check;
        check = isNetworkAvailable();
        if(check==true) {
            bookList = new ArrayList<>();
            lv = (ListView) findViewById(R.id.list);
            new GetBook().execute();
        }    else{
            Toast.makeText(getApplicationContext(),
                    " check internet connectivity",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }
    /**
     * Async task class to get json by making HTTP call
     */
    private class GetBook extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(book.this);
            pDialog.setMessage("Please wait...While searching");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject baseJsonResponse = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray featureArray = baseJsonResponse.getJSONArray("items");
                    // looping through All Contacts
                    for (int i = 0; i < featureArray.length(); i++) {
                        JSONObject firstFeature = featureArray.getJSONObject(i);
                        JSONObject properties = firstFeature.getJSONObject("volumeInfo");
                        String  title = properties.getString("title");
                        String  pub =properties.getString("publisher");
                        //get data of jsonarray of text
                        JSONArray value =properties.getJSONArray("authors");
                        String author =(String)value.get(0);
                           HashMap<String, String> book = new HashMap<>();
                            book.put("name", "Tilte : " + title);
                            book.put("pub", "Publisher : " + pub);
                            book.put("aut", "Author : " + author);
                            // adding contact to book list
                        c++;
                            bookList.add(book);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                     Log.e(TAG, "Json parsing error: " + e.getMessage());
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                     test();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing()){
                pDialog.dismiss();}
            /**
             * Updating parsed JSON data into ListView
             * */
            if(c==0){
                test();
            }
            ListAdapter adapter = new SimpleAdapter(
                    book.this, bookList,
                    R.layout.list_item, new String[]{"name", "pub",
                    "aut"}, new int[]{R.id.title,
                    R.id.pub, R.id.author});
                    lv.setAdapter(adapter);
        }
    }
        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    public void test(){
        lv.setEmptyView(findViewById(R.id.empty));
    }
}