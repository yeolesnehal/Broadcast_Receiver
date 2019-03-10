package edu.sjsu.android.currency_exchange_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        String dollar_amount = intent.getStringExtra("Dollar Amount");
        String convert = intent.getStringExtra("Convert to");

        String log = "Action: " + intent.getAction() + "\n" +
                "Dollar Amount: " + dollar_amount + "\n" +
                "Convert To: " + convert + "\n";

        Log.d( "MyBroadcastReceiver",log);

        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

        //Send the received data from broadcast receiver to Main Activity
        Intent i = new Intent("MyBroadcastReceiver");
        i.putExtra("Dollar Amount",dollar_amount);
        i.putExtra("Convert To",convert);
        context.sendBroadcast(i);

    }

}
