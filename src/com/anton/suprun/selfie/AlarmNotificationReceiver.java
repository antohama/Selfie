package com.anton.suprun.selfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmNotificationReceiver extends BroadcastReceiver {
	// Notification ID to allow for future updates
	private static final int MY_NOTIFICATION_ID = 1;
	private static final String TAG = "AlarmNotificationReceiver";

	// Notification Text Elements
	private final CharSequence tickerText = "It's time to selfie";

	// Notification Action Elements
	private Intent mIntent;
	private PendingIntent mNotificationIntent;

	/*
	 * // Notification Sound and Vibration on Arrival private final Uri soundURI
	 * = Uri .parse("android.resource://course.examples.Alarms.AlarmCreate/" +
	 * R.raw.alarm_rooster); private final long[] mVibratePattern = { 0, 200,
	 * 200, 300 };
	 */

	@Override
	public void onReceive(Context context, Intent intent) {

		// The Intent to be used when the user clicks on the Notification View
		mIntent = new Intent(context, DailySelfieActivity.class);

		// The PendingIntent that wraps the underlying Intent
		mNotificationIntent = PendingIntent.getActivity(context, 0, mIntent,
				Intent.FLAG_ACTIVITY_NEW_TASK);

		// Build the Notification
		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(tickerText).setSmallIcon(R.drawable.icon)
				.setAutoCancel(true).setContentTitle(tickerText)
				.setContentIntent(mNotificationIntent);
		// .setSound(soundURI).setVibrate(mVibratePattern);*/

		// Get the NotificationManager
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		// Pass the Notification to the NotificationManager:
		mNotificationManager.notify(MY_NOTIFICATION_ID,
				notificationBuilder.build());
		Log.d(TAG, "Notification sent");

	}
}
