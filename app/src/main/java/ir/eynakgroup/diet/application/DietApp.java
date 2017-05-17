package ir.eynakgroup.diet.application;

import android.app.Application;
import android.app.job.JobScheduler;

import com.evernote.android.job.JobManager;

import ir.eynakgroup.diet.schedule.ScheduleJobCreator;

/**
 * Created by Shayan on 4/29/2017.
 */

public class DietApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new ScheduleJobCreator());

    }
}
