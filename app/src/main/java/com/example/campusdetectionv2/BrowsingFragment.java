package com.example.campusdetectionv2;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.model.LatLng;
import com.example.campusdetectionv2.ReportFragment.MyLocationListenner;
import com.example.campusdetectionv2.support.BrowsingData;
import com.example.campusdetectionv2.support.NetUtil;
import com.example.campusdetectionv2.support.ReportData;
import com.example.campusdetectionv2.support.XmlContentHandler;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BrowsingFragment extends Fragment{
	MapView mMapView = null;  
	BaiduMap mBaiduMap=null;
	BDLocation location=null;
	LocationClient mLocClient;
	public final static String SER_KEY = "data";
	public MyLocationListenner myListener = new MyLocationListenner();
	public BDLocationListener myLocListener=null; 
	boolean isFirstLoc = true;// 是否首次定位
	TextView testView;
	private InfoWindow mInfoWindow;
	String resultStr;
	private ProgressDialog pDialog;
	List<BrowsingData> dataList = null;
	Button button = null;
	boolean getDataSuccess = false;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstLoc = true;
		mMapView = new MapView(getActivity(),
				new BaiduMapOptions().mapStatus(new MapStatus.Builder().build())); 	
        mMapView.showZoomControls(true);
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
		DownloadTask downloadTask = new DownloadTask();
		downloadTask.execute();
		mBaiduMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
		return mMapView;	
	}
	 private class DownloadTask extends AsyncTask<String, Integer, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(getActivity());
				pDialog.setMessage("加载中..");
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
				resultStr = NetUtil.download("browsingV2.php");
				Message msg = msgHandler.obtainMessage();
				if(resultStr==null)
				{
					msg.what = 0x34;
				}
				Log.w("tag1", resultStr);
				return null;				
			}
			protected void onPostExecute(String file_url) {
				// dismiss the dialog once done
				pDialog.dismiss();
				try{					 
					//创建一个SAXParserFactory
					SAXParserFactory factory = SAXParserFactory.newInstance();
					XMLReader reader = factory.newSAXParser().getXMLReader();
					//为XMLReader设置内容处理器
					XmlContentHandler handler = new XmlContentHandler();
					reader.setContentHandler(handler);
					//开始解析文件
					reader.parse(new InputSource(new StringReader(resultStr)));
					dataList = handler.getDataList();
					getDataSuccess = handler.isSuccess();
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					StringBuffer sb = new StringBuffer();
					if(getDataSuccess){
						for(int i=0;i<dataList.size();i++){
							/*sb.append("name: ");
							sb.append(dataList.get(i).getUsername()+ "\n");
							sb.append("time: ");
							sb.append(dataList.get(i).getTime()+ "\n"+ "\n"+ "\n");*/
							LatLng   pt1 = new LatLng(dataList.get(i).getLat(),dataList.get(i).getLng());
							OverlayOptions ooA; 
							BitmapDescriptor target_icon;
							target_icon = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
							 ooA = new MarkerOptions().position(pt1).icon(target_icon)
					      			.zIndex(9);
							Marker mMarkerA = (Marker) (mBaiduMap.addOverlay(ooA));
							mMarkerA.setTitle(String.valueOf(i));
						}						
					}else{
						Toast.makeText(getActivity(), "无法连接服务器 获取失败", Toast.LENGTH_SHORT).show();
					}
					
					//testView.setText(sb.toString());
				
			}
	 }
	 private final Handler msgHandler = new Handler()
		{
	        @SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) 
	        {
	                switch (msg.what) {
	                case 0x34:
	                	Toast.makeText(getActivity(), "无法连接服务器 获取失败", Toast.LENGTH_SHORT).show();
	                        break;
	                
	                }
	        }
	    };
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
					MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);
					mBaiduMap.animateMapStatus(u);
				}
			}
			public void onReceivePoi(BDLocation poiLocation) {
			}
		}
	    public class MyOnMarkerClickListener implements OnMarkerClickListener{
	    	public boolean onMarkerClick(final Marker marker) {
				button = new Button(getActivity().getApplicationContext());
				button.setBackgroundResource(R.drawable.popup);
				button.setTextColor(Color.BLACK);
				//使覆盖物偏移固定的像素
				final LatLng ll = marker.getPosition();
				Point p = mBaiduMap.getProjection().toScreenLocation(ll);
				p.y -= 47;
				LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
						final int m=Integer.valueOf(marker.getTitle());
						button.setText(dataList.get(m).getTypeString());
						button.setOnClickListener(new OnClickListener() {							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(), BrowsingInfoActivity. class); 
				                Bundle mBundle = new Bundle(); 
				                mBundle.putSerializable(SER_KEY , (Serializable) dataList.get(m));
				                intent.putExtras(mBundle);
				                startActivity(intent);

							}
						}); 
					  				 				
				mInfoWindow = new InfoWindow(button, llInfo,0);
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
	    	}
	    }
}
