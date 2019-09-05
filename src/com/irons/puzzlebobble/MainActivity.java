package com.irons.puzzlebobble;

import com.example.try_puzzlebobble.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        CommonUtil.screenWidth = dm.widthPixels;
        CommonUtil.screenHeight = dm.heightPixels;
        
        BitmapUtil.initBitmap(this);
        
        AudioUtil.init(this);
		
		setContentView(new GameView(this));
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
