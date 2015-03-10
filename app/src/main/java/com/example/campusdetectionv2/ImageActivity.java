package com.example.campusdetectionv2;

import com.example.campusdetectionv2.support.BitmapMaker;
import com.example.campusdetectionv2.view.ZoomImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;

public class ImageActivity extends Activity{
	private ZoomImageView zoomImageView;  
	  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.activity_image);  
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);  
        String imagePath = getIntent().getStringExtra("PICPATH");  
        Bitmap bitmap = BitmapMaker.GetRealsizeBitmap(imagePath);  
        zoomImageView.setImageBitmap(bitmap);         
    }  

}
