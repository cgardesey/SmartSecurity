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
        if (bundle == null) {
            Log.d(TAG, "SMS Bundle is empty");
            return;
        }

        final Object[] pduArr = (Object[]) bundle.get(KEY_PDUS);
        final byte[] pdu = (byte[]) pduArr[PDU_OBJECT_INDEX];
        if (pdu == null) {
            Log.d(TAG, "SMS pdu is empty");
            return;
        }

        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu);
        if (!isFromRemoteDevice(context, smsMessage.getDisplayOriginatingAddress())) {
            // Not from remote sensors
            Log.d(TAG, "Not from remote sensors ignoring");
            return;
        }
        String msgBody = smsMessage.getMessageBody();
        if (TextUtils.isEmpty(msgBody)) {
            Log.d(TAG, "Receiving empty messages from remote sensors");
            return;
        }

        try {
            // check for valid message
            Gson gson = new Gson();
            IncomingSMSMessage sysEvenMsg = gson.fromJson(msgBody, IncomingSMSMessage.class);
            IncomingSMSMessage.InnerMessage innerMessage = sysEvenMsg.getS();
            LocalOps localOps = new LocalOps(context);
            localOps.saveRemoteSystemState(innerMessage.toRemoteSystemResponse());
            sendNewSystemStateEvent();
        } catch (Exception e) {
            Log.d(TAG, "Remote State parsing error" + e);
        }
    }

    private void sendNewSystemStateEvent() {
        EventBus eventBus = EventBus.getDefault();
        eventBus.post(new NewSystemStateEvent());
    }

    private boolean isFromRemoteDevice(Context context, String srcPhoneNumber) {
        LocalOps localOps = new LocalOps(context);
        String remoteSensorsPhoneNumber = localOps.getSavedNumber();
        return remoteSensorsPhoneNumber.equals(srcPhoneNumber);
    }

    public static class NewSystemStateEvent {
    }
}