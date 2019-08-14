package com.intent.moviecatalogue.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import com.intent.moviecatalogue.Public;
import com.intent.moviecatalogue.R;
import com.intent.moviecatalogue.notification.Receiver;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.intent.moviecatalogue.Public.CHANGE_DAILY_REMINDER;
import static com.intent.moviecatalogue.Public.CHANGE_RELEASE_REMINDER;
import static com.intent.moviecatalogue.Public.DAILY_REMINDER_EXTRA;
import static com.intent.moviecatalogue.Public.RELEASE_REMINDER_EXTRA;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.dailyReminder)
    CompoundButton dailyReminder;
    @BindView(R.id.releaseReminder)
    CompoundButton releaseReminder;
    SharedPreferences prefDailyReminder, prefReleaseReminder;
    SharedPreferences.Editor changeDailyReminder, changeReleaseReminder;
    private Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        Button btnChangeLanguage = findViewById(R.id.btn_change_language);
        btnChangeLanguage.setOnClickListener(this);
        receiver = new Receiver();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        procPreferences();
        setSwitch();
    }

    private void setSwitch() {
        dailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeDailyReminder = prefDailyReminder.edit();
            if (isChecked) {
                serviceDailyReminder();
            } else {
                noServiceDailyReminder();
            }
        });

        releaseReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            changeReleaseReminder = prefReleaseReminder.edit();
            if (isChecked) {
               serviceReleaseReminder();
            } else {
               noServiceReleaseReminder();
            }
        });
    }

    private void noServiceReleaseReminder() {
        changeReleaseReminder.putBoolean(CHANGE_RELEASE_REMINDER, false);
        changeReleaseReminder.apply();
        receiver.bannedReminder(SettingsActivity.this, Public.NORMAL_RELEASE_REMINDER);
    }

    private void serviceReleaseReminder() {
        String time = "08:00";
        String message = getString(R.string.notify_release);

        changeReleaseReminder.putBoolean(CHANGE_RELEASE_REMINDER, true);
        changeReleaseReminder.apply();
        receiver.serviceReleaseReminder(SettingsActivity.this, Public.NORMAL_RELEASE_REMINDER, time, message);
    }

    private void noServiceDailyReminder() {
        changeDailyReminder.putBoolean(CHANGE_DAILY_REMINDER, false);
        changeDailyReminder.apply();

        receiver.bannedReminder(SettingsActivity.this, Public.NORMAL_DAILY_REMINDER);
    }

    private void serviceDailyReminder() {
        String time = "07:00";
        String message = getString(R.string.app_name) + " " + getString(R.string.notify_daily);

        changeDailyReminder.putBoolean(CHANGE_DAILY_REMINDER, true);
        changeDailyReminder.apply();
        receiver.serviceDailyReminder(SettingsActivity.this, Public.NORMAL_DAILY_REMINDER, time, message);
    }

    void procPreferences() {
        prefDailyReminder = getSharedPreferences(DAILY_REMINDER_EXTRA, MODE_PRIVATE);
        boolean checkSwitchDailyReminder = prefDailyReminder.getBoolean(CHANGE_DAILY_REMINDER, false);
        dailyReminder.setChecked(checkSwitchDailyReminder);

        prefReleaseReminder = getSharedPreferences(RELEASE_REMINDER_EXTRA, MODE_PRIVATE);
        boolean checkSwitchReleaseReminder = prefReleaseReminder.getBoolean(CHANGE_RELEASE_REMINDER, false);
        releaseReminder.setChecked(checkSwitchReleaseReminder);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_change_language) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
