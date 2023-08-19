//package com.example.btlcuoiki.service;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.os.Build;
//import android.widget.RemoteViews;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.NotificationCompat;
//
//import com.example.btlcuoiki.BuildConfig;
//import com.example.btlcuoiki.R;
//import com.example.btlcuoiki.activity.MainActivity;
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class FirebaseMessageService extends FirebaseMessagingService {
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//        if (message.getNotification()!=null){
//            showNotifition(message.getNotification().getTitle(), message.getNotification().getBody());
//        }
//        super.onMessageReceived(message);
//    }
//
//    private void showNotifition(String title, String body) {
//        Intent intent= new Intent(this, MainActivity.class);
//        String channelId = "noti";
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "noti")
//                .setSmallIcon(R.drawable.baseline_phone_24)
//                        .setAutoCancel(true)
//                                .setVibrate(new long[]{1000, 1000, 1000, 1000})
//                                        .setOnlyAlertOnce(true)
//                                                .setContentIntent(pendingIntent);
//
//        builder = builder.setContent(customView(title, body));
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel = new NotificationChannel(channelId, "wep_app", NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(notificationChannel);
//
//        }
//        notificationManager.notify(0, builder.build());
//    }
//
//    private RemoteViews customView(String title, String body){
//        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notificant);
//        remoteViews.setTextViewText(R.id.title_noti, title);
//        remoteViews.setTextViewText(R.id.body_noti, body);
//        remoteViews.setImageViewResource(R.id.imgnoti, R.drawable.mobile );
//        return remoteViews;
//    }
//}
