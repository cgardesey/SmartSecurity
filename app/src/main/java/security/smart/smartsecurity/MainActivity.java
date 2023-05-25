package security.smart.smartsecurity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_SYSTEM_RESPONSE = "EXTRA_SYSTEM_RESPONSE";
    public static final String PREF_BALANCE = "PREF_BALANCE_SMART";

    public static final String ALARM_1 = "1.";
    public static final String ALARM_2 = "2.";
    public static final String ALARM_3 = "3.";
    public static final String ALARM_4 = "4.";
    public static final String ALL_ALARMS = "Test all";
    boolean isSetAllAlarms =true;
    boolean Power = false;
    ImageView alarm1;
    ImageView alarm2;
    ImageView alarm3;
    ImageView alarm4;
    Button allAlarms;
    TextView balanceTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allAlarms = (Button) findViewById(R.id.allAlarms);
        alarm1 = (ImageView) findViewById(R.id.alarm1);
        alarm2 = (ImageView) findViewById(R.id.alarm2);
        alarm3 = (ImageView) findViewById(R.id.alarm3);
        alarm4 = (ImageView) findViewById(R.id.alarm4);
         balanceTV = (TextView) findViewById(R.id.balanceTV);

        setAlarm();
     //   seekbarSetup();
//        ButterKnife.bind(this);

    }


    public void setAlarm() {

        allAlarms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(isSetAllAlarms){
                    sendMessage("all0");
                    return true;
                }
                else{
                    sendMessage("all1");
                    return false;
                }

            }
        });
//        alarm1.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                sendMessage("Set alarm 1");
//                return true;}});
//        alarm2.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                sendMessage("Set alarm 2");
//                return true;}});
//        alarm3.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                sendMessage("Set alarm 3");
//                return true;}});
//
//        alarm4.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                sendMessage("Set alarm 4");
//                return true;}});
    }

