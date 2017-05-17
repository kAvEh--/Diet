package ir.eynakgroup.diet.schedule;

import android.content.Context;
import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;
import com.evernote.android.job.JobManager;

import java.lang.ref.WeakReference;

import ir.eynakgroup.diet.schedule.jobs.NotificationJob;

/**
 * Created by Shayan on 5/17/2017.
 */

public class ScheduleJobCreator implements JobCreator {
    @Override
    public Job create(String tag) {
        switch (tag){
            case NotificationJob.TAG:
                return new WeakReference<>(new NotificationJob()).get();

            default:
                return null;
        }
    }

    private static void cancelJob(int jobId) {
        JobManager.instance().cancel(jobId);
    }

}
