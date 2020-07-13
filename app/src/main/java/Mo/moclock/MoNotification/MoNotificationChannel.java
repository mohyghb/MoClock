package Mo.moclock.MoNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class MoNotificationChannel {


    /**
     *
     * @param name (user can see this name) name of the notification channel
     * @param description of the notification channel
     * @param context
     * @param channelId unique channel id
     * @param importance of the channel
     */
    public static void createNotificationChannel(String name, String description,
                                                 Context context, String channelId, int importance) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            channel.setSound(null,null);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }


    /**
     *
     * @param name user viewable name
     * @param description of the channel
     * @param context
     * @param channelId unique id of the channel
     * @param importance of the channel
     * @param sound of the notification channel
     */
//    public static void createNotificationChannel(String name, String description,
//                                                 Context context, String channelId, int importance, Uri sound) {
//        NotificationChannel channel = null;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            channel = new NotificationChannel(channelId, name, importance);
//            channel.setDescription(description);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();
//            channel.setSound(sound,audioAttributes);
//            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
//        }
//
//    }

    /**
     * creates a notification channel with default
     * priority or importance
     * @param name
     * @param description
     * @param context
     * @param channelId
     * @return
     */
    public static NotificationChannel createNotificationChannel(String name, String description,
                                                 Context context, String channelId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);
            channel.setDescription(description);
            channel.setImportance(NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
            return channel;
        }
        return null;
    }


    /**
     * cancels the notification with [tag] and [id]
     * @param context
     * @param tag
     * @param id
     */
    public static void cancelNotification(Context context,String tag,int id){
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel(tag,id);
        }
    }

}
