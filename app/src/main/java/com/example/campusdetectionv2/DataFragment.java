package com.example.campusdetectionv2;


import java.util.ArrayList;

import com.baidu.mapapi.SDKInitializer;
import com.example.campusdetectionv2.support.BitmapMaker;
import com.example.campusdetectionv2.support.ReportData;

import android.R.color;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DataFragment extends ListFragment{
    ArrayList<ReportData> dataList;
    public static String SER_KEY="parkey";
    public  MyApp myApp;
    public String username ;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getActivity().getApplicationContext());


    }
    @Override
    public void onResume() {
        myApp=(MyApp)getActivity().getApplication();
        username = myApp.getUsername();
        dataList=ReportData.getReportData(getActivity(),username);
        DataBaseAdapter adapter = new DataBaseAdapter(getActivity());
        setListAdapter(adapter);
        super.onResume();
    }



    public class DataBaseAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        public DataBaseAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // return mData.size();
            return dataList.size();
        }
        @Override
        public Object getItem(int arg0) {
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        public final class ViewHolder{
            public ImageView dataPic;
            public TextView dataTime;
            public TextView dataType;
            public TextView dataSumit;
            public RelativeLayout dataLayout;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder=new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_data, null);
                holder.dataPic = (ImageView)convertView.findViewById(R.id.dataImg);
                holder.dataTime = (TextView)convertView.findViewById(R.id.dataTime);
                holder.dataType = (TextView)convertView.findViewById(R.id.dataType);
                holder.dataSumit = (TextView)convertView.findViewById(R.id.dataSubmit);
                holder.dataLayout = (RelativeLayout)convertView.findViewById(R.id.dataRelativeLayout);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            long time1 = System.currentTimeMillis();

            Bitmap bm=BitmapMaker.GetSmallBitmap(dataList.get(position).getPicPath());
            holder.dataPic.setImageBitmap(bm);
            Log.w("time", "aaaaaa"+Long.toString(System.currentTimeMillis()-time1));
            time1 = System.currentTimeMillis();
            holder.dataTime.setText(dataList.get(position).getSaveTime());
            holder.dataType.setText(dataList.get(position).getTypeString());
            if(dataList.get(position).isSubmit())
                holder.dataSumit.setText("Already Uploaded");
            else{
                holder.dataSumit.setText("Not Uploaded");
                holder.dataSumit.setTextColor(Color.parseColor("#33B5E5"));
            }
            convertView.setBackgroundColor(color.background_light);
            final int p = position;
            holder.dataLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DetialActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(SER_KEY , dataList.get(p));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            });
            Log.w("time", "bbbbbb"+Long.toString(System.currentTimeMillis()-time1));
            time1 = System.currentTimeMillis();
            return convertView;
        }
    }
}
