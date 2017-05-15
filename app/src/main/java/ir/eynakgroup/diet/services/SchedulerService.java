package ir.eynakgroup.diet.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import ir.eynakgroup.diet.receivers.AlarmReceiver;

/**
 * Created by Shayan on 5/10/2017.
 */

public class SchedulerService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public static String START_MILLIS = "start_millis";
    public static String INTERVAL_MILLIS = "interval_millis";
    public static String END_MILLIS = "end_millis";

    public static String START_ALARM = "start";
    public static String CANCEL_ALARM = "cancel";

    public SchedulerService() {
        super("SchedulerService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent.getAction().equals(START_ALARM)){
            long startMillis = intent.getExtras().getLong(START_MILLIS, System.currentTimeMillis());
            long intervalMillis = intent.getExtras().getLong(INTERVAL_MILLIS);
            long endMillis = intent.getExtras().getLong(END_MILLIS);
            scheduleAlarm(startMillis, intervalMillis, endMillis);
        }
        else if(intent.getAction().equals(CANCEL_ALARM))
            cancelAlarm();

    }

    public void scheduleAlarm(long startMillis, long intervalMillis, long endMillis) {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.setAction(AlarmReceiver.ACTION);
        intent.putExtra(END_MILLIS, endMillis);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC , startMillis,
                intervalMillis, pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }
}


