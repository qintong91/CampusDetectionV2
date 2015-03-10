package com.example.campusdetectionv2;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFragment extends Fragment{
	Button buttonExit;
	SharedPreferences sp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, null);
		buttonExit = (Button)view.findViewById(R.id.buttonExit);
		buttonExit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				 new AlertDialog.Builder(getActivity())
				          .setTitle("提示")
			           .setMessage("确定退出?")
			          .setIcon(android.R.drawable.ic_dialog_info)
			          .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				          public void onClick(DialogInterface dialog, int whichButton) {
				        	  sp = getActivity().getSharedPreferences(SignInActivity.USER_INFO, Context.MODE_PRIVATE);
				        	  Editor editor =sp.edit();
				        	  editor.putBoolean(SignInActivity.AUTO_SIGNIN, false);
				        	  editor.commit();
				        	  Intent intent = new Intent(getActivity(), SignInActivity.class);
				        	  getActivity().startActivity(intent);
				        	  getActivity().finish();
				          }
				           })
			           .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			          public void onClick(DialogInterface dialog, int whichButton) {
				           //取消按钮事件
				            }
				          })
				           .show();


			}
		});
		return view;
	}
}
