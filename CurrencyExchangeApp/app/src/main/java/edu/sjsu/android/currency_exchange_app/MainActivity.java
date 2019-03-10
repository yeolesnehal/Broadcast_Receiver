package edu.sjsu.android.currency_exchange_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    double gbp;
    double eur;
    double inr;
    double currency;

    String currencyConvertTo;
    double convertedCurrency;
    MyBroadcastReceiver br = new MyBroadcastReceiver();
    BroadcastReceiver br2;
    private Button receive_btn;

    public void enableStrictMode()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter("android.intent.action.MAIN");
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        this.registerReceiver(br, filter);

        br2 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                if (b != null){
                    currency = Double.parseDouble(b.getString("Dollar Amount"));
                    currencyConvertTo = intent.getStringExtra("Convert To");

                    //Assign the extracted values from broadcast receiver to TextView on screen of MAin Activity
                    TextView dollarAmount = (TextView)findViewById(R.id.dollarAmount);
                    dollarAmount.setText("Dollar Amount: $"+currency);
                    TextView convertTo = (TextView)findViewById(R.id.convertTo);
                    convertTo.setText("Convert to: "+currencyConvertTo);

                }

            }
        };
        this.registerReceiver(br2, new IntentFilter("MyBroadcastReceiver"));

        receive_btn = (Button)findViewById(R.id.apply);
        receive_btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                String api_response = GetAPIResponse();
                Log.v("*********************",api_response);
                String convertedCurrency = String.valueOf(currencyConversion(currency, currencyConvertTo, api_response));
                Log.v("************** rate",convertedCurrency);

                Intent intent = new Intent();
                intent.setAction("edu.sjsu.android.currency_exchange_app");
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                intent.putExtra("Converted Currency", convertedCurrency);
                Log.i("Intent is sent back", String.valueOf(intent.getExtras()));
                sendBroadcast(intent);

            }
        });


    }



    public double currencyConversion(double amount, String convertTo, String apiResponse){

        JSONParser myparser = new JSONParser();
        Object s = null;
        Log.v("Entering  method",apiResponse );
        try
        {
            JSONObject myobj = (JSONObject)myparser.parse(apiResponse);
            JSONObject myRateObj = (JSONObject)myobj.get("rates");
            s = myRateObj.get("GBP");
            String gbpCurrency = s.toString();
            Log.v("converted",gbpCurrency);
            gbp = Double.parseDouble(gbpCurrency);
            s = myRateObj.get("EUR");
            String eurCurrency = s.toString();
            eur = Double.parseDouble(eurCurrency);
            s = myRateObj.get("INR");
            String inrCurrency = s.toString();
            inr = Double.parseDouble(inrCurrency);

        }
        catch (org.json.simple.parser.ParseException e)
        {
            e.printStackTrace();
        }

        convertedCurrency = 0.0;
        switch(convertTo){
            case "British Pound":
                convertedCurrency = amount * gbp;
                break;
            case "Indian Rupee":
                convertedCurrency = amount * inr;
                break;
            case "Euro":
                convertedCurrency = amount * eur;
                break;

        }

        return convertedCurrency;
    }

    public String GetAPIResponse()
    {
        enableStrictMode();

        URL urlGetRequest = null;
        String apiResponse = null;
        try {
            urlGetRequest = new URL("https://api.openrates.io/latest?base=USD");
            String readLine = null;
            HttpURLConnection connection = (HttpURLConnection) urlGetRequest.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                //Log.v("***func", "Message okay");
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null)
                {
                    response.append(readLine);
                }
                in.close();
                apiResponse = response.toString();
                Log.v("Api Response**********",apiResponse);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiResponse;

    }


    public void finishMainActivity(View v)
    {
        MainActivity.this.finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(this.br);
        this.unregisterReceiver(this.br2);

    }

}

