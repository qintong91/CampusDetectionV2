package com.example.campusdetectionv2;
 

import java.io.File;
import java.io.StringReader;
import java.util.Date;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.campusdetectionv2.R.drawable;
import com.example.campusdetectionv2.ReportFragment.SubmitClickListener;
 
import com.example.campusdetectionv2.support.BitmapMaker;
import com.example.campusdetectionv2.support.NetUtil;
import com.example.campusdetectionv2.support.ReportData;
import com.example.campusdetectionv2.support.XmlContentHandler;
import com.example.campusdetectionv2.view.RoundProgressBar;
import com.example.campusdetectionv2.view.RoundedImageView;

import android.R.color;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;



public class DetialActivity extends Activity{
	MapView mMapView = null;
	BaiduMap mBaiduMap=null;
	public Button submitButton = null;
	public RoundedImageView imageView = null;
	public TextView typeView = null;
	public TextView saveTimeView = null;
	public TextView submitTimeView = null;
	public TextView targetLatlngView = null;
	public TextView userLatlngView = null;
	public TextView remarkView = null;
	public TextView authorityView = null;
	public TableRow submitTimeRow = null;
	public TableRow authorityRow = null;
	public ReportData data = null;
	public Bitmap bmp = null;
	public  MyApp myApp;
	public String username;

	private ProgressDialog pDialog;



    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detial);
		submitButton=(Button)findViewById(R.id.dSubmit_button);
		imageView=(RoundedImageView)findViewById(R.id.dImageView);
		
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
		data = (ReportData)getIntent().getSerializableExtra(DataFragment.SER_KEY);
		setView();

		imageView.setOnClickListener(new DetialImageClickListener());
		submitButton.setOnClickListener(new DetialSubmitClickListener());
		myApp=(MyApp)getApplication();
		username = myApp.getUsername();


    }
	class DetialSubmitClickListener implements View.OnClickListener
	{
		  @Override
		   public void onClick(View v) {
			   
			  DetialSubmitTask asyncTask = new DetialSubmitTask();
				  asyncTask.execute();								 		 
			 }
			     				   		 		     
	}
	class DetialImageClickListener implements View.OnClickListener
	{
		  @Override
		   public void onClick(View v) {
				 Intent intent = new Intent(DetialActivity.this, ImageActivity.class);  
             	 Bundle mBundle = new Bundle();  
                  mBundle.putString("PICPATH", data.getPicPath());  
                  intent.putExtras(mBundle);
             	startActivity(intent);
			 }
			     				   		 		     
	}
	 private class DetialSubmitTask extends AsyncTask<String, Integer, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(DetialActivity.this);
				pDialog.setMessage("上传中..");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
			}
			@Override                     			 
			protected void onProgressUpdate(Integer... values)///执行操作中，发布进度后
		 
			{

		//	progressBar.setProgress(values[0]);//每次更新进度条
		 
			}

			/**
			 * Creating product
			 * */
			protected String doInBackground(String... args) {
				int submitState=data.submit(username);
				boolean save = false;
				if(submitState !=ReportData.UN_SUBMIT ){
					  save=data.saveSubmitInfo(DetialActivity.this, username);
				}									
				Message msg = msgHandler.obtainMessage();
				if(submitState==ReportData.UN_SUBMIT&&save)
				{
					msg.what = 0x44;
				}
				else if(submitState==ReportData.SUBMIT_REAL&&save)
				{
					msg.what = 0x45;
				}
				else if(submitState==ReportData.SUBMIT_FAKE&&save)
				{
					msg.what = 0x46;
				}
				else if(submitState==ReportData.SUBMIT_REAL&&(!save))
				{
					msg.what = 0x48;
				}
				else if(submitState==ReportData.SUBMIT_FAKE&&(!save))
				{
					msg.what = 0x49;
				}
				msgHandler.sendMessage(msg);
				
				
				return null;
				
			}
			
			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
				setView();
			}

		}
	 private final Handler msgHandler = new Handler()
		{
	        @SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) 
	        {
	                switch (msg.what) {
	                case 0x44:
	                	Toast.makeText(DetialActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x45:
	                	Toast.makeText(DetialActivity.this, "上传成功|图片为真", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x46:
	                	Toast.makeText(DetialActivity.this, "上传成功|图片为假", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x48:
	                	Toast.makeText(DetialActivity.this, "上传成功|图片为真|保存失败", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x49:
	                	Toast.makeText(DetialActivity.this, "上传成功|图片为假|保存失败", Toast.LENGTH_SHORT).show();
	                        break;
	                default:
	                        break;
	               
	                }
	        }
	    };
	    public void setView()
	    {
			imageView.setImageBitmap(BitmapMaker.GetLargeBitmap(data.getPicPath()));
			typeView.setText(data.getTypeString());
			remarkView.setText(data.getRemark());
			saveTimeView.setText(data.getSaveTime());
			targetLatlngView.setText(String.format("%.6f", data.getTargetLat()) + "," +String.format("%.6f", data.getTargetLng()));
			userLatlngView.setText(String.format("%.6f", data.getUserLat()) + "," +String.format("%.6f", data.getUserLng()));
			LatLng   pt1 = new LatLng(data.getTargetLat(),data.getTargetLng());
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
			if(data.isSubmit()){
				submitTimeRow.setVisibility(View.VISIBLE);
				authorityRow.setVisibility(View.VISIBLE);
				submitTimeView.setText(data.getSubmitTime());
				submitButton.setBackgroundResource(drawable.disable_button_shape);
				submitButton.setEnabled(false);
				submitButton.setText("已上传");
				if(data.isPicAuthenticity()){
					authorityView.setText("True");
				}
				else{
					authorityView.setText("False");
				}
			}
			else{
				submitTimeRow.setVisibility(View.GONE);
				authorityRow.setVisibility(View.GONE);
				submitButton.setText("UPLOAD");
				submitButton.setEnabled(true);
			}
	    }
	   
}
