package com.hm.tools.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

	View rootView;
	ScreenLogcat mScreenLogcat = new ScreenLogcat();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater mInflater = LayoutInflater.from(this);
		rootView = mScreenLogcat.addLogcatToScreen(this, rootView , "logcat -s A");
		setContentView(rootView);
		new Timer().schedule(new TimerTask(){
			int i;
			@Override
			public void run() {
				i++;
				Log.d("A",""+i+"  Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!"
						+"Hello World! Hello World! Hello World! Hello World! Hello World! Hello World!\n");
			}
			
		}, 0, 1000);
	}
			
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mScreenLogcat.destroyLogcatToScreen();
	}

}
