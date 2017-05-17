package ir.eynakgroup.diet.schedule.jobs;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import ir.eynakgroup.diet.R;
import ir.eynakgroup.diet.activities.MainActivity;

/**
 * Created by Shayan on 5/17/2017.
 */

public class NotificationJob extends Job {

    public static final String TAG = "Notification_Job";

//    private static NotificationJob mNotificationJob;
//
//    private NotificationJob(){}
//
//    public static NotificationJob getInstance(){
//        if(mNotificationJob == null)
//            mNotificationJob = new NotificationJob();
//
//        return mNotificationJob;
//    }

    @NonNull
    @Override
    protected Result onRunJob(Params params) {

        if (params.isPeriodic()) {
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, new Intent(getContext(), MainActivity.class), 0);

            Notification notification = new NotificationCompat.Builder(getContext())
                    .setContentTitle("Notification Job")
                    .setContentText("Periodic job ran")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.avatar_karafs)
                    .setShowWhen(true)
                    .setColor(Color.TRANSPARENT)
                    .setLocalOnly(true)
                    .build();

            NotificationManagerCompat.from(getContext()).notify(new Random().nextInt(), notification);
        }

        return Result.SUCCESS;
    }


    public static void scheduleJob(){
        new JobRequest.Builder(NotificationJob.TAG)
                .setPeriodic(JobRequest.MIN_INTERVAL, JobRequest.MIN_FLEX)
                .setPersisted(true)
                .build()
                .schedule();
    }

}
