package com.example.campusdetectionv2.support;

import java.io.IOException;

import com.example.campusdetectionv2.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapMaker {
	public static Bitmap GetLargeBitmap(String picPath)
	{
		Bitmap bmp;	 	
		bmp =  BitmapFactory.decodeFile(picPath, null);
		if(bmp!=null)
		{
			//String imgPath=fileUri.getPath();
				int degree=getExifOrientation(picPath);
				ExifInterface exif;
			try {
			exif = new ExifInterface(picPath);				
				int legth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1);
				int wildth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1);
			 
                //Roate preview icon according to exif orientation
				 Matrix matrix = new Matrix();
                matrix.postRotate(degree);
               // matrix.postScale(3,4); 
                 bmp = Bitmap.createBitmap(
             		   bmp,
                       0, 0,wildth, legth,  matrix, true);	  
				if(degree==90||degree==270)
				{	                
                 bmp = Bitmap.createScaledBitmap(bmp, 300,400, true);
				}
				else
				{
				   bmp = Bitmap.createScaledBitmap(bmp, 300,225, true);
				}
				} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	 
		 return bmp;
	}
	public static Bitmap GetSmallBitmap(String picPath)  
	{
		 	Bitmap bmp;	 	
			bmp =  BitmapFactory.decodeFile(picPath, null);
			if(bmp!=null)
			{
				//String imgPath=fileUri.getPath();
 				int degree=getExifOrientation(picPath);
 				ExifInterface exif;
				try {
				exif = new ExifInterface(picPath);				
 				int legth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1);
 				int wildth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1);
				 
	                //Roate preview icon according to exif orientation
 				 Matrix matrix = new Matrix();
	                matrix.postRotate(degree);
	               // matrix.postScale(3,4); 
	                 bmp = Bitmap.createBitmap(
	             		   bmp,
	                       0, 0,wildth, legth,  matrix, true);	  
 				if(degree==90||degree==270)
 				{	                
	                 bmp = Bitmap.createScaledBitmap(bmp, 60, 80, true);
 				}
 				else
 				{
 				   bmp = Bitmap.createScaledBitmap(bmp, 60,45 , true);
 				}
 				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 
			 return bmp;
	}
	public static Bitmap GetRealsizeBitmap(String picPath)  
	{
		 	Bitmap bmp;	 	
			bmp =  BitmapFactory.decodeFile(picPath, null);
			if(bmp!=null)
			{
				//String imgPath=fileUri.getPath();
 				int degree=getExifOrientation(picPath);
 				ExifInterface exif;
				try {
				exif = new ExifInterface(picPath);				
 				int legth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, -1);
 				int wildth=exif.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, -1);
				 
	                //Roate preview icon according to exif orientation
 				 Matrix matrix = new Matrix();
	                matrix.postRotate(degree);
	               // matrix.postScale(3,4); 
	                 bmp = Bitmap.createBitmap(
	             		   bmp,
	                       0, 0,wildth, legth,  matrix, true);	   		 
 				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		 
			 return bmp;
	}
	 
	public static int getExifOrientation(String filepath) {
	       int degree = 0;
	       ExifInterface exif = null;

	       try {
	           exif = new ExifInterface(filepath);
	       } catch (IOException ex) {
	          // MmsLog.e(ISMS_TAG, "getExifOrientation():", ex);
	       }

	       if (exif != null) {
	           int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
	           if (orientation != -1) {
	               // We only recognize a subset of orientation tag values.
	               switch (orientation) {
	               case ExifInterface.ORIENTATION_ROTATE_90:
	                   degree = 90;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_180:
	                   degree = 180;
	                   break;

	               case ExifInterface.ORIENTATION_ROTATE_270:
	                   degree = 270;
	                   break;
	               default:
	                   break;
	               }
	           }
	       }

	       return degree;
	   } 

}
