package com.example.campusdetectionv2;

 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.campusdetectionv2.support.MD5Util;
import com.example.campusdetectionv2.support.ReportDataBase;
 
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
 
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	 private FragmentTabHost mTabHost = null;;  
	 private View indicator = null; 
	 private TabWidget mTabWidget=null;
	 public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	 public static final int LORD_IMAGE_ACTIVITY_REQUEST_CODE = 300;
	 public static final int LOCATION_ACTIVITY_REQUEST_CODE = 200;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.activity_main);
  		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);  
  		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		//TabWidget tabW=(TabWidget) findViewById(R.id.tabs);  
	//	mTabHost.getTabWidget().setShowDividers( LinearLayout.SHOW_DIVIDER_NONE);      
        mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
        mTabWidget.setBackgroundColor(Color.WHITE);
        mTabWidget.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabWidget.setGravity(Gravity.BOTTOM); 
        indicator = getIndicatorView("举报", R.layout.report_indicator);	     
       /* mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(indicator),
        		ReportFragment.class, null);*/
         mTabHost.addTab(mTabHost.newTabSpec("Report").setIndicator(indicator),
        		 ReportFragment.class, null);   
        indicator = getIndicatorView("记录", R.layout.data_indicator);	     
        mTabHost.addTab(mTabHost.newTabSpec("Data")  
                .setIndicator(indicator), DataFragment.class, null);  
        indicator = getIndicatorView("浏览", R.layout.browsing_indicator);	     
        mTabHost.addTab(mTabHost.newTabSpec("Browsing")  
                .setIndicator(indicator), BrowsingFragment.class, null);  
        indicator = getIndicatorView("噪声", R.layout.noise_indicator);	     
        mTabHost.addTab(mTabHost.newTabSpec("Noise")  
                .setIndicator(indicator), NoiseFragment.class, null);  
        indicator = getIndicatorView("设置", R.layout.setting_indicator);	     
        mTabHost.addTab(mTabHost.newTabSpec("Setting")  
                .setIndicator(indicator), SettingFragment.class, null);
       ReportDataBase db=new ReportDataBase(this,"qin");
       	    		
	}
	private View getIndicatorView(String name, int layoutId) {
		// TODO Auto-generated method stub
		 View v = getLayoutInflater().inflate(layoutId, null);  
	        TextView tv = (TextView) v.findViewById(R.id.tabText);  
	        tv.setText(name);  
	        return v;  
		 
	}
	@Override
	protected void	onActivityResult(int requestCode, int resultCode, Intent data) {
		//File temp = new File(Environment.getExternalStorageDirectory().getPath()+"/campusdet/Camera/"
			//	+timeString+".jpg");
		//startPhotoZoom(Uri.fromFile(temp));
		super.onActivityResult(requestCode, resultCode, data);
		   ReportFragment reportFragment = (ReportFragment)getSupportFragmentManager().findFragmentByTag("Report");
		switch (requestCode) {
		case   CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: 
			 if (resultCode == RESULT_OK) {
		            // Image captured and saved to fileUri specified in the Intent
				 /*String[] projection = { MediaStore.Images.Media.DATA};
		            Toast.makeText(this, "Image saved to:\n" +
		                     data.getData(), Toast.LENGTH_LONG).show();*/
		         
				 reportFragment.getImage=true;
				 try {
					 	InputStream is = null;
					 	String imgPath=reportFragment.fileUri.getPath();
						is = getAssets().open("publickey.txt");//   一定记得关闭流！！
						reportFragment.picPath=MD5Util.encrypt(imgPath, is);
						new File(imgPath).delete();
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
		           
		        } else if (resultCode == RESULT_CANCELED) {
		            // User cancelled the image capture
		        } else {
		            // Image capture failed, advise user
		        }
		break;
		
		case LOCATION_ACTIVITY_REQUEST_CODE:
		{
			if(resultCode==2)
			{
				String latlng=data.getStringExtra("latlng");
				String[] strarray=latlng.split(","); 
				reportFragment.userLat=data.getDoubleExtra("userlat",0);
				reportFragment.userLng=data.getDoubleExtra("userlng",0);
				reportFragment.targetLat=Double.parseDouble(strarray[0]);
				reportFragment.targetLng=Double.parseDouble(strarray[1]);
			    //System.out.println(lat+lng);
			}
		}
		break;
		
		case LORD_IMAGE_ACTIVITY_REQUEST_CODE:
			 
			break;
		
	}
	}



	 
}
