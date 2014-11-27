package com.anton.suprun.selfie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {

	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private Context mContext;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 2 * 60 * 1000L;
	
	private int[] mImages = { R.drawable.wolf_01, R.drawable.wolf_02,
			R.drawable.wolf_03, R.drawable.wolf_04, R.drawable.wolf_05,
			R.drawable.wolf_06, R.drawable.wolf_07, R.drawable.wolf_08,
			R.drawable.wolf_09, R.drawable.wolf_10, R.drawable.wolf_11,
			R.drawable.wolf_12 };

	private ArrayList<Bitmap> mThumbs = new ArrayList();

	ListView listView;
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = getApplicationContext();
		
		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(mContext,
				AlarmNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				mContext, 0, mNotificationReceiverIntent, 0);
		
		// Set inexact repeating alarm
		mAlarmManager.setInexactRepeating(
				AlarmManager.ELAPSED_REALTIME,
				SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
				60000, //AlarmManager.INTERVAL_FIFTEEN_MINUTES,
				mNotificationReceiverPendingIntent);
		
		Button btn = (Button) findViewById(R.id.btn_add_image);

		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int length = mThumbs.size();

				Bitmap img = Bitmap.createScaledBitmap(BitmapFactory
						.decodeResource(getResources(), mImages[length]), 250,
						250, true);
				/*AssetManager asm = getResources().getAssets();
				File file = new File(asm.toString() + "thumb_" + length);
				try {
					file.createNewFile();
					FileOutputStream ostream = new FileOutputStream(file);
					img.compress(CompressFormat.PNG, 100, ostream);
					ostream.close(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
				dispatchTakePictureIntent();

			}
		});

		listView = (ListView) findViewById(R.id.listview);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void dispatchTakePictureIntent() {
	    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        Bitmap imageBitmap = (Bitmap) extras.get("data");
	        //mImageView.setImageBitmap(imageBitmap);
	        addImageToList(imageBitmap);
	    }
	}
	
	protected void addImageToList(Bitmap bmp) {
		mThumbs.add(bmp);
		listView.setAdapter(new ImageAdapter(getApplicationContext(), mThumbs));
		listView.invalidate();
	}
}
