package edu.sjsu.android.currency_converter_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class FirstReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String convertedCurrency = intent.getStringExtra("Converted Currency");

        String log = "Action: " + intent.getAction() + "\n" +
                "Converted Currency: " + convertedCurrency + "\n";
        Log.d( "FirstReceiver",log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();


    //Send the received data from broadcast receiver to the main activity
    Intent i = new Intent("FirstReceiver");
    i.putExtra("Converted Currency", convertedCurrency);
    context.sendBroadcast(i);

    }
}
