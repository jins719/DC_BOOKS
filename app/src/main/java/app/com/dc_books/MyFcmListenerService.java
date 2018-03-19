package app.com.dc_books;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by JINS on 10/19/2016.
 */
public class MyFcmListenerService extends FirebaseMessagingService {

    String user_id,reg_id,User_Name;

    Bitmap mBitmap;


    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();




        System.out.printf("yyyyyyyyyyy"+from);
        Map data = message.getData();
        System.out.printf("yyyyyyyyyyy"+data.toString());


        SharedPreferences s = getSharedPreferences(LoginActivity.mp, 0);

        Log.d(TAG, "From: " + message.getFrom());

        // Check if message contains a data payload.
        if (message.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + message.getData());
        }

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + message.getNotification().getBody());
        }
    }



    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }


}