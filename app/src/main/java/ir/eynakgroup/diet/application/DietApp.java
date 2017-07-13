package ir.eynakgroup.diet.application;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import ir.eynakgroup.diet.schedule.ScheduledJobCreator;

/**
 * Created by Shayan on 4/29/2017.
 */

public class DietApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new ScheduledJobCreator());
    }
}
