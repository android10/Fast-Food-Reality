package org.android10.fastfoodfinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author Penano
 * This activity is for Splash Screen Purpouse
 */
public class SplashActivity extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 2500; //time to display the splash screen in ms
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setFullScreen();
		setContentView(R.layout.splash);  
				
		showSplashScreen();	
	}

	//Called when the user press the screen 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}
	
	//----------------------------------------------------------------------
	//H E L P E R    M E T H O D S
	//----------------------------------------------------------------------
	private void setFullScreen() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	private void showSplashScreen(){
		//Thread for displaying the SplashScreen
		Thread splashThread = new Thread(){			
			@Override
			public void run() {
				try {		
					int waited=0;
					while(_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				}
				catch(InterruptedException e) {
					//do nothing
				}
				finally {
					uiCallback.sendEmptyMessage(0);
				}
			}
		};
		splashThread.start();
	}
	
	//ui thread callback handler
	private Handler uiCallback = new Handler()
	{
		@Override
		public void handleMessage(android.os.Message msg) 
		{
			finish();
			startActivity(new Intent("org.android10.intent.action.LAUNCH_MAIN"));
		}
	};
}
