package security.smart.smartsecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by user on 6/23/2016.
 */
public final class SystemStateOps {

    private SystemStateOps() {
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
    public static void saveLastRemoteEvent(Context context, String message) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putString(PREF_LAST_SYSTEM_STATE, message).commit();
    }

    public static void saveLastRemoteEvent(Context context, RemoteDetectionEvent event) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        Gson gson = new Gson();
        editor.putString(PREF_LAST_SYSTEM_STATE, gson.toJson(event)).apply();
    }

    public static boolean isSystemSetUp(Context ctx) {
        return !TextUtils.isEmpty(getSavedNumber(ctx));
    }

    public static boolean hasPendingRemoteEvent(Context ctx) {
        String savedEvent = PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString(PREF_LAST_SYSTEM_STATE, "");
        return !TextUtils.isEmpty(savedEvent);
    }

    public static RemoteDetectionEvent getLastRemoteEvent(Context ctx) {
        String savedEvent = PreferenceManager
                .getDefaultSharedPreferences(ctx)
                .getString(PREF_LAST_SYSTEM_STATE, "");
        return new Gson().fromJson(savedEvent, RemoteDetectionEvent.class);
    }

    public static void clearLastPendingEvent(Context ctx) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(ctx).edit();
        editor.remove(PREF_LAST_SYSTEM_STATE).apply();
    }
}
