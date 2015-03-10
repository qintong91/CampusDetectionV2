package com.example.campusdetectionv2.support;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
 

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.Data;
import android.util.Log;

public class ReportData implements Serializable{
	public static final int SUBMIT_REAL=100;
	public static final int SUBMIT_FAKE=200;
	public static final int UN_SUBMIT=300;
	Date saveTime;
	String type;
	double userLat;
	double userLng;
	double targetLat;
	double targetLng;
	String picPath;
	String remark;
	boolean submit;
	Date submitTime;
	boolean picAuthenticity=false;
	
	static	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final String TAG_SUCCESS = "success";
	private static final String SUBMIT_TIME = "submit_time";
	private static final String AUTHENTICITY = "authenticity";
	public ReportData(Date saveTime, String type, double userLat,
			double userLng, double targetLat, double targetLng, String picPath,
			String remark, boolean submit, Date submitTime,boolean picAuthenticity) {
		super();
		this.saveTime = saveTime;
		this.type = type;
		this.userLat = userLat;
		this.userLng = userLng;
		this.targetLat = targetLat;
		this.targetLng = targetLng;
		this.picPath = picPath;
		this.remark = remark;
		this.submit = submit;
		this.submitTime = submitTime;
		this.picAuthenticity=picAuthenticity;
			
	}
	public boolean save(Context context, String username)//保存至数据库
	{
		ReportDataBase rb=new ReportDataBase(context,username);
		SQLiteDatabase db = rb.getWritableDatabase(); 
		ContentValues cv = new ContentValues(); 
		cv.put(ReportDataBase.SAVE_TIME,formatter.format(saveTime)); 
		cv.put(ReportDataBase.TYPE, type);
		cv.put(ReportDataBase.USER_LAT,userLat);
		cv.put(ReportDataBase.USER_LNG,userLng);
		cv.put(ReportDataBase.TARGET_LAT,targetLat);
		cv.put(ReportDataBase.TARGET_LNG,targetLng);
		cv.put(ReportDataBase.REMARK,remark); 
		cv.put(ReportDataBase.PICPATH,picPath);
		if(submitTime!=null)
		{
			cv.put(ReportDataBase.SUBMIT_TIME,formatter.format(submitTime)); 
		}
		
		if(submit==true)
			cv.put(ReportDataBase.SUBMIT,"true"); 
		else 
			cv.put(ReportDataBase.SUBMIT,"false"); 
		if(picAuthenticity==true)
			cv.put(ReportDataBase.AUTHENTICITY,"true"); 
		else 
			cv.put(ReportDataBase.AUTHENTICITY,"false"); 
		long row; 
		row = db.insert(ReportDataBase.TABLENAME, null, cv); 
		cv.clear();
		if(row==-1)
			return false;
		else 
			return true;
	}

	public int submit(final String username)// 提交并更新数据库
	{
		File f = new File(picPath);
		final Map<String, String> map = new HashMap<String, String>();
		map.put("username", username);
		map.put("saveTime", formatter.format(saveTime));
		map.put("type", type);
		map.put("userLat", Double.toString(userLat));
		map.put("userLng", Double.toString(userLng));
		map.put("targetLat", Double.toString(targetLat));
		map.put("targetLng", Double.toString(targetLng));
		map.put("remark", remark);
		try {
			JSONObject result;
			result = NetUtil.uploadSubmit("up_ImageV2.php", map, f); 
			Log.i("tag" , ""+result);
			int success = result.getInt(TAG_SUCCESS);
			int authenticity = result.getInt(AUTHENTICITY);
			if (success == 0) {
				this.submit=false;
				return UN_SUBMIT;
			}
			else 
			{
				this.submit=true;
				this.submitTime=formatter.parse(result.getString(SUBMIT_TIME));
				if(authenticity == 0)
				{
					this.picAuthenticity=false;
					return SUBMIT_FAKE;
				}
				else if(authenticity == 1)
				{
					this.picAuthenticity=true;
					return SUBMIT_REAL;
				}
				
					
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return UN_SUBMIT;
		}
		return UN_SUBMIT;
	}
	 
	public String getSaveTime() {
		return formatter.format(saveTime);
	}
	public String getType() {
		return type;
	}
	public double getUserLat() {
		return userLat;
	}
	public double getUserLng() {
		return userLng;
	}
	public double getTargetLat() {
		return targetLat;
	}
	public double getTargetLng() {
		return targetLng;
	}
	public String getPicPath() {
		return picPath;
	}
	public String getRemark() {
		return remark;
	}
	public boolean isSubmit() {
		return submit;
	}
	public String getSubmitTime() {
		return formatter.format(submitTime);
	}
	public boolean isPicAuthenticity() {
		return picAuthenticity;
	}
	public String getTypeString()
	{
		String str="";
		switch(type){
		case "Garbage" : 
			str="垃圾污染";
			break;
		case "Discharge" : 
			str="污染排放";
			break;
		case "Damage" : 
			str="设施损坏";
		}
		return str;
	}
	public boolean saveSubmitInfo(Context context,String username)
	{
		ContentValues cv = new ContentValues();
		cv.put(ReportDataBase.SUBMIT,"true");
		cv.put(ReportDataBase.SUBMIT_TIME,getSubmitTime());
		if(picAuthenticity)
			cv.put(ReportDataBase.AUTHENTICITY,"true");
		else
			cv.put(ReportDataBase.AUTHENTICITY,"false");
		
		String[] args = {String.valueOf(picPath)};
		ReportDataBase rb=new ReportDataBase(context,username);
		SQLiteDatabase db = rb.getWritableDatabase(); 
		long row;
		row = db.update(ReportDataBase.TABLENAME, cv,ReportDataBase.PICPATH  + "=?",args);
		cv.clear();
		if(row==-1)
			return false;
		else 
			return true;
	}
	public static ArrayList<ReportData> getReportData(Context context, String username)
	{
		ArrayList<ReportData> list=new ArrayList<ReportData>();
		ReportDataBase rb=new ReportDataBase(context,username);	
		Cursor cursor=rb.select();
		while(cursor.moveToNext()){
			Date saveTime;
			try {
				saveTime = formatter.parse(cursor.getString(cursor.getColumnIndex(ReportDataBase.SAVE_TIME )));
				String type = cursor.getString(cursor.getColumnIndex(ReportDataBase.TYPE ));
				Double userLat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ReportDataBase.USER_LAT ))); 
				Double userLng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ReportDataBase.USER_LNG ))); 
				Double targetLat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ReportDataBase.TARGET_LAT )));
				Double targetLng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(ReportDataBase.TARGET_LNG )));
				String picPath = cursor.getString(cursor.getColumnIndex(ReportDataBase.PICPATH ));
				String remark = cursor.getString(cursor.getColumnIndex(ReportDataBase.REMARK ));
				Date submitTime=null;
				boolean submit;
				boolean authenticity;
				 if(cursor.getString(cursor.getColumnIndex(ReportDataBase.SUBMIT )).equals("true"))
				 {
					 submit=true;
					 submitTime = formatter.parse(cursor.getString(cursor.getColumnIndex(ReportDataBase.SUBMIT_TIME )));
				 }
				 else submit=false;
				 if(cursor.getString(cursor.getColumnIndex(ReportDataBase.AUTHENTICITY )).equals("true"))
				 {
					 authenticity=true;
 				 }
				 else authenticity=false;
				list.add(new ReportData(saveTime, type, userLat, userLng, targetLat, targetLng, picPath, remark, submit, submitTime,authenticity));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
            
      }

		
		return list;
	}
	 
  
	 
}
