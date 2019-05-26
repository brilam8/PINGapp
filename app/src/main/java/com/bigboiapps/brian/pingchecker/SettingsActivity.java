package com.bigboiapps.brian.pingchecker;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity  {
    public static final String MY_PREFS_NAME = "SettingsPref";
    public static final String KEY_PREF_WARNINGS = "pref_displayWarnings";
    public static final String KEY_PREF_NUMPINGS = "pref_numPings";
    public static final String KEY_PREF_ANIMATEDBG = "pref_animatedBackgrounds";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        /*getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();*/
    }
}