package com.anton.suprun.selfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class FullPhotoActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		// Get the Intent used to start this Activity
		Intent intent = getIntent();
		
		// Make a new ImageView
		ImageView imageView = new ImageView(getApplicationContext());
		
		// Get the path of the image to display and set it as the image for this ImageView
		imageView.setBackground(Drawable.createFromPath(intent.getStringExtra(DailySelfieActivity.EXTRA_RES_ID)));
		
		setContentView(imageView);
	}
	
}
