package security.smart.smartsecurity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private Map<Alarm, ImageView> alarmIVMap = new LinkedHashMap<>();

    Button allAlarmsBtn;
    Button checkBalanceBtn;
    Button loadCreditBtn;
    TextView balanceTV;
    TextView userTV;
    TextView intruderAlertTV;

    ImageView powerIV;

    RemoteSystemState currentSystemState = new RemoteSystemState();

    RemoteMessageOps remoteMessageOps;

    SystemStateOps systemStateOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context appContext = getApplicationContext();
        remoteMessageOps = new RemoteMessageOps(appContext);
        systemStateOps = new SystemStateOps(appContext);
        setUpUIHandles();
    }

    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

        if (!systemStateOps.isSystemSetUp()) {
            showSetUpDialog();
        } else {
            updateUI();
            setSystemState();
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
    }

    private void updateIntruderAlertTV() {
        if (currentSystemState.isIntruder()) {
            intruderAlertTV.setText("Intrusion Alert!!!");
            intruderAlertTV.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void playIntruderSound() {
//        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        if (alert == null) {
//            // alert is null, using backup
//            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//            // I can't see this ever being null (as always have a default notification)
//            // but just incase
//            if (alert == null) {
//                // alert backup is null, using 2nd backup
//                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            }
//        }
//        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alert);
//        r.play();
//        r.play();
//        r.play();
//        new CountDownTimer(10000, 200) {
//            int timerCounter = 0;
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                // empty
//
//                if (timerCounter % 2 == 0) {
//                    powerTV.setBackgroundResource(R.drawable.red_background);
//                } else {
//                    powerTV.setBackgroundResource(R.color.default_color);
//                }
//            }
//
//            @Override
//            public void onFinish() {
//                powerTV.setBackgroundResource(R.drawable.white_background);
//
//                powerTV.setText("");
//                SystemStateOps.setAlarmState(MainActivity.this, false);
//            }
//        }.start();
    }

    private void updateUserTV() {
        userTV.setText("System Response to " + String.valueOf(currentSystemState.getUser()));
    }

    private void updateBalanceTV() {
        balanceTV.setText("GHC " + String.valueOf(currentSystemState.getBalance()));
        double lowBalanceLimit = systemStateOps.getLowBalanceWarningLimit();
        if (currentSystemState.getBalance() < lowBalanceLimit) {
            balanceTV.setTextColor(getResources().getColor(R.color.red));
        } else {
            balanceTV.setTextColor(getResources().getColor(R.color.green));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(IncomingSMSBroadcastReceiver.NewSystemStateEvent event) {
        setSystemState();
    }

    private void setSystemState() {
        if (systemStateOps.hasRemoteSystemState()) {
            currentSystemState = systemStateOps.getRemoteSystemState();
            updateUI();
        }
    }

    private void setUpUIHandles() {
        alarmIVMap.put(Alarm.ALARM_1, (ImageView) findViewById(R.id.alarm1));
        alarmIVMap.put(Alarm.ALARM_2, (ImageView) findViewById(R.id.alarm2));
        alarmIVMap.put(Alarm.ALARM_3, (ImageView) findViewById(R.id.alarm3));
        alarmIVMap.put(Alarm.ALARM_4, (ImageView) findViewById(R.id.alarm4));
        balanceTV = findViewById(R.id.balanceTV);
        allAlarmsBtn = findViewById(R.id.allAlarms);
        intruderAlertTV = findViewById(R.id.intruderAlertTV);
        userTV = findViewById(R.id.userTV);
        checkBalanceBtn = findViewById(R.id.checkBalanceBtn);
        powerIV = findViewById(R.id.powerIV);
        loadCreditBtn = findViewById(R.id.loadCreditBtn);
        setUpBtns();
    }

    private void setUpBtns() {
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
                    remoteMessageOps.turnAllAlarmsOff();
                    return true;
                } else {
                    remoteMessageOps.turnAllAlarmsOn();
                    return false;
                }

            }
        });
    }

    private void setUpCheckBalanceBtn() {
        checkBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteMessageOps.retrieveRemoteSystemState();
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
                    remoteMessageOps.turnPowerOff();
                } else {
                    remoteMessageOps.turnPowerOn();
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
        remoteMessageOps.testAlarm(alarm);
    }

    private void testAllAlarms() {
        for (Alarm alarm : Alarm.values()) {
            testAlarm(alarm);
        }
    }
}
