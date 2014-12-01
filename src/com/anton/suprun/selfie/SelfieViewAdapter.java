package com.anton.suprun.selfie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieViewAdapter extends CursorAdapter {

	private Context mContext;
	private static final String APP_DIR = "DailySelfie/Photos";
	private ArrayList<SelfieRecord> mSelfieRecords = new ArrayList<SelfieRecord>();
	private static LayoutInflater sLayoutInflater = null;
	private String mBitmapStoragePath;

	public SelfieViewAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);

		mContext = context;
		sLayoutInflater = LayoutInflater.from(mContext);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				String root = mContext.getExternalFilesDir(null)
						.getCanonicalPath();
				if (null != root) {
					File bitmapStorageDir = new File(root, APP_DIR);
					bitmapStorageDir.mkdirs();
					mBitmapStoragePath = bitmapStorageDir.getCanonicalPath();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {

		// clear the ArrayList list so it contains
		// the current set of PlaceRecords.

		if (null != newCursor && newCursor.moveToFirst() != false) {
			mSelfieRecords.clear();

			do {
				mSelfieRecords.add(getSelfieRecordFromCursor(newCursor));
			} while (newCursor.moveToNext());

		}
		return super.swapCursor(newCursor);

	}

	// Returns a new SelfieRecord for the data at the cursor's
	// current position
	private SelfieRecord getSelfieRecordFromCursor(Cursor cursor) {

		Uri photoBitmapUri = Uri.parse(cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_URI)));

		return new SelfieRecord(photoBitmapUri);

	}

	// Return the number of items in the Adapter
	@Override
	public int getCount() {
		return mSelfieRecords.size();
	}

	// Return the data item at position
	@Override
	public Object getItem(int position) {
		return mSelfieRecords.get(position);
	}

	// Will get called to provide the ID that
	// is passed to OnItemClickListener.onItemClick()
	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		ImageView photo;
		TextView title;
	}

	public void add(SelfieRecord listItem) {

		// String lastPathSegment = Uri.parse(listItem.getPhotoUrl())
		// .getLastPathSegment();

		// Create an image file name
		String imageFilePath = listItem.getPhotoBitmapPath();

		/*
		 * if (storeBitmapToFile(listItem.getPhoto(), filePath)) {
		 * 
		 * listItem.setPhotoBitmapPath(filePath);
		 */
		mSelfieRecords.add(listItem);

		ContentValues values = new ContentValues();

		// Insert new record into the ContentProvider
		ContentResolver resolver = mContext.getContentResolver();

		// values.put(PlaceBadgesContract._ID, mPlaceRecords.size());
		values.put(SelfiesContact.PHOTO_PATH, listItem.getPhotoBitmapPath());
		values.put(SelfiesContact.PHOTO_TITLE, listItem.getPhotoTitle());
		values.put(SelfiesContact.PHOTO_URI, listItem.getPhotoUri().toString());

		resolver.insert(SelfiesContact.CONTENT_URI, values);

		values.clear();

	}

	// Return an ImageView for each item referenced by the Adapter
	/*
	 * @Override public View getView(int position, View convertView, ViewGroup
	 * parent) {
	 * 
	 * ImageView imageView = (ImageView) convertView;
	 * 
	 * // if convertView's not recycled, initialize some attributes if
	 * (imageView == null) { imageView = new ImageView(mContext);
	 * imageView.setLayoutParams(new ListView.LayoutParams(WIDTH, HEIGHT));
	 * imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
	 * imageView.setScaleType(ImageView.ScaleType.CENTER_CROP); }
	 * 
	 * imageView.setImageBitmap(mSelfieRecords.get(position)); return imageView;
	 * }
	 */

	public ArrayList<SelfieRecord> getList() {
		return mSelfieRecords;
	}

	public void removeAllViews() {
		mSelfieRecords.clear();

		// delete all records in the ContentProvider
		mContext.getContentResolver().delete(SelfiesContact.CONTENT_URI, null,
				null);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.photo.setImageBitmap(Bitmap.createScaledBitmap(BitmapFactory
				.decodeFile(cursor.getString(cursor
						.getColumnIndex(SelfiesContact.PHOTO_PATH))), 100, 100,
				false));

		holder.title.setText(cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_TITLE)));

	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View newView;
		ViewHolder holder = new ViewHolder();

		newView = sLayoutInflater.inflate(R.layout.single_row, parent, false);
		holder.photo = (ImageView) newView.findViewById(R.id.itemPhoto);
		holder.title = (TextView) newView.findViewById(R.id.itemText);

		newView.setTag(holder);

		return newView;
	}

	private Bitmap getBitmapFromFile(String filePath) {
		return BitmapFactory.decodeFile(filePath);
	}

	private boolean storeBitmapToFile(Bitmap bitmap, String filePath) {

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			try {

				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(filePath));
				bitmap.compress(CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
			} catch (FileNotFoundException e) {
				return false;
			} catch (IOException e) {
				return false;
			}
			return true;
		}
		return false;
	}
}
