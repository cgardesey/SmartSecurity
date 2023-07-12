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
    private static final String PREF_PHONE_NUMBER = "PREF_PHONE_NUMBER";
    private static final String PREF_LAST_SYSTEM_STATE = "PREF_LAST_SYSTEM_STATE";
    private static final String PREF_ALARM_STATE = "PREF_ALARM_STATE";
    private static final String PENDING_STATE = "PENDING_STATE";

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
                .putString(PREF_LAST_SYSTEM_STATE, gson.toJson(state))
                .putBoolean(PENDING_STATE, true)
                .apply();
    }

    public boolean isSystemSetUp() {
        return !TextUtils.isEmpty(getSavedNumber());
    }

    public boolean hasRemoteSystemState() {
        String savedEvent = prefs.getString(PREF_LAST_SYSTEM_STATE, "");
        return !TextUtils.isEmpty(savedEvent);
    }

    public RemoteSystemState getRemoteSystemState() {
        String savedEvent = prefs.getString(PREF_LAST_SYSTEM_STATE, "");
        return new Gson().fromJson(savedEvent, RemoteSystemState.class);
    }

    public double getLowBalanceWarningLimit() {
        String balStr = prefs.getString(context.getString(R.string.pref_key_low_balance_warning_limit),
                context.getString(R.string.pref_low_balance_warning_limit_default));
        return Double.parseDouble(balStr);
    }

    public boolean hasPendingState() {
        return prefs.getBoolean(PENDING_STATE, false);
    }

    public void clearPendingState() {
        prefs.edit().remove(PENDING_STATE).apply();
    }
}
