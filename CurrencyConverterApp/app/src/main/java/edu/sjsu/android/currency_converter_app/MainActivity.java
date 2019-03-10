package edu.sjsu.android.currency_converter_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText enterDollarAmount;
    Spinner spinner;
    BroadcastReceiver br;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                if(b != null){
                    TextView convertedCurrency = (TextView)findViewById(R.id.convertedCurrency);
                    String output = "Dollar amount $" + enterDollarAmount.getText().toString() + " converted to " + b.getString("Converted Currency") + " " + spinner.getSelectedItem().toString();
                    convertedCurrency.setText(output);
                   Log.i("output: ", output);
                }
            }
        };


        Button convert = (Button) findViewById(R.id.convert);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDollarAmount = (EditText)findViewById(R.id.enterDollarAmount);
                spinner = (Spinner)findViewById(R.id.spinner);
                Intent intent = new Intent();
                intent.setAction("edu.sjsu.android.currency_converter_app");
                intent.putExtra("Dollar Amount", enterDollarAmount.getText().toString());
                intent.putExtra("Convert to", spinner.getSelectedItem().toString());
                intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                sendBroadcast(intent);
            }
        });

        this.registerReceiver(br, new IntentFilter("FirstReceiver"));
    }

    public void finishMainActivity(View v)
    {
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.unregisterReceiver(this.br);
    }
}
