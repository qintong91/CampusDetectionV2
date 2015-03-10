package com.example.campusdetectionv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class SplashActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 1000; // 延迟六秒  
	private SharedPreferences sp; 
    private MyApp myApp;
    private String username=null;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);  
        sp = getSharedPreferences(SignInActivity.USER_INFO, Context.MODE_PRIVATE);
        username=sp.getString(SignInActivity.USERNAME,null);
        new Handler().postDelayed(new Runnable() {  
            public void run() {
            	if(sp.getBoolean(SignInActivity.AUTO_SIGNIN,false)&&username!=null){
            		myApp = (MyApp)getApplication();
					myApp.setUsername(username);
					Intent mainIntent = new Intent(SplashActivity.this,  
                            MainActivity.class);  
                    SplashActivity.this.startActivity(mainIntent);  
                    SplashActivity.this.finish();   
            	}
            	else{
            		Intent mainIntent = new Intent(SplashActivity.this,  
                            SignInActivity.class);  
                    SplashActivity.this.startActivity(mainIntent);  
                    SplashActivity.this.finish();  
            		
            	}
            }  
        }, SPLASH_DISPLAY_LENGHT);  
    }
}
