package com.meliusid.melius.consumobuses;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import logic.Bus;
import logic.HttpHandler;

public class registerConsumption extends AppCompatActivity {

    private String TAG = registerConsumption.class.getSimpleName();

    private ProgressDialog pDialog;
    private Spinner sp;

    // URL to get contacts JSON
    private static String url = "http://192.168.0.8:8080/api/v1/buses";

    ArrayList<Bus> busesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_consumption);

        busesList = new ArrayList<>();

        sp = (Spinner) findViewById(R.id.spinner);
        new GetBuses().execute();
    }

    /**
     *
     */

    public void sendInfo(){

        EditText kilometers = (EditText) findViewById(R.id.kilometers);
        EditText gallons = (EditText) findViewById(R.id.gallons);
        EditText date = (EditText) findViewById(R.id.date);
    }



    /**
     * Async task class to get json by making HTTP call
     */
    private class GetBuses extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(registerConsumption.this);
            pDialog.setMessage("Please wait...");
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
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray buses = jsonObj.getJSONArray("buses");

                    // looping through All Contacts
                    for (int i = 0; i < buses.length(); i++) {

                        JSONObject c = buses.getJSONObject(i);

                        String[] allCodes = c.getString("codes").split(",");

                        for (int j = 0; j < allCodes.length; j++) {
                            Bus bus = new Bus(allCodes[j].replace('"',' '));
                            busesList.add(bus);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            sp.setAdapter(new ArrayAdapter<String>(registerConsumption.this, android.R.layout.simple_spinner_item, Bus.buses_codes(busesList)));
        }

    }
}
