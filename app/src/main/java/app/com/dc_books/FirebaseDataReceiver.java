package app.com.dc_books;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import java.util.Random;

/**
 * Created by User on 11/28/2017.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseDataReceiver";

    public void onReceive(Context context, Intent intent) {

       /* Log.d(TAG, "I'm in!!!");
        if (intent.getExtras() != null) {
            String message= intent.getExtras().get("gcm.notification.body").toString();
            String title=intent.getExtras().get("gcm.notification.title").toString();
          //  sendNotification(message,title,context);
//            String message= intent.getExtras().get("gcm.notification.body").toString();
//            String message= intent.getExtras().get("gcm.notification.body").toString();
//            String message= intent.getExtras().get("gcm.notification.body").toString();
//            String message= intent.getExtras().get("gcm.notification.body").toString();
//            Log.d("kdjfksaj",message);
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                Log.d("dksjfksajdfaj",value.toString());
                Log.e("FirebaseDataReceiver", "Key: " + key + " Value: " + value);
            }
        }*/

        Bundle bundle = intent.getExtras();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            System.out.println("yyyyyyyyyyyyyyyyyyy"+String.format("%s %s (%s)", key,
                    value.toString(), value.getClass().getName()));
        }


    }
    private void sendNotification(String msg,String title,Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, new Random().nextInt(100) , intent,
                PendingIntent.FLAG_ONE_SHOT);
        long when = System.currentTimeMillis();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(context);
        mNotifyBuilder.setVibrate(new long[] { 1000, 1000,1000,1000,1000,1000});
        boolean lollipop = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        if (lollipop) {

            mNotifyBuilder = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                    .setContentText(msg)
                    .setColor(Color.TRANSPARENT)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setWhen(when).setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

        } else {

            mNotifyBuilder = new NotificationCompat.Builder(context)
                    .setStyle(
                            new NotificationCompat.BigTextStyle()
                                    .bigText(msg))
                    .setContentTitle(title).setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setWhen(when).setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);

        }
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(new Random().nextInt(100) /* ID of notification */, mNotifyBuilder.build());
    }
}
