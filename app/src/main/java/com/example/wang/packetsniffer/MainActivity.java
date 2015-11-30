package com.example.wang.packetsniffer;

import static com.example.wang.packetsniffer.Constants.FIRST_COLUMN;
import static com.example.wang.packetsniffer.Constants.SECOND_COLUMN;
import static com.example.wang.packetsniffer.Constants.THIRD_COLUMN;
import static com.example.wang.packetsniffer.Constants.FOURTH_COLUMN;
import static com.example.wang.packetsniffer.Constants.FIFTH_COLUMN;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    public Process process;
    public int lenIndex;
    public ArrayList<String> malIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createMalIP();
        // Toast.makeText(getApplicationContext(), Integer.toString(malIP.size()), Toast.LENGTH_LONG).show();
        //((TextView)findViewById(R.id.librarySizeView)).setText("Malicious IP amount: " + Integer.toString(malIP.size()));
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



    public void startButtonOnClick(View view){
        ((TextView)findViewById(R.id.progressView)).setText("IN PROGRESS");
        try {
            //process = Runtime.getRuntime().exec(new String[]{"su", "-c", "chmod 777 /sdcard/programapp/tcpdump"});
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "tcpdump -v -n -i any -c 100 > /mnt/sdcard/programapp/output"});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopButtonOnClick(View view) {
        ((TextView) findViewById(R.id.progressView)).setText("COMPLETED");
        // process.destroy();
        try {
            process.waitFor();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        process.destroy();
        displayOutput();
    }

    public void displayOutput()
    {
        ListView listView=(ListView)findViewById(R.id.listView1);

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();

        StringBuilder text = new StringBuilder();
        try {

            FileInputStream fis = new FileInputStream("/mnt/sdcard/programapp/output");

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = br.readLine()) != null) {
                HashMap<String,String> temp = new HashMap<String, String>();
                String time = line.substring(0,8);
                String proto = findProto(line);
                String len = findLen(line);
                String source = findSource(line);
                String status = "Healthy";
                temp.put(FIRST_COLUMN, time);
                temp.put(SECOND_COLUMN, source);
                temp.put(THIRD_COLUMN, proto);
                temp.put(FOURTH_COLUMN, len);

//                if(malIP.contains(source)){
//                    status = "Malicious";
//                }

                temp.put(FIFTH_COLUMN, status);
                list.add(temp);

//                text.append(line);
//                text.append('\n');
            }

            ListViewAdapter adapter=new ListViewAdapter(this, list);
            listView.setAdapter(adapter);


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

    public String findProto (String s) {
        String proto = "";

        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == 'p') {
                if (s.charAt(i + 1) == 'r') {
                    if (s.charAt(i + 2) == 'o') {
                        if (s.charAt(i + 3) == 't') {
                            if (s.charAt(i + 4) == 'o') {
                                proto = s.substring(i + 6, i + 9);
                            }
                        }
                    }
                }
            }
        }

        return proto;
    }

    public String findLen (String s){
        String length = "0";
        int len = s.length();
        int start = 0;
        int end = 0;
        for(int i = 0; i < len; i++) {
            if(s.charAt(i) == 'l'){
                if(s.charAt(i+1) == 'e'){
                    if(s.charAt(i+2) == 'n'){
                        if(s.charAt(i+3) == 'g'){
                            if(s.charAt(i+4) == 't'){
                                if(s.charAt(i+5) == 'h'){
                                    start = i + 6;
                                    lenIndex = start;
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int i = start; i < len; i++) {
            if(s.charAt(i) == ')'){
                end = i;
                break;
            }
        }
        length = s.substring(start, end);

        return length;
    }

    public String findSource (String s){
        String source = "";
        int len = s.length();
        int start = 0;
        int end = 0;
        for(int i = lenIndex; i< len; i++){
            if(s.charAt(i) == ')'){
                start = i + 2;
                break;
            }
        }
        for(int i = lenIndex; i < len; i++) {
            if(s.charAt(i) == '>'){
                end = i;
                break;
            }
        }
        source = s.substring(start, end);
        return source;
    }

    public void createMalIP(){
        malIP = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File("/mnt/sdcard/programapp/iplist.txt"));
            while (s.hasNext()){
                malIP.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException e){
            Toast.makeText(getApplicationContext(), "File Malicious IP List Not Found!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

}
