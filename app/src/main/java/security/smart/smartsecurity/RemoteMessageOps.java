package security.smart.smartsecurity;

import android.content.Context;
import android.telephony.SmsManager;

public class RemoteMessageOps {
    private static final String MSG_CHECK_STATUS = "Check balance";

    private static final String MSG_ALL_ALARMS_TURN_ON = "all1";

    private static final String MSG_ALL_ALARMS_TURN_OFF = "all0";
    private static final String MSG_POWER_TURN_OFF = "Power off";
    private static final String MSG_POWER_TURN_ON = "Power on";

    private Context context;

    private SystemStateOps systemStateOps;

    public RemoteMessageOps(Context context) {
        this.context = context;
        this.systemStateOps = new SystemStateOps(context);
    }

    public void retrieveRemoteSystemState() {
        sendMessage(MSG_CHECK_STATUS);
    }

    public void setAlarmState(Alarm alarm, boolean state) {
        String stateStr = state ? "1" : "0";
        sendMessage(alarm.getLabel() + stateStr);
    }

    public void turnPowerOn() {
        sendMessage(MSG_POWER_TURN_ON);
    }

    public void turnPowerOff() {
        sendMessage(MSG_POWER_TURN_OFF);
    }

    public void turnAllAlarmsOff() {
        sendMessage(MSG_ALL_ALARMS_TURN_OFF);
    }

    public void turnAllAlarmsOn() {
        sendMessage(MSG_ALL_ALARMS_TURN_ON);
    }

    public void testAlarm(Alarm alarm) {
        sendMessage(alarm.getLabel());
    }

    public void loadCredit(String rechargeStr) {
        sendMessage(rechargeStr);
    }

    private void sendMessage(String message) {
        if (!systemStateOps.isSystemSetUp()) throw new IllegalStateException("System setup required");
        final String sysPhoneNumber = systemStateOps.getSavedNumber();
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(sysPhoneNumber, null, message, null, null);
    }
}
