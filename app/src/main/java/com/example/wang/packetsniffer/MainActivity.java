package com.example.wang.packetsniffer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Process process;

    public void startButtonOnClick(View view){
        ((TextView)findViewById(R.id.progressView)).setText("IN PROGRESS");
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 /sdcard/programapp/tcpdump"});
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "tcpdump -c 100 -l -i eth0 > /mnt/sdcard/programapp/output"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopButtonOnClick(View view) {
        ((TextView) findViewById(R.id.progressView)).setText("COMPLETED");
        process.destroy();
        displayOutput();
    }

    public void displayOutput()
    {
        StringBuilder text = new StringBuilder();
        try {

            FileInputStream fis = new FileInputStream("/mnt/sdcard/programapp/output");

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        }
        catch (FileNotFoundException e) {
            Toast.makeText(getApplicationContext(), "File not found!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        TextView output=(TextView) findViewById(R.id.trafficView);
        output.setMovementMethod(new ScrollingMovementMethod());
        output.setText(text);
    }
}
