package com.example.campusdetectionv2;

 
import org.json.JSONException;
import org.json.JSONObject;

import com.example.campusdetectionv2.support.NetUtil;
 

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity{
	private Button buttonSignIn=null;
	private Button buttonSignUp=null;
    public EditText TextUsername=null;   
    //密码文本编辑框   
    private EditText TextPassword=null;  
    private CheckBox checkBoxRemPSW = null;
    private CheckBox checkBoxAutoSignIn = null; 
    private ProgressDialog pDialog;
    private MyApp myApp;
    public static final String USER_INFO = "password";
    public static final String TAG_SUCCESS = "success";
    public static final String REM_PSW = "remPsw";
    public static final String AUTO_SIGNIN = "autoSignIn";
    public static final String USERNAME = "username";
    public static final String PSW = "password";
    private SharedPreferences sp;  
    protected void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signin);
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
		buttonSignIn.setOnClickListener( new SignInClickListener());
		buttonSignUp = (Button) findViewById(R.id.buttonToSignUp);
		buttonSignUp.setOnClickListener( new SignUpClickListener());
		TextUsername = (EditText) findViewById(R.id.editTextSignInUser);   
		TextPassword = (EditText) findViewById(R.id.editTextSignInPsw);     
		checkBoxRemPSW = (CheckBox)findViewById(R.id.checkBoxRemPsw);
		checkBoxAutoSignIn = (CheckBox)findViewById(R.id.checkBoxAutoSignIn);
		//为checkBox设置监听
		checkBoxAutoSignIn.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(checkBoxAutoSignIn.isChecked())
					checkBoxRemPSW.setChecked(true);
				
			}
		});
		checkBoxRemPSW.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(!checkBoxRemPSW.isChecked())
				{
					checkBoxAutoSignIn.setChecked(false);	
					TextPassword.setText("");
				}
				 
						
			}
		});
		sp = this.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
			//记住用户名密码 填写到栏里
			checkBoxRemPSW.setChecked(sp.getBoolean(REM_PSW,false));
			TextUsername.setText(sp.getString(USERNAME,""));
			TextPassword.setText(sp.getString(PSW,""));				 
    }
    
    class SignUpClickListener implements View.OnClickListener
	{	
		//实现监听器类必须实现的方法，该方法将会作为事件处理器
		@Override
		public void onClick(View sdw)
		{
			Intent intent =new Intent();
			System.out.println("Sign up ~~~~~~~~~~~~~~~~~~~");
			
			intent.setClass(SignInActivity.this,SignUpActivity.class);	
			SignInActivity.this.startActivity(intent);
		}		
	}
	class SignInClickListener implements View.OnClickListener
	{	
		//实现监听器类必须实现的方法，该方法将会作为事件处理器
		@Override
		public void onClick(View sdw)
		{
                        
            new SignInTask().execute();
		}		
	}
	
	class SignInTask extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SignInActivity.this);
			pDialog.setMessage("登录中..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String Sname = TextUsername.getText().toString();
			String Spassword = TextPassword.getText().toString();
			//TODO
			JSONObject json=null;
			try {
				json = NetUtil.signIn(Sname, Spassword);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// check log cat fro response
			if(json==null)
			{
				Message msg = msgHandler.obtainMessage();
				msg.what = 0x38;
				msgHandler.sendMessage(msg);
			}
			//Log.d("Create Response", json.toString());
			// check for success tag
			else
			{
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					myApp = (MyApp)getApplication();
					myApp.setUsername(Sname);
					
					Editor editor = sp.edit();
					if(checkBoxRemPSW.isChecked()){
						//记住用户名和密码					
						editor.putBoolean(REM_PSW, true);
						editor.putString(USERNAME, Sname);
						editor.putString(PSW,Spassword);
						if(checkBoxAutoSignIn.isChecked()){
							editor.putBoolean(AUTO_SIGNIN, true);
						}
					}
					else{
						editor.clear();
					}
					editor.commit();
					Intent i = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(i);
					// closing this screen
					finish();
				} else {
					Message msg = msgHandler.obtainMessage();
					msg.what = 0x34;
					msgHandler.sendMessage(msg);
					System.out.println("~~~~~~~~~~~~~~~~");					
					// failed to create product
				}
			} catch (JSONException e) {
				System.out.println("~~~~??~~~~~~~");
				e.printStackTrace();
				
			}
			}

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
                	Toast.makeText(SignInActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                        break;
                case 0x38:
                	Toast.makeText(SignInActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
                        break;
                default:
                        break;
                }
        }
    };
}
