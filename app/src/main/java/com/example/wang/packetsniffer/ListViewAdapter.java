package com.example.wang.packetsniffer;

/**
 * Created by Wang on 11/29/15.
 */
import static com.example.wang.packetsniffer.Constants.FIRST_COLUMN;
import static com.example.wang.packetsniffer.Constants.SECOND_COLUMN;
import static com.example.wang.packetsniffer.Constants.THIRD_COLUMN;
import static com.example.wang.packetsniffer.Constants.FOURTH_COLUMN;
import static com.example.wang.packetsniffer.Constants.FIFTH_COLUMN;


import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    TextView txtFifth;
    public ListViewAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater=activity.getLayoutInflater();

        if(convertView == null){

            convertView=inflater.inflate(R.layout.colmn_row, null);

            txtFirst=(TextView) convertView.findViewById(R.id.time);
            txtSecond=(TextView) convertView.findViewById(R.id.source);
            txtThird=(TextView) convertView.findViewById(R.id.protocal);
            txtFourth=(TextView) convertView.findViewById(R.id.size);
            txtFifth=(TextView) convertView.findViewById(R.id.status);

        }

        HashMap<String, String> map=list.get(position);
        txtFirst.setText(map.get(FIRST_COLUMN));
        txtSecond.setText(map.get(SECOND_COLUMN));
        txtThird.setText(map.get(THIRD_COLUMN));
        txtFourth.setText(map.get(FOURTH_COLUMN));
        txtFifth.setText(map.get(FIFTH_COLUMN));

        return convertView;
    }

}
