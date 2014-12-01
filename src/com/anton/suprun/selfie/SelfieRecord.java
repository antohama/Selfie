package com.anton.suprun.selfie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class SelfieRecord {

	// URL for retrieving the photo
	private Uri mPhotoUri;

	// path to images in external memory
	private String mPhotoBitmapPath;
	private String mPhotoTitle;
	private Bitmap mPhotoBitmap;
	private Bitmap mThumbBitmap;

	public SelfieRecord(Uri photoUri) {
		mPhotoUri = photoUri;
		mPhotoBitmapPath = photoUri.getPath();
		Bitmap mPhotoBitmap = BitmapFactory.decodeFile(mPhotoBitmapPath);
		Bitmap mThumbBitmap = Bitmap.createScaledBitmap(mPhotoBitmap, 100, 100, false);
		
		mPhotoTitle = photoUri.getLastPathSegment();
	}

	public Bitmap getThumb() {
		return mThumbBitmap;
	}
	
	public Bitmap getPhoto() {
		return mPhotoBitmap;
	}

	public void setPhoto(Bitmap photoBitmap) {
		mPhotoBitmap = photoBitmap;
	}

	public Uri getPhotoUri() {
		return mPhotoUri;
	}

	public void setPhotoUri(Uri photoUri) {
		this.mPhotoUri = photoUri;
	}

	public String getPhotoTitle() {
		return mPhotoTitle;
	}

	public void setPhotoTitle(String photoTitle) {
		this.mPhotoTitle = photoTitle;
	}

	@Override
	public String toString() {
		return mPhotoTitle;

	}

	public String getPhotoBitmapPath() {
		return mPhotoBitmapPath;
	}

	public void setPhotoBitmapPath(String photoBitmapPath) {
		this.mPhotoBitmapPath = photoBitmapPath;
	}
}
