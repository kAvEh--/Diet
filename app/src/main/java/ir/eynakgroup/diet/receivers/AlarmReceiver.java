package ir.eynakgroup.diet.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ir.eynakgroup.diet.services.SchedulerService;

/**
 * Created by Shayan on 5/10/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "ir.eynakgroup.diet.receivers.alarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getExtras().containsKey(SchedulerService.END_MILLIS)){
            long endMillis = intent.getExtras().getLong(SchedulerService.END_MILLIS);
            if(endMillis < System.currentTimeMillis())
                cancelAlarm(context);
        }




    }

    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}
