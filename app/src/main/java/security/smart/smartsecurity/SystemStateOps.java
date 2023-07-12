package security.smart.smartsecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by user on 6/23/2016.
 */
public class SystemStateOps {
    private static final String PREF_PHONE_NUMBER = "PREF_PHONE_NUMBER";
    private static final String PREF_LAST_SYSTEM_STATE = "PREF_LAST_SYSTEM_STATE";
    private static final String PREF_ALARM_STATE = "PREF_ALARM_STATE";

    private Context context;
    public SystemStateOps(Context context) {
        this.context = context;
    }

    public String getSavedNumber() {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_key_system_phone_number), "");
    }

    public void savePhoneNumber(String number) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(context.getString(R.string.pref_key_system_phone_number), number)
                .apply();
    }

    public void saveRemoteSystemState(RemoteSystemState state) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        Gson gson = new Gson();
        editor.putString(PREF_LAST_SYSTEM_STATE, gson.toJson(state)).apply();
    }

    public boolean isSystemSetUp() {
        return !TextUtils.isEmpty(getSavedNumber());
    }

    public boolean hasRemoteSystemState() {
        String savedEvent = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_LAST_SYSTEM_STATE, "");
        return !TextUtils.isEmpty(savedEvent);
    }

    public RemoteSystemState getRemoteSystemState() {
        String savedEvent = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREF_LAST_SYSTEM_STATE, "");
        return new Gson().fromJson(savedEvent, RemoteSystemState.class);
    }

    public String getLowBalanceWarningLimit() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.pref_key_low_balance_warning_limit), "0.0");
    }
}
