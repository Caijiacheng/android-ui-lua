package org.snail.robot.lib.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NextAndroidActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_next);
		
		Button btn_cnt = (Button) findViewById(R.id.btn_cnt);
		btn_cnt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Button bv = (Button)v;
				bv.setText(String.valueOf(Integer.parseInt(bv.getText().toString()) +1));
			}
		});
		
	}
}
