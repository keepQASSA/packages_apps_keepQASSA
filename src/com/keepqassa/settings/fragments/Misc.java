/*
 * Copyright (C) 2018 Havoc-OS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keepqassa.settings.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.keepqassa.settings.fragments.misc.GamingMode;
import com.android.settings.SettingsPreferenceFragment;

public class Misc extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Misc";

    private static final String KEY_GAMES_SPOOF = "use_games_spoof";
    private static final String KEY_PHOTOS_SPOOF = "use_photos_spoof";
    private static final String KEY_STREAM_SPOOF = "use_stream_spoof";
    private static final String SYS_GAMES_SPOOF = "persist.sys.pixelprops.games";
    private static final String SYS_PHOTOS_SPOOF = "persist.sys.pixelprops.gphotos";
    private static final String SYS_STREAM_SPOOF = "persist.sys.pixelprops.streaming";

    private SwitchPreference mGamesSpoof;
    private SwitchPreference mPhotosSpoof;
    private SwitchPreference mStreamSpoof;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.keepqassa_settings_misc);

	final PreferenceScreen prefScreen = getPreferenceScreen();
        final ContentResolver resolver = getActivity().getContentResolver();
        Resources res = getResources();

        mGamesSpoof = (SwitchPreference) prefScreen.findPreference(KEY_GAMES_SPOOF);
        mGamesSpoof.setChecked(SystemProperties.getBoolean(SYS_GAMES_SPOOF, false));
        mGamesSpoof.setOnPreferenceChangeListener(this);

        mPhotosSpoof = (SwitchPreference) prefScreen.findPreference(KEY_PHOTOS_SPOOF);
        mPhotosSpoof.setChecked(SystemProperties.getBoolean(SYS_PHOTOS_SPOOF, true));
        mPhotosSpoof.setOnPreferenceChangeListener(this);

        mStreamSpoof = (SwitchPreference) findPreference(KEY_STREAM_SPOOF);
        mStreamSpoof.setChecked(SystemProperties.getBoolean(SYS_STREAM_SPOOF, true));
        mStreamSpoof.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	if (preference == mGamesSpoof) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_GAMES_SPOOF, value ? "true" : "false");
            return true;
        } else if (preference == mPhotosSpoof) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_PHOTOS_SPOOF, value ? "true" : "false");
            return true;
        } else if (preference == mStreamSpoof) {
            boolean value = (Boolean) newValue;
            SystemProperties.set(SYS_STREAM_SPOOF, value ? "true" : "false");
            return true;
        }
        return true;
    }

    public static void reset(Context mContext) {
    final ContentResolver resolver = mContext.getContentResolver();
	SystemProperties.set(SYS_GAMES_SPOOF, "false");
        SystemProperties.set(SYS_PHOTOS_SPOOF, "true");
        SystemProperties.set(SYS_STREAM_SPOOF, "true");
        GamingMode.reset(mContext);
        Settings.System.putIntForUser(resolver,
                Settings.System.SCREENSHOT_SOUND, 1, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KEEPQASSA;
    }
}
