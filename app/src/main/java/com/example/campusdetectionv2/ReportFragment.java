package com.example.campusdetectionv2;

 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
 
import com.example.campusdetectionv2.support.BitmapMaker;
 
import com.example.campusdetectionv2.support.MD5Util;
import com.example.campusdetectionv2.support.ReportData;
 
import com.example.campusdetectionv2.view.RoundedImageView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
 
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ReportFragment extends Fragment{
	MapView mMapView = null;  
	BaiduMap mBaiduMap=null;
	BDLocation location=null;
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	public BDLocationListener myLocListener=null; 
	boolean isFirstLoc = true;// 是否首次定位
	public RoundedImageView imageView=null;
	public RadioGroup radioGroup=null;
	public Button buttonGetLoc=null;
	public Button buttonSave=null;
	public Button buttonSubmit=null;
	public RadioButton radioButtonGarbage=null;
	public RadioButton radioButtonDischarge=null;
	public RadioButton radioButtonDamage=null;
	public EditText remarkText=null;
	public TextView textPleaseGetImage=null;
	public FrameLayout imageFrameLayout=null;
	private ProgressDialog pDialog;
	public String type=null;
	public String remark="";
	public double targetLat=0;
	public double targetLng=0;
	public double userLat=0;
	public double userLng=0;
	public ReportData data=null;
	public Uri fileUri=null;
	LayoutParams para=null;
	public static final int MEDIA_TYPE_IMAGE = 1;
	boolean getImage=false;
	InputStream is = null;
	public static String picPath=null;
	public  MyApp myApp;
	public String username;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState)  
    {   
		SDKInitializer.initialize(this.getActivity().getApplicationContext());  		  
        View view = inflater.inflate(R.layout.fragment_report, container, false); 
        imageView=(RoundedImageView)view.findViewById(R.id.imageView);
        imageFrameLayout=(FrameLayout)view.findViewById(R.id.imageFrameLayout);        
    /*    para = (LayoutParams) imageFrameLayout.getLayoutParams();  
		para.width = (int) ((para.height)*0.75);
	    imageFrameLayout.setLayoutParams(para);*/
	  

        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        buttonGetLoc=(Button)view.findViewById(R.id.get_location_button);
        buttonSave=(Button)view.findViewById(R.id.save_button);
        buttonSubmit=(Button)view.findViewById(R.id.submit_button);
        radioButtonGarbage=(RadioButton)view.findViewById(R.id.radioGarbage);
        radioButtonDischarge=(RadioButton)view.findViewById(R.id.radioDischarge);
        radioButtonDamage=(RadioButton)view.findViewById(R.id.radioDamage);
        remarkText=(EditText)view.findViewById(R.id.editText);
        textPleaseGetImage=(TextView)view.findViewById(R.id.getImageText);
        mMapView = (MapView)view.findViewById(R.id.bmapView); 
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();   
        // 开启定位图层  
        mBaiduMap.setMyLocationEnabled(true);        
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）  
        mLocClient = new LocationClient(this.getActivity());
        mLocClient.registerLocationListener(myListener);        
        LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		buttonGetLoc.setOnClickListener(new LocClickListener());
		imageView.setOnClickListener(new ImageViewCickListener());
		buttonSave.setOnClickListener(new SaveClickListener());
		buttonSubmit.setOnClickListener(new SubmitClickListener());	
	       ViewTreeObserver vto = imageFrameLayout.getViewTreeObserver(); 
	       vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { 
	       public boolean onPreDraw() { 
	 	  LinearLayout.LayoutParams para=null;
		    para = (android.widget.LinearLayout.LayoutParams) imageFrameLayout.getLayoutParams();  
			para.width = (int) ((imageFrameLayout.getMeasuredHeight())*0.75); 
	       return true; 
	       } 
	       });
	       MyApp myApp=(MyApp)getActivity().getApplication();
	       username = myApp.getUsername();

        return view;  
    } 
	@Override
	public void onPause() {
		mMapView.onPause();
		mLocClient.stop();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
	
		mLocClient.start();
		isFirstLoc=true;
		 if(targetLat!=0)
		 {
	 		LatLng   pt1 = new LatLng(targetLat,targetLng);
			OverlayOptions ooA; 
			BitmapDescriptor target_icon;
			target_icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
			 ooA = new MarkerOptions().position(pt1).icon(target_icon)
	      			.zIndex(9);
			 mBaiduMap.addOverlay(ooA);	
		 }
		 if(picPath!=null){
			 if(!(new File(picPath).exists())){
				 fileUri=null;
				 picPath=null;
				 getImage=false;
				 imageView.setImageBitmap(null); 
				 textPleaseGetImage.setVisibility(View.VISIBLE);
			 }
		 }
		
		 if(getImage)
		 {
			                         	                  					  
					 imageView.setImageBitmap(BitmapMaker.GetLargeBitmap(picPath)); 
					 textPleaseGetImage.setVisibility(View.GONE);				 	 
		 }
		 
		  
		super.onResume();
	}

	@Override
	public void onDestroy() {
		 
		
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 18);
				mBaiduMap.animateMapStatus(u);
			}
		}
		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	class LocClickListener implements View.OnClickListener
	{	
		   @Override
		   public void onClick(View v) {
		   Intent i = new Intent(getActivity().getApplicationContext(),
					   LocActivity.class);
		   mBaiduMap.clear();
		   getActivity().startActivityForResult(i,MainActivity.LOCATION_ACTIVITY_REQUEST_CODE);				 
		    }		   
	}
	class ImageViewCickListener implements View.OnClickListener
	{
		  @Override
		   public void onClick(View v) {
			  if(!getImage){
				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
			 	fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				 System.out.println(fileUri);
		 		intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);			 
				getActivity().startActivityForResult(intent,MainActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);	
			  }
			  else{
				  	Intent intent = new Intent(ReportFragment.this.getActivity(), ReportImageActivity.class);  
	             	Bundle mBundle = new Bundle();  
	                mBundle.putString("PICPATH",picPath);  
	                intent.putExtras(mBundle);
	             	startActivity(intent);
			  }
		    }

	}
	class SaveClickListener implements View.OnClickListener
	{
		  @Override
		  
		   public void onClick(View v) {
 

			  remark=remarkText.getText().toString();
			  if(radioButtonGarbage.isChecked())
				 {
					 type="Garbage";
				 }
				 else if(radioButtonDischarge.isChecked())
				 {
					 type="Discharge";
				 }
				 else if(radioButtonDamage.isChecked())
				 {
					 type="Damage";
				 }
			 if(remark!=""&&type!=null&&Math.abs(targetLat)>1&&Math.abs(targetLng)>1&&getImage)
			 {
				 ReportData data=new ReportData(new Date(System.currentTimeMillis()), type, userLat, userLng, targetLat, targetLng, picPath, remark, false, null,false);
				 if(data.save(getActivity(), username))
				 {
					 resetInput();
					 Toast.makeText(getActivity().getApplicationContext(),"保存成功", Toast.LENGTH_LONG).show();					 
				 }
				 else 
					 Toast.makeText(getActivity().getApplicationContext(),"保存失败", Toast.LENGTH_LONG).show();
				 		 
			 }
			 else
			 {
				 StringBuffer sb=new StringBuffer();
				 sb.append("请添加");
				 sb.append(type==null?" 举报类型":"");
				 sb.append(remark.equals("")?" 相关描述":"");
				 sb.append(!getImage?" 污染源图片":"");
				 sb.append(Math.abs(targetLat)<1&&Math.abs(targetLng)<1?" 污染源位置":"");				  
				 new AlertDialog.Builder(getActivity())
		          .setMessage(sb.toString())
		          .setPositiveButton("确定",
		                         new DialogInterface.OnClickListener(){
		                                 public void onClick(DialogInterface dialoginterface, int i){
		                                     //按钮事件
		                                  }
		                          })
		          .show(); 
			 }		 			   		 
		    }	
	}
	class SubmitClickListener implements View.OnClickListener
	{
		  @Override
		   public void onClick(View v) {
			  remark=remarkText.getText().toString();
			  if(radioButtonGarbage.isChecked())
				 {
					 type="Garbage";
				 }
				 else if(radioButtonDischarge.isChecked())
				 {
					 type="Discharge";
				 }
				 else if(radioButtonDamage.isChecked())
				 {
					 type="Damage";
				 }
			 if(remark!=""&&type!=null&&Math.abs(targetLat)>1&&Math.abs(targetLng)>1&&getImage)
			 {

				  data=new ReportData(new Date(System.currentTimeMillis()), type, userLat, userLng, targetLat, targetLng, picPath, remark, false, null,false);
				  SubmitTask asyncTask = new SubmitTask();
				  asyncTask.execute();
                  resetInput();
			 }
			 else
			 {
				 StringBuffer sb=new StringBuffer();
				 sb.append("请添加");
				 sb.append(type==null?" 举报类型":"");
				 sb.append(remark.equals("")?" 相关描述":"");
				 sb.append(!getImage?" 污染源图片":"");
				 sb.append(Math.abs(targetLat)<1&&Math.abs(targetLng)<1?" 污染源位置":"");				  
				 new AlertDialog.Builder(getActivity())
		          .setMessage(sb.toString())
		          .setPositiveButton("确定",
		                         new DialogInterface.OnClickListener(){
		                                 public void onClick(DialogInterface dialoginterface, int i){
		                                     //按钮事件
		                                  }
		                          })
		          .show(); 
			 }		 			   				   		 
		    }	
	}
	 
	
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "CampuseDetectionV2");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    }  
	    else {
	        return null;
	    }

	    return mediaFile;
	}
	 private boolean resetInput()
	{
		 type=null;
		 remark="";
		 targetLat=0;
		 targetLng=0;
		 userLat=0;
		 userLng=0;
		 fileUri=null;
		 picPath=null;
		 getImage=false;
		 radioGroup.clearCheck();
		 imageView.setImageBitmap(null); 
		 textPleaseGetImage.setVisibility(View.VISIBLE);
		 remarkText.setText(null);	
		 mBaiduMap.clear();	
		
		 return true;
	}
	 private class SubmitTask extends AsyncTask<String, Integer, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(getActivity());
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
				boolean save=data.save(getActivity(), username);
				Message msg = msgHandler.obtainMessage();
				if(submitState==ReportData.UN_SUBMIT&&save)
				{
					msg.what = 0x34;
				}
				else if(submitState==ReportData.SUBMIT_REAL&&save)
				{
					msg.what = 0x35;
				}
				else if(submitState==ReportData.SUBMIT_FAKE&&save)
				{
					msg.what = 0x36;
				}
				else if(submitState==ReportData.UN_SUBMIT&&(!save))
				{
					msg.what = 0x37;
				}
				else if(submitState==ReportData.SUBMIT_REAL&&(!save))
				{
					msg.what = 0x38;
				}
				else if(submitState==ReportData.SUBMIT_FAKE&&(!save))
				{
					msg.what = 0x39;
				}
				msgHandler.sendMessage(msg);
				//resetInput();
				return null;
				
			}
			
			/**
			 * After completing background task Dismiss the progress dialog
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
			}

		}
	 private final Handler msgHandler = new Handler()
		{
	        @SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) 
	        {
	                switch (msg.what) {
	                case 0x34:
	                	Toast.makeText(getActivity(), "上传失败|保存成功", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x35:
	                	Toast.makeText(getActivity(), "上传成功|图片为真|保存成功", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x36:
	                	Toast.makeText(getActivity(), "上传成功|图片为假|保存成功", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x37:
	                	Toast.makeText(getActivity(), "上传失败|保存失败", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x38:
	                	Toast.makeText(getActivity(), "上传成功|图片为真|保存失败", Toast.LENGTH_SHORT).show();
	                        break;
	                case 0x39:
	                	Toast.makeText(getActivity(), "上传成功|图片为假|保存失败", Toast.LENGTH_SHORT).show();
	                        break;
	                default:
	                        break;
	                }
	        }
	    };

	
}
