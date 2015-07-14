package com.example.campusdetectionv2.support;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.example.campusdetectionv2.BrowsingInfoActivity;
import com.example.campusdetectionv2.BrowsingInfoActivity.GetImageTask;
import com.example.campusdetectionv2.MainActivity;
import com.example.campusdetectionv2.ReportFragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;

public class NetUtil {
	
 
	  final static String  baseUrl="http://1.campusdetection.sinaapp.com/";
	  public final static String   baseStorageUrl = "http://campusdetection-uploads.stor.sinaapp.com/";
	
	//发送个人头像
	public static JSONObject uploadSubmit(String url, Map<String, String> param,File file) throws Exception {
		System.out.println("11111");
		HttpPost post = new HttpPost(baseUrl+url);  
		HttpClient httpClient=new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity();
		if (param != null && !param.isEmpty()) {
			for (Map.Entry<String, String> entry : param.entrySet()) {     
				if (entry.getValue() != null
						&& entry.getValue().trim().length() > 0) {
					entity.addPart(entry.getKey(),new StringBody(entry.getValue(),
							Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
				}
			}
		}
		// 添加文件参数
		if (file != null && file.exists()) {
			entity.addPart("file", new FileBody(file));
		}
		post.setEntity(entity);  
		HttpResponse response = httpClient.execute(post);
		int stateCode = response.getStatusLine().getStatusCode();
		StringBuffer sb = new StringBuffer();
		System.out.println("222");
		if (stateCode == HttpStatus.SC_OK) {
			System.out.println("333");
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {					 
						sb.append(tempLine + "\n");
				}
			}
		}
		post.abort();
		String jString=sb.toString();
		Log.i("tag", jString); 
		JSONObject jsonObject = new JSONObject(jString);
		return jsonObject;
	}
	public static JSONObject signIn(String username,String password) throws Exception
	{
		String url = "sign_inV2.php";
		HttpPost post = new HttpPost(baseUrl+url);
		HttpClient httpClient=new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity();
		 
			entity.addPart("username",new StringBody(username,
					Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
			entity.addPart("password",new StringBody(password,
					Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));		
		post.setEntity(entity);  
		HttpResponse response = httpClient.execute(post);
		StringBuffer sb = new StringBuffer();
		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {					 
						sb.append(tempLine + "\n");
				}
			}
		}
		post.abort();
		String jString=sb.toString();
		Log.i("tag", jString); 
		JSONObject jsonObject = new JSONObject(jString);
		return jsonObject;		
	}	
	public static JSONObject signUp(String username,String password,String mail) throws Exception
	{
		String url = "sign_upV2.php";
		HttpPost post = new HttpPost(baseUrl+url);
		HttpClient httpClient=new DefaultHttpClient();
		MultipartEntity entity = new MultipartEntity();
		 
			entity.addPart("username",new StringBody(username,
					Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));
			entity.addPart("password",new StringBody(password,
					Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));	
			entity.addPart("mail",new StringBody(password,
					Charset.forName(org.apache.http.protocol.HTTP.UTF_8)));	
		post.setEntity(entity);  
		HttpResponse response = httpClient.execute(post);
		StringBuffer sb = new StringBuffer();
		int stateCode = response.getStatusLine().getStatusCode();
		if (stateCode == HttpStatus.SC_OK) {
			HttpEntity result = response.getEntity();
			if (result != null) {
				InputStream is = result.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String tempLine;
				while ((tempLine = br.readLine()) != null) {					 
						sb.append(tempLine + "\n");
				}
			}
		}
		post.abort();
		String jString=sb.toString();
		Log.i("tag", jString); 
		JSONObject jsonObject = new JSONObject(jString);
		return jsonObject;		
	}
	public static String download(String urlStr) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader buffer = null;
		try {
			// 创建一个URL对象
			URL url = new URL(baseUrl+urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			// 使用IO流读取数据
			buffer = new BufferedReader(new InputStreamReader(urlConn
					.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	public static File downloadPic(String urlStr,GetImageTask task) {
		File file = null;
		try {
			// 创建一个URL对象
			URL url = new URL(baseStorageUrl+urlStr);
			// 创建一个Http连接
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			int fileSize = urlConn.getContentLength();
			// 使用IO流读取数据
			file = 	new File(Environment.getExternalStoragePublicDirectory(
		              Environment.DIRECTORY_PICTURES), "CampuseDetectionV2/download"+urlStr);
			/*file = new File(urlStr);*/
			InputStream ins = urlConn.getInputStream();
			OutputStream os = new FileOutputStream(file);
			   int bytesRead = 0;
			   int count = 0;
			   byte[] buffer = new byte[1000];
			   while ((bytesRead = ins.read(buffer, 0, buffer.length)) != -1) {
			      os.write(buffer, 0, bytesRead);
			      count += bytesRead;
				   task.doProgress((int)((count*100)/fileSize));  
			   }			   
			   os.close();
			   ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}