//    public void seekbarSetup() {
//
//        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
//
//        SystemResponse response = getIntent().getParcelableExtra(EXTRA_SYSTEM_RESPONSE);
//        if (response != null) {
//            setSystemResponseDisplay(response);
//        }
//
//
//        seekBar.setOnSeekBarChangeListener(
//                new SeekBar.OnSeekBarChangeListener() {
//                    int progress = 0;
//
//                    @Override
//                    public void onProgressChanged(SeekBar seekBar,
//                                                  int progresValue, boolean fromUser) {
//                        progress = progresValue;
//                    }
//
//                    @Override
//                    public void onStartTrackingTouch(SeekBar seekBar) {
//                        // Do something here,
//                        //if you want to do anything at the start of
//                        // touching the seekbar
//                    }
//
//                    @Override
//                    public void onStopTrackingTouch(SeekBar seekBar) {
//
//                        sendMessage("V" + String.valueOf(progress));
//
//                        // Display the value in textview
//                    }
//                });
//    }

    @Override
    protected void onResume() {
        super.onResume();
        String message = Utils.getLastSystemState(this);
        if(!TextUtils.isEmpty(message)){
            SystemResponse response = JsonParser.parseJson(message);
            setSystemResponseDisplay(response);
        }

        if (TextUtils.isEmpty(Utils.getSavedNumber(this)))
            showPhoneNumberDialog();
    }

    private void showPhoneNumberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter system number");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        input.setText(Utils.getSavedNumber(this));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                Utils.savePhoneNumber(MainActivity.this, userInput);
            }
        });
        builder.show();
    }

    private void setSystemResponseDisplay(SystemResponse response) {
        alarm1 = (ImageView) findViewById(R.id.alarm1);
        if (response.getAlarmStatus().get(0)) {
            alarm1.setImageResource(R.drawable.alarm_on);
        }

        alarm2 = (ImageView) findViewById(R.id.alarm2);
        if (response.getAlarmStatus().get(1)) {
            alarm2.setImageResource(R.drawable.alarm_on);
        }
        alarm3 = (ImageView) findViewById(R.id.alarm3);
        if (response.getAlarmStatus().get(2)) {
            alarm3.setImageResource(R.drawable.alarm_on);
        }

        alarm4 = (ImageView) findViewById(R.id.alarm4);
        if (response.getAlarmStatus().get(3)) {
            alarm4.setImageResource(R.drawable.alarm_on);
        }
        new CountDownTimer(2000, 200) {

            @Override
            public void onTick(long millisUntilFinished) {
                // empty
            }

            @Override
            public void onFinish() {

                alarm1.setImageResource(R.drawable.alarm_off);
                alarm2.setImageResource(R.drawable.alarm_off);
                alarm3.setImageResource(R.drawable.alarm_off);
                alarm4.setImageResource(R.drawable.alarm_off);
            }
        }.start();

        balanceTV.setText("GHC " +String.valueOf(response.getBalance()));
        double savedBalance = getSavedBalance();
        double responseBalance = response.getBalance();

        checkbalancelimit( savedBalance , responseBalance );

        TextView userTV = (TextView) findViewById(R.id.userTV);
        userTV.setText("System Response to " +  String.valueOf(response.getUser()));

        final TextView powerTV = (TextView) findViewById(R.id.powerTV);
        if (response.isIntruder() && Utils.getAlarmState(this)) {
            powerTV.setText("Intrusion Alert!!!");
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            if (alert == null) {
                // alert is null, using backup
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                // I can't see this ever being null (as always have a default notification)
                // but just incase
                if (alert == null) {
                    // alert backup is null, using 2nd backup
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), alert);
            r.play();
            r.play();
            r.play();
            new CountDownTimer(10000, 200) {
                int timerCounter = 0;

                @Override
                public void onTick(long millisUntilFinished) {
                    // empty

                    if (timerCounter % 2 == 0) {
                        powerTV.setBackgroundResource(R.drawable.red_background);
                    } else {
                        powerTV.setBackgroundResource(R.color.default_color);
                    }
                }

                @Override
                public void onFinish() {
                    powerTV.setBackgroundResource(R.drawable.white_background);

                    powerTV.setText("");
                    Utils.setAlarmState(MainActivity.this,false);
                }
            }.start();

        }


//        if (response.isPower()) {
//            ImageView imageView = (ImageView) findViewById(R.id.powerImage);
//            imageView.setImageResource(R.drawable.power_on);
//            Power = true;
//            powerTV.setText("System Enabled");
//        } else {
//            ImageView imageView = (ImageView) findViewById(R.id.powerImage);
//            imageView.setImageResource(R.drawable.power_off);
//            Power = false;
//            powerTV.setText("System Disabled");
//        }
    }

    public void checkbalancelimit(double balance ,double balanceLimit ){
        if(balance > balanceLimit) {
            balanceTV.setTextColor(getResources().getColor(R.color.red));}
        else{
            balanceTV.setTextColor(getResources().getColor(R.color.green));


        }
    }

    public String getContactName(Context context, String phoneNumber) {
//        ContentResolver cr = context.getContentResolver();
//        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
//        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
//        if (cursor == null) {
//            return null;
//        }
//        String contactName = null;
//        if (cursor.moveToFirst()) {
//            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//        }
//
//        if (cursor != null && !cursor.isClosed()) {
//            cursor.close();
//        }
//        if (contactName.isEmpty() || contactName == "") {
//            return phoneNumber;
//        } else {
//            return contactName;
//        }

        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void powerToggle(View view) {
        if (Power)
            sendMessage("Power off.");
        else
            sendMessage("Power on");
    }

    public void loadCredit(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Load Credit");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendMessage("Load" + input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void checkBalance(View view) {
        sendMessage("Check balance");
    }

    public void sendMessage(String message) {
        String phoneNumber = Utils.getSavedNumber(this);
        if (TextUtils.isEmpty(phoneNumber)) {
            showPhoneNumberDialog();
            return;
        }
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(MainActivity.this, "Empty operation parameter", Toast.LENGTH_SHORT).show();
            return;
        }

        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phoneNumber, null, message, null, null);
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

    }

    public void changeNumber(MenuItem item) {
        showPhoneNumberDialog();
    }

    public void setLimit(MenuItem item) {
        showBalanceDialog();
    }

    public void checkAlarm(View view) {
        int id = view.getId();
        if (id == R.id.alarm1) {
            sendMessage(ALARM_1);
        } else if (id == R.id.alarm2) {
            sendMessage(ALARM_2);
        } else if (id == R.id.alarm3) {
            sendMessage(ALARM_3);
        } else if (id == R.id.alarm4) {
            sendMessage(ALARM_4);
        }
    }

    private double getSavedBalance() {
        double am = PreferenceManager
                .getDefaultSharedPreferences(this)
                .getFloat(PREF_BALANCE, 2f);
        return am;
    }

    private void saveBalance(double balanceLimit) {
        PreferenceManager
                .getDefaultSharedPreferences(MainActivity.this)
                .edit()
                .putFloat(PREF_BALANCE, (float) balanceLimit)
                .apply();
    }

    private void showBalanceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter balance limit");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);
        input.setText(Double.toString(getSavedBalance()));

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                saveBalance(Double.parseDouble(userInput));
            }
        });
        builder.show();
    }

    void setBalanceLimit() {
        showBalanceDialog();
    }
}
