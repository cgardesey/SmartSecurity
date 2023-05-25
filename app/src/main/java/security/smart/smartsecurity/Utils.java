package security.smart.smartsecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.WorkerThread;

/**
 * Created by user on 6/23/2016.
 */
public final class Utils {

    private Utils() {
    }

    public static final String PREF_PHONE_NUMBER = "PREF_PHONE_NUMBER";
    public static final String PREF_LAST_SYSTEM_STATE = "PREF_LAST_SYSTEM_STATE";
    public static final String PREF_ALARM_STATE = "PREF_ALARM_STATE";


    public static String getSavedNumber(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_PHONE_NUMBER, "");
    }

    public static void savePhoneNumber(Context context, String number) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_PHONE_NUMBER, number)
                .apply();
    }

    public static boolean getAlarmState(Context context){
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(PREF_ALARM_STATE, false);
    }

    public static void setAlarmState(Context context, Boolean state) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_ALARM_STATE, state)
                .apply();
    }

    public static String getLastSystemState(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_LAST_SYSTEM_STATE, "");
    }

    @WorkerThread
    public static void saveSystemState(Context context, String message) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString(PREF_LAST_SYSTEM_STATE, message).commit();
    }



}
