package com.anton.suprun.selfie;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

public class DailySelfieActivity extends ListActivity implements
		LoaderCallbacks<Cursor> {

	private AlarmManager mAlarmManager;
	private Intent mNotificationReceiverIntent;
	private Context mContext;
	private Uri mfileUri = null;
	private PendingIntent mNotificationReceiverPendingIntent;
	private static final long INITIAL_ALARM_DELAY = 30 * 1000L;

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
		setListAdapter(mCursorAdapter);

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
		} else {
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
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File

			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				mfileUri = Uri.fromFile(photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mfileUri);
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

				setResult(RESULT_OK, takePictureIntent);
			}

			else {
				mfileUri = null;
			}
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);

		// Save a file: path for use with ACTION_VIEW intents
		// mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Uri photoUri = null;
			if (null != data) {
				Bundle extras = data.getExtras();
				// Bitmap thumbBitmap = (Bitmap) extras.get("data");
				photoUri = (Uri) extras.get(MediaStore.EXTRA_OUTPUT);
			} else {
				photoUri = mfileUri;
			}
			SelfieRecord record = new SelfieRecord(photoUri);
			// mImageView.setImageBitmap(imageBitmap);
			mCursorAdapter.add(record);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getApplicationContext(), SelfiesContact.CONTENT_URI, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor newCursor) {
		mCursorAdapter.swapCursor(newCursor);

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCursorAdapter.swapCursor(null);


	}
}
