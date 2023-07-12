package security.smart.smartsecurity;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by user on 6/23/2016.
 */
public class LocalOps {
    private static final String PREF_KEY_LAST_SYSTEM_STATE = "PREF_LAST_SYSTEM_STATE";
    private static final String PREF_KEY_PENDING_ALARM_STATE = "PREF_KEY_PENDING_ALARM_STATE";

    private Context context;
    private SharedPreferences prefs;

    public LocalOps(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getSavedNumber() {
        return prefs.getString(context.getString(R.string.pref_key_system_phone_number), "");
    }

    public void savePhoneNumber(String number) {
        prefs.edit().putString(context.getString(R.string.pref_key_system_phone_number), number).apply();
    }

    public void saveRemoteSystemState(RemoteSystemState state) {
        Gson gson = new Gson();
        prefs.edit()
                .putString(PREF_KEY_LAST_SYSTEM_STATE, gson.toJson(state))
                .apply();
    }

    public boolean isSystemSetUp() {
        return !TextUtils.isEmpty(getSavedNumber());
    }

    public RemoteSystemState getRemoteSystemState() {
        String systemStateStr = prefs.getString(PREF_KEY_LAST_SYSTEM_STATE, "");
        if (TextUtils.isEmpty(systemStateStr)) {
            return new RemoteSystemState();
        } else {
            return new Gson().fromJson(systemStateStr, RemoteSystemState.class);
        }
    }

    public double getLowBalanceWarningLimit() {
        String balStr = prefs.getString(context.getString(R.string.pref_key_low_balance_warning_limit),
                context.getString(R.string.pref_low_balance_warning_limit_default));
        return Double.parseDouble(balStr);
    }

    public boolean hasAlarmState() {
        return prefs.getBoolean(PREF_KEY_PENDING_ALARM_STATE, false);
    }

    public void clearAlarmState() {
        prefs.edit().putBoolean(PREF_KEY_PENDING_ALARM_STATE, false).apply();
    }

    public void setAlarmState() {
        prefs.edit().putBoolean(PREF_KEY_PENDING_ALARM_STATE, true).apply();
    }
}
