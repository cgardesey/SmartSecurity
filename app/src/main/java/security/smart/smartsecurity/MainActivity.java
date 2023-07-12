package security.smart.smartsecurity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final long ALERT_TV_BLINK_DURATION = 10000;
    private final long ALERT_TV_BLINK_INTERVAL = 200;
    private Map<Alarm, ImageView> alarmIVMap = new LinkedHashMap<>();

    Button allAlarmsBtn;
    Button checkBalanceBtn;
    Button loadCreditBtn;
    TextView balanceTV;
    TextView userTV;
    TextView intruderAlertTV;

    ImageView powerIV;

    RemoteSystemState currentSystemState;

    RemoteOps remoteOps;

    LocalOps localOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context appContext = getApplicationContext();
        remoteOps = new RemoteOps(appContext);
        localOps = new LocalOps(appContext);
        setUpUIHandles();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        refreshSystemState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(IncomingSMSBroadcastReceiver.NewSystemStateEvent event) {
        refreshSystemState();
    }

    private void refreshSystemState() {
        currentSystemState = localOps.getRemoteSystemState();
        updateUI();
        if (!localOps.isSystemSetUp()) {
            showSetUpDialog();
        }
    }

    private void showSetUpDialog() {
        SetupDialogFragment setupDialogFragment = new SetupDialogFragment();
        setupDialogFragment.show(getSupportFragmentManager(), null);
    }

    private void updateUI() {
        updateAlarmStates();
        updateBalanceTV();
        updateUserTV();
        updateIntruderAlertTV();
        if (localOps.hasAlarmState()) {
            showAlarmState();
        }
    }

    private void updateIntruderAlertTV() {
        intruderAlertTV.setTextColor(getResources().getColor(R.color.red));
        intruderAlertTV.setBackgroundResource(R.drawable.white_background);
        if (currentSystemState.isIntruder()) {
            intruderAlertTV.setText("Intrusion Alert!!!");
        } else {
            intruderAlertTV.setText(null);
        }
    }

    private void showAlarmState() {
        playAlarmSound();
        blinkIntruderTV();
        localOps.clearAlarmState();
    }

    private void blinkIntruderTV() {
        new CountDownTimer(ALERT_TV_BLINK_DURATION, ALERT_TV_BLINK_INTERVAL) {
            int timerCounter = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                timerCounter++;

                if (timerCounter % 2 == 0) {
                    intruderAlertTV.setBackgroundResource(R.drawable.red_background);
                    intruderAlertTV.setTextColor(Color.WHITE);
                } else {
                    intruderAlertTV.setBackgroundResource(R.color.smoke_white);
                    intruderAlertTV.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void onFinish() {
                updateIntruderAlertTV();
            }
        }.start();
    }

    private void playAlarmSound() {
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), getAlarmSoundUri());
        r.play();
        r.play();
        r.play();
    }

    private Uri getAlarmSoundUri() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (uri != null) return uri;
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (uri != null) return uri;
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    }

    private void updateUserTV() {
        userTV.setText("System Response to " + String.valueOf(currentSystemState.getUser()));
    }

    private void updateBalanceTV() {
        balanceTV.setText("GHC " + String.valueOf(currentSystemState.getBalance()));
        double lowBalanceLimit = localOps.getLowBalanceWarningLimit();
        if (currentSystemState.getBalance() < lowBalanceLimit) {
            balanceTV.setTextColor(getResources().getColor(R.color.red));
        } else {
            balanceTV.setTextColor(getResources().getColor(R.color.green));
        }
    }

    private void setUpUIHandles() {
        for (Alarm alarm : Alarm.values()) {
            alarmIVMap.put(alarm, (ImageView) findViewById(alarm.getViewId()));
        }
        balanceTV = findViewById(R.id.balanceTV);
        allAlarmsBtn = findViewById(R.id.allAlarms);
        intruderAlertTV = findViewById(R.id.intruderAlertTV);
        userTV = findViewById(R.id.userTV);
        checkBalanceBtn = findViewById(R.id.checkBalanceBtn);
        powerIV = findViewById(R.id.powerIV);
        loadCreditBtn = findViewById(R.id.loadCreditBtn);
        setUpButtons();
    }

    private void setUpButtons() {
        setUpCheckBalanceBtn();
        setUpPowerBtn();
        setUpLoadCreditBtn();
        setUpAllAlarmsBtn();
    }

    private void setUpAllAlarmsBtn() {
        allAlarmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testAllAlarms();
            }
        });

        allAlarmsBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (currentSystemState.areAllAlarmsOn()) {
                    remoteOps.turnAllAlarmsOff();
                    return true;
                } else {
                    remoteOps.turnAllAlarmsOn();
                    return false;
                }

            }
        });
    }

    private void setUpCheckBalanceBtn() {
        checkBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteOps.retrieveRemoteSystemState();
            }
        });
    }

    private void setUpLoadCreditBtn() {
        loadCreditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadCreditDialogFragment loadCreditDialogFragment = new LoadCreditDialogFragment();
                loadCreditDialogFragment.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void setUpPowerBtn() {
        powerIV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (currentSystemState.hasPower()) {
                    remoteOps.turnPowerOff();
                } else {
                    remoteOps.turnPowerOn();
                }
                return true;
            }
        });
    }

    private void showEmptyPhoneNumberError() {
        //todo Show Error on empty phone number
    }

    private void updateAlarmStates() {
        for (Alarm alarm : Alarm.values()) {
            boolean alarmState = currentSystemState.getAlarmStatus().get(alarm.getIndex());
            setAlarmState(alarm, alarmState);
        }
    }

    private void setAlarmState(Alarm alarm, boolean state) {
        ImageView alarmIV = alarmIVMap.get(alarm);
        if (state) {
            alarmIV.setImageResource(R.drawable.alarm_on);
        } else {
            alarmIV.setImageResource(R.drawable.alarm_off);
        }
    }

    private void setAllAlarmsToOff() {
        Alarm[] alarms = Alarm.values();
        for (Alarm alarm : alarms) {
            setAlarmState(alarm, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_setting) {
            launchSettingScreen();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void launchSettingScreen() {
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    public void onAlarmIVClick(View view) {
        final int id = view.getId();
        if (id == R.id.alarm1) {
            testAlarm(Alarm.ALARM_1);
        } else if (id == R.id.alarm2) {
            testAlarm(Alarm.ALARM_2);
        } else if (id == R.id.alarm3) {
            testAlarm(Alarm.ALARM_3);
        } else if (id == R.id.alarm4) {
            testAlarm(Alarm.ALARM_4);
        }
    }

    private void testAlarm(Alarm alarm) {
        remoteOps.testAlarm(alarm);
    }

    private void testAllAlarms() {
        for (Alarm alarm : Alarm.values()) {
            testAlarm(alarm);
        }
    }
}
