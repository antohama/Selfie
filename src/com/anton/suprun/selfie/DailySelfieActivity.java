package com.anton.suprun.selfie;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import course.labs.contentproviderlab.PlaceViewAdapter;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class DailySelfieActivity extends ListActivity implements
LoaderCallbacks<Cursor> {

	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private Context mContext;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 30 * 1000L;

	private ArrayList<Bitmap> mThumbs = new ArrayList();

	CheckBox checkbox;
	
	private SelfieViewAdapter mCursorAdapter;

	static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final String TAG = "Selfie app";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(getApplicationContext(),
					"External Storage is not available.", Toast.LENGTH_LONG)
					.show();
			finish();
		}
		setContentView(R.layout.activity_main);

		checkbox = (CheckBox) findViewById(R.id.checkbox_alarm);
		mContext = getApplicationContext();

		// Get the AlarmManager Service
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Create an Intent to broadcast to the AlarmNotificationReceiver
		mNotificationReceiverIntent = new Intent(DailySelfieActivity.this,
				AlarmNotificationReceiver.class);

		// Create an PendingIntent that holds the NotificationReceiverIntent
		mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
				DailySelfieActivity.this, 0, mNotificationReceiverIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Create and set empty PlaceViewAdapter
		mCursorAdapter = new SelfieViewAdapter(getApplicationContext(), null, 0);

		// Initialize the loader
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "Alarm has been disabled");
		mAlarmManager.cancel(mNotificationReceiverPendingIntent);
	}
	
	@Override
	public void onPause() {
		
		// Set inexact repeating alarm
		if (checkbox.isChecked()) {
			Log.d(TAG, "Alarm has been set");
			mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
					SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
					INITIAL_ALARM_DELAY, mNotificationReceiverPendingIntent); // AlarmManager.INTERVAL_FIFTEEN_MINUTES,
		}
		else {
			Log.d(TAG, "Alarm has been disabled");
			mAlarmManager.cancel(mNotificationReceiverPendingIntent);
		}
		
		super.onPause();
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
		if (id == R.id.take_photo_item) {
			dispatchTakePictureIntent();
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
			// mImageView.setImageBitmap(imageBitmap);
			addImageToList(imageBitmap);
		}
	}

	protected void addImageToList(SelfieRecord selfie) {
		SelfieViewAdapter adapter = new SelfieViewAdapter( mContext, mThumbs);
		mCursorAdapter.add(selfie);
		mThumbs.add(bmp);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}
}