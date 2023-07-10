package security.smart.smartsecurity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

public class IncomingSMSBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "IncomingSMSBroadcastReceiver";
    private final String KEY_PDUS = "pdus"; // Key to extract pdu objects from bundle

    private final int PDU_OBJECT_INDEX = 0; // Expect only one pdu per message
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
      final Bundle bundle = intent.getExtras();
      if (bundle == null) throw new IllegalStateException("SMS Bundle is empty");

      final Object[] pduArr = (Object[]) bundle.get(KEY_PDUS);
      final byte[] pdu = (byte[]) pduArr[PDU_OBJECT_INDEX];
      if (pdu == null) throw new IllegalStateException("SMS pdu is empty");

        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu);
        if (!isFromRemoteDevice(context, smsMessage.getDisplayOriginatingAddress())) {
            // Not from remote sensors
            Log.d(TAG, "Not from remote sensors ignoring");
            return;
        }
        String msgBody = smsMessage.getMessageBody();
        if (TextUtils.isEmpty(msgBody)) throw new IllegalStateException("Receiving empty messages from remote sensors");

        // check for valid message
        Gson gson = new Gson();
        IncomingRemoteEventMessage sysEvenMsg = gson.fromJson(msgBody, IncomingRemoteEventMessage.class);
        RemoteDetectionEvent remoteDetectionEvent = sysEvenMsg.getS();
        if (remoteDetectionEvent == null) throw new IllegalStateException("Event parsing error");

        SystemStateOps.saveLastRemoteEvent(context, remoteDetectionEvent);
        SystemStateOps.setAlarmState(context,true);
        launchMainScreen(context);
    }

    private void launchMainScreen(Context context) {
        Intent i = new Intent(context.getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    private boolean isFromRemoteDevice(Context ctx, String srcPhoneNumber) {
        String remoteSensorsPhoneNumber = SystemStateOps.getSavedNumber(ctx);
        return remoteSensorsPhoneNumber.equals(srcPhoneNumber);
    }
}