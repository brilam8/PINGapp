package com.bigboiapps.brian.pingchecker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.Guideline;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    public static final String MY_PREFS_NAME = "SettingsPref";
    public static final String KEY_PREF_NUMPINGS = "pref_numPings";
    public static final String KEY_PREF_WARNINGS = "pref_displayWarnings";
    public static final String KEY_PREF_ANIMATEDBG = "pref_animatedBackgrounds";
    public static final int bigDistance = 150;
    private final static String APP_PNAME = "com.bigboiapps.brian.pingchecker.free";// Package Name


    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(KEY_PREF_NUMPINGS)) {
            Preference pingsPref = findPreference(key);
            String value = sharedPreferences.getString(key, "5");

            // Set summary to be the user-description for the selected value
            pingsPref.setSummary(getString(R.string.pref_summary_numPings));
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE).edit();
            editor.putString(key, value);
            editor.apply();
        } else if (key.equals(KEY_PREF_ANIMATEDBG)) {
            Preference animatedPref = findPreference(key);
            boolean value = sharedPreferences.getBoolean(key, false);// Set summary to be the user-description for the selected value
            //animatedPref.setSummary(sharedPreferences.getBoolean(key, true));
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE).edit();
            editor.putBoolean(key,value);
            editor.apply();
        }
        else if (key.equals(KEY_PREF_WARNINGS)) {
            Preference warnings = findPreference(key);
            boolean value = sharedPreferences.getBoolean(key, true);// Set summary to be the user-description for the selected value
            //animatedPref.setSummary(sharedPreferences.getBoolean(key, true));
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE).edit();
            editor.putBoolean(key,value);
            editor.apply();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);


    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        int height = args.getInt("height");
        int type = args.getInt("type");

        Preference button = findPreference("rateApp");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //code for what you want it to do
                getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                return true;
            }
        });



        System.out.println(height);
        ListView lv = (ListView)view.findViewById(android.R.id.list);
        ViewGroup parent = (ViewGroup) lv.getParent();
        if (type == 1) {
            parent.setPadding(0, height, 0, 0);
        }
        else if (type == 2){
            parent.setPadding(height, 0, 0, 0);
        }
    }
}
