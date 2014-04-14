package org.snail.robot.lib.app;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelloAndroidActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
//		Log.e(HelloAndroidActivity.class.getName(), "hello, test-robot-lib-app");
		
		Button btn_cnt = (Button) findViewById(R.id.btn_cnt);
		btn_cnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Button bv = (Button)v;
				bv.setText(String.valueOf(Integer.parseInt(bv.getText().toString()) +1));
			}
		});
		
		Button btn_cnt_1 = (Button) findViewById(R.id.btn_cnt_1);
		btn_cnt_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Button bv = (Button)v;
				bv.setText(String.valueOf(Integer.parseInt(bv.getText().toString()) +1));
			}
		});
		
		Button btn_cnt_2 = (Button) findViewById(R.id.btn_cnt_2);
		btn_cnt_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Button bv = (Button)v;
				bv.setText(String.valueOf(Integer.parseInt(bv.getText().toString()) +1));
			}
		});			
		
		Button btn_next = (Button) findViewById(R.id.btn_next);
		
		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				startActivity(new Intent(HelloAndroidActivity.this, NextAndroidActivity.class));
			}
		});
	}


}
