package security.smart.smartsecurity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kobby on 10-Jun-16.
 */
public class JsonParser {
    public static final String TAG_ALARMS ="A";
    public static final String TAG_POWER ="P";
    public static final String TAG_INTRUDER ="I";
    public static final String TAG_BALANCE ="B";
    public static final String TAG_USER ="U";
    public static final String TAG_VOLUME ="V";

    public static SystemResponse parseJson(String jsonString){
        SystemResponse response = new SystemResponse();

        try {

            JSONObject j = new JSONObject(jsonString);
            JSONObject jsonObject = j.getJSONObject("S");

            // get Alarms
            JSONArray alarms = jsonObject.getJSONArray(TAG_ALARMS);
            List<Boolean> alarmList = new ArrayList<>(4);

            for (int i = 0; i < alarms.length(); i++) {

                int obj = (int) alarms.get(i);
                alarmList.add(getBooleanValueFromInt(obj));
            }
            response.setAlarmStatus(alarmList);

            // power
            int powVal = jsonObject.getInt(TAG_POWER);
            response.setPower(getBooleanValueFromInt(powVal));

            // Intruder
            int intruderVal = jsonObject.getInt(TAG_INTRUDER);
            response.setIntruder(getBooleanValueFromInt(intruderVal));

            // balance
            double balance = jsonObject.getDouble(TAG_BALANCE);
            response.setBalance(balance);

            // user
            String user = jsonObject.getString(TAG_USER);
            response.setUser(user);

            // volume
            int volume = jsonObject.getInt(TAG_VOLUME);
            response.setVolume(volume);

        } catch (Exception e){
            Log.e("JsonParser", e.toString());
        }

        return response;
    }

    public static boolean getBooleanValueFromInt(int val){
        if (val == 0)
            return false;
        else
            return true;
    }
}
