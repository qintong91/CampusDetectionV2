package com.example.campusdetectionv2;

import java.io.File;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.example.campusdetectionv2.support.BitmapMaker;
import com.example.campusdetectionv2.view.ZoomImageView;

public class ReportImageActivity extends Activity{
	private ZoomImageView zoomImageView;  
	ActionBar actionBar;
	String imagePath;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_report_image); 
        actionBar=getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);//隐藏Label标签
        actionBar.setDisplayShowHomeEnabled(false);//隐藏logo和icon
        actionBar.show();
        zoomImageView = (ZoomImageView) findViewById(R.id.report_zoom_image_view);  
        imagePath = getIntent().getStringExtra("PICPATH");  
        Bitmap bitmap = BitmapMaker.GetRealsizeBitmap(imagePath);  
        zoomImageView.setImageBitmap(bitmap);         
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {  
        getMenuInflater().inflate(R.menu.menu_report_iamge, menu);  
        return true;  
    }
    @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {  
        // Respond to the action bar's Up/Home button  
        case R.id.action_delete: 
			new AlertDialog.Builder(this)
					.setTitle("删除")
					.setMessage("确定删除该图片?")
					.setIcon(android.R.drawable.ic_delete)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									new File(imagePath).delete();  
									setResult(RESULT_OK);// 确定按钮事件
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 取消按钮事件
								}
							}).show();
        	
            return true;  
        }  
        return super.onOptionsItemSelected(item);  
    }  
}
