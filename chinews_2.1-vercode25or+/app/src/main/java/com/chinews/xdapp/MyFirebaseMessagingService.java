package com.chinews.xdapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;


/**
 * NOTE: There can only be one service in each app that receives FCM messages. If multiple
 * are declared in the Manifest then the first one will be chosen.
 * <p>
 * In order to make this Java sample functional, you must remove the following from the Kotlin messaging
 * service in the AndroidManifest.xml:
 * <p>
 * <intent-filter>
 * <action android:name="com.google.firebase.MESSAGING_EVENT" />
 * </intent-filter>
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        //Map<String, String> data = remoteMessage.getData();
        String title = Objects.requireNonNull(remoteMessage.getNotification()).getTitle();
        String message = remoteMessage.getNotification().getBody();
        Uri imageUrl = remoteMessage.getNotification().getImageUrl();
        String channel = remoteMessage.getNotification().getChannelId();
        Log.i(TAG, "onMessageReceived: title : "+title);
        Log.i(TAG, "onMessageReceived: message : "+message);
        Log.i(TAG, "onMessageReceived: imageUrl : "+imageUrl);
        Log.i(TAG,"onMessageReceived: channel : "+channel);
        if (imageUrl == null) {
            sendNotification(title,message,channel);
        } else {
            sendNotification(title,message,imageUrl,channel);
        }
    }
    private void sendNotification(String title, String message, String channel) {
        Intent resultIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColor(Color.argb(255,125,240,210));
        //设置点击通知之后的响应，启动SettingActivity类
        //通过 builder.build() 拿到 notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, mBuilder.build());
        Log.d(TAG,"done");
    }
    private void sendNotification(String title, String message, Uri image, String channel) {
        Bitmap bitmap = null;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(image.toString()).openConnection();
            httpURLConnection.connect();
            bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), channel)
                .setContentTitle(title)
                .setContentText(message)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setColor(Color.argb(255,125,240,210))
                .setLargeIcon(bitmap);
        //设置点击通知之后的响应，启动SettingActivity类
        //通过 builder.build() 拿到 notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(1, mBuilder.build());
        Log.d(TAG,"done");
    }

    // [END receive_message]


    // [START on_new_token]

    /**
     * There are two scenarios when onNewToken is called:
     * 1) When a new token is generated on initial app startup
     * 2) Whenever an existing token is changed
     * Under #2, there are three scenarios when the existing token is changed:
     * A) App is restored to a new device
     * B) User uninstalls/reinstalls the app
     * C) User clears app data
     */
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);
    }
    // [END on_new_token]


    ///**
    // * Persist token to third-party servers.
    // * <p>
    // * Modify this method to associate the user's FCM registration token with any
    // * server-side account maintained by your application.
    // *
    // * @param token The new token.
    // */
    //private void sendRegistrationToServer(String token) {
    //    // TODO: Implement this method to send token to your app server.
//
    //}

}