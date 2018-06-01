package com.jjoey.transporterdriver.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {

    public Context context;
    public SharedPreferences logOutPrefs, accountPrefs;
    public SharedPreferences.Editor editor, accountEditor;

    public static final String KEY_LOG_OUT_PREFS = "hasDriverLoggedOut";
    public static final String LOG_OUT = "driverLogOut";

    public static final String KEY_ACCOUNT_PREFS = "driverHasAccount";
    public static final String HAS_ACCOUNT = "driverAccount";

    private boolean value;

    public SharedPrefsHelper(Context context) {
        this.context = context;

        logOutPrefs = context.getSharedPreferences(KEY_LOG_OUT_PREFS, Context.MODE_PRIVATE);
        editor = logOutPrefs.edit();

        accountPrefs = context.getSharedPreferences(KEY_ACCOUNT_PREFS, Context.MODE_PRIVATE);
        accountEditor = accountPrefs.edit();

    }

    public void setHasAccount(boolean hasAccount) {
        accountEditor.putBoolean(HAS_ACCOUNT, hasAccount);
        accountEditor.commit();
    }

    public boolean getHasAccount() {
        return accountPrefs.getBoolean(HAS_ACCOUNT, false);
    }

    public void setLoggedOut(boolean value) {
        editor.putBoolean(LOG_OUT, value);
        editor.commit();
    }

    public boolean getLoggedOut(){
        return logOutPrefs.getBoolean(LOG_OUT, false);
    }

}
