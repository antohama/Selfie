package com.anton.suprun.selfie;

import android.graphics.Bitmap;

public class SelfieRecord {

	// URL for retrieving the photo
		private String mPhotoUrl;

		// path to flag image in external memory
		private String mPhotoBitmapPath;

		private Bitmap mPhotoBitmap;
		private Bitmap mThumbBitmap;
		private String mPhotoTitle;

		public SelfieRecord(String photoBitmapPath,
				String photoTitle) {
			//mPhotoUrl = photoUrl;
			mPhotoBitmapPath = photoBitmapPath;
			mPhotoTitle = photoTitle;
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

		public String getPhotoUrl() {
			return mPhotoUrl;
		}

		public void setPhotoUrl(String photoUrl) {
			this.mPhotoUrl = photoUrl;
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
