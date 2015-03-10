package com.example.campusdetectionv2.support;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import android.content.ContentValues; 
import android.content.Context; 
import android.database.Cursor; 
import android.database.sqlite.SQLiteDatabase; 
import android.database.sqlite.SQLiteOpenHelper; 

public class ReportDataBase extends SQLiteOpenHelper { 
 
	private final static int DATABASE_VERSION = 1; 
	public final static String TABLENAME = "report_info_table"; 
	public final static String ID = "report_id";
	public final static String SAVE_TIME = "report_time"; 
	public final static String TYPE = "report_type"; 
	public final static String USER_LAT = "user_lat"; 
	public final static String USER_LNG = "user_lng"; 
	public final static String TARGET_LAT = "target_lat";
	public final static String TARGET_LNG = "target_lng";
	public final static String REMARK = "remark";
	public final static String PICPATH = "picpath";
	public final static String SUBMIT_TIME = "submit_time";
	public final static String SUBMIT="submit";
	public final static String AUTHENTICITY="pic_authenticity";
 
	public static String Sname;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	 
	
	public ReportDataBase(Context context, String username){ 
		super(context,username+".db",null,DATABASE_VERSION);
		Sname = username;
	}

	//create table 
	@Override 
	public void onCreate(SQLiteDatabase db) { 
	String sql = "CREATE TABLE " + TABLENAME + " (" + ID 
	+ " INTEGER primary key autoincrement, " + 
			SAVE_TIME + " text, "+ TYPE + " text, "+ USER_LAT + " text, "+ USER_LNG +" text, " 
	+TARGET_LAT +" text, " + TARGET_LNG + " text, " + REMARK + " text, " + PICPATH + " text, " +SUBMIT_TIME+" text, "+ SUBMIT + " text, " + AUTHENTICITY + " text);"; 
	db.execSQL(sql); 
	ContentValues cv = new ContentValues(); 
	long time=System.currentTimeMillis()-60000*61*8;
	String TIME = formatter.format((new Date(System.currentTimeMillis()-60000*60*8)));
	  
		cv.put(SAVE_TIME,"2"); 
		cv.put(TYPE, "2");
		cv.put(USER_LAT, "2");
		cv.put(USER_LNG,"2");
		cv.put(TARGET_LAT,"2");
		cv.put(TARGET_LNG, "2");
		cv.put(REMARK,"2"); 
		cv.put(PICPATH,"2"); 
		cv.put(SUBMIT_TIME,"2"); 
		cv.put(SUBMIT,"2"); 
		cv.put(AUTHENTICITY, "2");
		long row; 
		  row = db.insert(TABLENAME, null, cv); 
		  cv.clear();
	} 
	@Override 
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { 
	String sql = "DROP TABLE IF EXISTS " + TABLENAME; 
	db.execSQL(sql); 
	onCreate(db); 
	} 

	public Cursor select() { 
	SQLiteDatabase db = this.getReadableDatabase(); 
	Cursor cursor = db 
	.query(TABLENAME, null, null, null, null, null,SAVE_TIME + " desc"); 
	return cursor; 
	} 
	 
	 
	//删除操作 
	public void delete(int id) 
	{ 
	SQLiteDatabase db = this.getWritableDatabase(); 
	String where = ID + " = ?"; 
	String[] whereValue ={ Integer.toString(id) }; 
	db.delete(TABLENAME, where, whereValue); 
	} 
	//修改操作 
	/*public void update(int id, String time,String lat, String lon) 
	{ 
	SQLiteDatabase db = this.getWritableDatabase(); 
	String where = Location_ID + " = ?"; 
	String[] whereValue = { Integer.toString(id) }; 

	ContentValues cv = new ContentValues(); 
	cv.put(Location_TIME, time); 
	cv.put(Location_LAT, lat); 
	cv.put(Location_LON, lon);
	db.update(TABLENAME, cv, where, whereValue); 
	} */
 	 
//	public void deleteTable(){
//		SQLiteDatabase db = this.getWritableDatabase();
//		String sql = "DROP TABLE " + Sname + ".db" + ";";
//		db.execSQL(sql);
//	}
 	
 
	}


