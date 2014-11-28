package com.anton.suprun.selfie;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
	private int WIDTH = 120;
	private int HEIGHT = 120;
	private int PADDING = 10;

	private static final String APP_DIR = "DailySelfie/Photos";
	private ArrayList<SelfieRecord> mSelfieRecords = new ArrayList<SelfieRecord>();
	private static LayoutInflater sLayoutInflater = null;
	private String mBitmapStoragePath;

	// Store the list of image IDs
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

	// Returns a new SelfieRecord for the data at the cursor's
	// current position
	private SelfieRecord getSelfieRecordFromCursor(Cursor cursor) {

		String photoBitmapPath = cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_PATH));
		String photoTitle = cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_TITLE));

		return new SelfieRecord(photoBitmapPath, photoTitle);

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

	// TODO !!!!
	public void add(SelfieRecord listItem) {

		String lastPathSegment = Uri.parse(listItem.getPhotoUrl())
				.getLastPathSegment();
		String filePath = mBitmapStoragePath + "/" + lastPathSegment;

		if (storeBitmapToFile(listItem.getPhoto(), filePath)) {

			listItem.setPhotoBitmapPath(filePath);
			mSelfieRecords.add(listItem);

			ContentValues values = new ContentValues();

			// TODO - Insert new record into the ContentProvider

			ContentResolver resolver = mContext.getContentResolver();
			// values.r

		}

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

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.photo.setImageBitmap(getBitmapFromFile(cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_PATH))));
		holder.title.setText(cursor.getString(cursor
				.getColumnIndex(SelfiesContact.PHOTO_TITLE)));

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
