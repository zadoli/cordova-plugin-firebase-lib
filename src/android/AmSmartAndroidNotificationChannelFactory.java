package biz.amsmart.cordova.plugin.android.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.content.ContentResolver;

/**
 * @author steeve
 * 
 * Extenstion for AmSmart Android Notification initializing Default and Alarm Notification Channels
 */
public class AmSmartAndroidNotificationChannelFactory {

    public static String CHANNEL_ID_DEFAULT = "default";
    public static String CHANNEL_ID_ALARM = "alarm";
    private static AmSmartAndroidNotificationChannelFactory factory;

    private NotificationManager notificationManager;
	private String packageName;
    

    public static AmSmartAndroidNotificationChannelFactory create(final NotificationManager notificationManager, final String packageName){
        if(null == factory){
            factory = new AmSmartAndroidNotificationChannelFactory(notificationManager, packageName);
        }
        return factory;
    }

    private AmSmartAndroidNotificationChannelFactory(final NotificationManager notificationManager, final String packageName){
        this.notificationManager = notificationManager;
		this.packageName = packageName;
    }

    public AmSmartAndroidNotificationChannelFactory initializeDefaultChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri defaultNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            final AudioAttributes audioAttributesForDefault = new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

            // TODO: use translations for name and description
            final NotificationChannel alarmNotificationChannel = create(
                CHANNEL_ID_DEFAULT, 
                "Notification",
                NotificationManager.IMPORTANCE_DEFAULT, 
                "Used for informal and warning push notifications",
                defaultNotificationSound,
                audioAttributesForDefault);

            notificationManager.createNotificationChannel(alarmNotificationChannel);
        }

        return factory;
    }

    public AmSmartAndroidNotificationChannelFactory initializeAlarmChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final Uri notificationSoundAlarm = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                            + packageName + "/raw/alarm");
            
            final AudioAttributes audioAttributesAlarm = new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_ALARM)
                            .build();
            
            // TODO: use translations for name and description
            final NotificationChannel alarmNotificationChannel = create(
                    CHANNEL_ID_ALARM, 
                    "Alarm",
                    NotificationManager.IMPORTANCE_HIGH, 
                    "Used for alarm severity push notifications",
                    notificationSoundAlarm,
                    audioAttributesAlarm);

            notificationManager.createNotificationChannel(alarmNotificationChannel);
        }

        return factory;
    }

    private NotificationChannel create(
        final String channelId,
        final String name, 
        final int importance, 
        final String description, 
        final Uri soundUri, 
        final AudioAttributes audioAttributes){

        final NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
        notificationChannel.setDescription(description);
        notificationChannel.setSound(soundUri, audioAttributes);

        return notificationChannel;
    }
}