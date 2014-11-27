package com.anton.suprun.selfie;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class ImageAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<Bitmap> mImageIds;
	private int WIDTH = 70;
	private int HEIGHT = 70;
	private int PADDING = 10;

	// Store the list of image IDs
	public ImageAdapter(Context context, List<Bitmap> ids) {
		mContext = context;
		this.mImageIds = ids;
	}

	// Return the number of items in the Adapter
	@Override
	public int getCount() {
		return mImageIds.size();
	}

	// Return the data item at position
	@Override
	public Object getItem(int position) {
		return mImageIds.get(position);
	}

	// Will get called to provide the ID that
	// is passed to OnItemClickListener.onItemClick()
	@Override
	public long getItemId(int position) {
		return position;
	}

	// Return an ImageView for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView = (ImageView) convertView;

		// if convertView's not recycled, initialize some attributes
		if (imageView == null) {
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new ListView.LayoutParams(WIDTH, HEIGHT));
			imageView.setPadding(PADDING, PADDING, PADDING, PADDING);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}

		imageView.setImageBitmap(mImageIds.get(position));
		return imageView;
	}
}
