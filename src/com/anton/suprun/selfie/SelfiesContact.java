package com.anton.suprun.selfie;

import android.net.Uri;

public abstract class SelfiesContact {

	public static final String AUTHORITY = "com.anton.suprun.selfie"; // may need revision 
	public static final Uri BASE_URI = Uri
			.parse("content://" + AUTHORITY + "/");

	public static final String TABLE_NAME = "selfietable";
	
	// The URI for this table.
	public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI,
			TABLE_NAME);

	public static final String _ID = "_id";
	public static final String PHOTO_PATH = "photo";
	public static final String THUMB_PATH = "thumb";
	public static final String PHOTO_TITLE = "title";

}
