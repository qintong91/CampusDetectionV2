package com.example.campusdetectionv2;

import java.io.File;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.campusdetectionv2.R.drawable;
import com.example.campusdetectionv2.support.BitmapMaker;
import com.example.campusdetectionv2.support.BrowsingData;
import com.example.campusdetectionv2.support.NetUtil;
import com.example.campusdetectionv2.support.ReportData;
import com.example.campusdetectionv2.view.RoundProgressBar;
import com.example.campusdetectionv2.view.RoundedImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class BrowsingInfoActivity extends Activity{
	MapView mMapView = null;
	BaiduMap mBaiduMap=null;
	public RoundProgressBar prograssBar = null;
	public RoundedImageView imageView = null;
	public TextView userView = null;
	public TextView typeView = null;
	public TextView saveTimeView = null;
	public TextView submitTimeView = null;
	public TextView targetLatlngView = null;
	public TextView userLatlngView = null;
	public TextView remarkView = null;
	public TextView authorityView = null;
	public TableRow submitTimeRow = null;
	public TableRow authorityRow = null;
	public BrowsingData data = null;
	public Bitmap bmp = null;
	public  MyApp myApp;
	public GetImageTask getImageTask = null;
	public String username;
	private ProgressDialog pDialog;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_browsinginfo);
		imageView=(RoundedImageView)findViewById(R.id.dImageView);
		prograssBar = (RoundProgressBar)findViewById(R.id.roundProgressBar);
		userView = (TextView)findViewById(R.id.dUser);
		typeView=(TextView)findViewById(R.id.dType);
		saveTimeView=(TextView)findViewById(R.id.dSaveTime);
		submitTimeView=(TextView)findViewById(R.id.dSubmitTime);
		targetLatlngView=(TextView)findViewById(R.id.dTargetLatlng);
		userLatlngView=(TextView)findViewById(R.id.dUserLatlng);
		remarkView=(TextView)findViewById(R.id.dRemark);
		authorityView=(TextView)findViewById(R.id.dAuthenticity);
		submitTimeRow=(TableRow)findViewById(R.id.dSubmitTimeRow);
		authorityRow=(TableRow)findViewById(R.id.dAuthenticityRow);
		mMapView = (MapView)findViewById(R.id.bmapView); 
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();  
        mBaiduMap.setMyLocationEnabled(false);
		data = (BrowsingData)getIntent().getSerializableExtra(BrowsingFragment.SER_KEY);
		setView();
		imageView.setOnClickListener(new DetialImageClickListener());
		myApp=(MyApp)getApplication();
		username = myApp.getUsername();
		getImageTask = new GetImageTask();
		getImageTask.execute();
	}
	class DetialImageClickListener implements View.OnClickListener
	{
		  @Override
		   public void onClick(View v) {
				 Intent intent = new Intent(BrowsingInfoActivity.this, ImageActivity.class);  
             	 Bundle mBundle = new Bundle();  
               //   mBundle.putString("PICPATH", data.getPicPath());  
                  intent.putExtras(mBundle);
             	startActivity(intent);
			 }
			     				   		 		     
	}
	    public void setView()
	    {
			//imageView.setImageBitmap(BitmapMaker.GetLargeBitmap(data.getPicPath()));
	    	userView.setText(data.getUsername());
			typeView.setText(data.getTypeString());
			remarkView.setText(data.getRemark());
			saveTimeView.setText(data.getTime());
			submitTimeView.setText(data.getSubmitTime());
			targetLatlngView.setText(String.format("%.6f", data.getLat()) + "," +String.format("%.6f", data.getLng()));
			userLatlngView.setText(String.format("%.6f", data.getUserLat()) + "," +String.format("%.6f", data.getUserLng()));
			if(data.isFlag())
				authorityView.setText("真");
			else
				authorityView.setText("伪");
			LatLng   pt1 = new LatLng(data.getLat(),data.getLng());
			OverlayOptions ooA; 
			BitmapDescriptor target_icon;
			target_icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			 ooA = new MarkerOptions().position(pt1).icon(target_icon)
	      			.zIndex(9);
			 mBaiduMap.addOverlay(ooA);	
			 LatLng   pt2 = new LatLng(data.getUserLat(),data.getUserLng());
				OverlayOptions ooA2; 
				BitmapDescriptor target_icon2;
				target_icon2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_userpos);
				 ooA2 = new MarkerOptions().position(pt2).icon(target_icon2)
		      			.zIndex(9);
				 MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(pt2, 18);
					mBaiduMap.animateMapStatus(u);
				 mBaiduMap.addOverlay(ooA2);

	    }
	    public class GetImageTask extends AsyncTask<String, Integer, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			@Override                     			 
			protected void onProgressUpdate(Integer... values)///执行操作中，发布进度后		 
			{		 
				prograssBar.setProgress(values[0]);//每次更新进度条		 
			}

			/**
			 * Creating product
			 * */
			protected String doInBackground(String... args) {
				  File pic = NetUtil.downloadPic(data.getPicpath(),GetImageTask.this);
				    bmp = BitmapMaker.GetLargeBitmap(pic.getAbsolutePath());				   
				Message msg = msgHandler.obtainMessage();
				if(bmp==null)
				{
					msg.what = 0x34;
				}
 
				return null;				
			}
			public void doProgress(int value){
		        publishProgress(value);
		    }
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				if(bmp != null){
				 prograssBar.setVisibility(View.GONE);
				 imageView.setImageBitmap(bmp);
				}				 				
			}
	 }
	    private final Handler msgHandler = new Handler()
		{
	        @SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) 
	        {
	                switch (msg.what) {
	                case 0x34:
	                	Toast.makeText(BrowsingInfoActivity.this, "无法获取图片", Toast.LENGTH_SHORT).show();
	                        break;
	                
	                }
	        }
	    };

}
