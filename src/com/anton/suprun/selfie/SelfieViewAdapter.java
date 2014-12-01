package com.anton.suprun.selfie;

import java.util.ArrayList;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieViewAdapter extends CursorAdapter {

	private Context mContext;
	private ArrayList<SelfieRecord> mSelfieRecords = new ArrayList<SelfieRecord>();
	private static LayoutInflater sLayoutInflater = null;

	public SelfieViewAdapter(Context context, Cursor cursor, int flags) {
		super(context, cursor, flags);

		mContext = context;
		sLayoutInflater = LayoutInflater.from(mContext);

	}

	@Override
	public Cursor swapCursor(Cursor newCursor) {

		// get SelfieRecords from Cursor.
		if (null != newCursor && newCursor.moveToFirst() != false) {
			mSelfieRecords.clear();

			do {
				mSelfieRecords.add(getSelfieRecordFromCursor(newCursor));
			} while (newCursor.moveToNext());

		}
		return super.swapCursor(newCursor);

	}

	// Return a new SelfieRecord for the data at the cursor's
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
}
