package com.example.robertschat.notification;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class BackgroundNotificationScheduler {

    public static void scheduleNotification(Context context, String title, String message, long delaySeconds) {
        @SuppressLint("RestrictedApi") Data data = new Data.Builder()
                .put("title", title)
                .put("message", message)
                .build();

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delaySeconds, TimeUnit.SECONDS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest);
    }
}
