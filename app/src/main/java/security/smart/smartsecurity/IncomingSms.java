package security.smart.smartsecurity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by user on 6/3/2016.
 */ //    The Incoming Sms Static Inner Class
public class IncomingSms extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String phoneNumber;
                String senderNum;
                String message;
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String sysNumber = Utils.getSavedNumber(context);
                    if (phoneNumber.equals(sysNumber)) {
                        senderNum = phoneNumber;
                        message = currentMessage.getDisplayMessageBody();

                        // Show Alert
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(context,
//                                "senderNum: " + senderNum + ", message: " + message, duration);
//                        toast.show();

                        if (TextUtils.isEmpty(sysNumber))
                            return;

                        Utils.saveSystemState(context, message);
                        Intent k = new Intent(context.getApplicationContext(), MainActivity.class);
                        k.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(k);
                        Utils.setAlarmState(context,true);
                        return;
                    }
                }

                // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
            throw e;
        }
    }
}
