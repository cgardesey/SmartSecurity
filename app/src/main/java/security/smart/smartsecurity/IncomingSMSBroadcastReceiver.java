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

import org.greenrobot.eventbus.EventBus;

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
        IncomingSMSMessage sysEvenMsg = gson.fromJson(msgBody, IncomingSMSMessage.class);
        IncomingSMSMessage.InnerMessage innerMessage = sysEvenMsg.getS();
        if (innerMessage == null) throw new IllegalStateException("Event parsing error");

        SystemStateOps  systemStateOps = new SystemStateOps(context);
        systemStateOps.saveRemoteSystemState(innerMessage.toRemoteSystemResponse());
        sendNewSystemStateEvent();
    }

    private void sendNewSystemStateEvent() {
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new NewSystemStateEvent());
    }

    private boolean isFromRemoteDevice(Context context, String srcPhoneNumber) {
        SystemStateOps  systemStateOps = new SystemStateOps(context);
        String remoteSensorsPhoneNumber = systemStateOps.getSavedNumber();
        return remoteSensorsPhoneNumber.equals(srcPhoneNumber);
    }

    public static class NewSystemStateEvent {}
}