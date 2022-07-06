/*
 * Copyright (C) 2018 Havoc-OS
 * Copyright (C) 2022 QASSA
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
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.Settings;
import androidx.preference.*;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.qassa.ActionUtils;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.keepqassa.settings.preferences.SystemSettingListPreference;
import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;

public class Notifications extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Notifications";

    private static final String NOTIFICATION_HEADERS  = "notification_headers";
    private static final String CENTER_NOTIFICATION_HEADERS = "center_notification_headers";
    private static final String RIGHT_NOTIFICATION_HEADERS = "right_notification_headers";

    private SystemSettingSwitchPreference mShowHeaders;
    private SystemSettingSwitchPreference mCenterHeaders;
    private SystemSettingSwitchPreference mRightHeaders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.keepqassa_settings_notifications);
        PreferenceScreen prefScreen = getPreferenceScreen();
        final Resources res = getResources();

        final ContentResolver resolver = getActivity().getContentResolver();

        mShowHeaders = findPreference(NOTIFICATION_HEADERS);
        mShowHeaders.setChecked((Settings.System.getInt(resolver,
                Settings.System.NOTIFICATION_HEADERS, 1) == 1));
        mShowHeaders.setOnPreferenceChangeListener(this);

        mCenterHeaders = findPreference(CENTER_NOTIFICATION_HEADERS);
        mCenterHeaders.setChecked((Settings.System.getInt(resolver,
                Settings.System.CENTER_NOTIFICATION_HEADERS, 1) == 1));
        mCenterHeaders.setOnPreferenceChangeListener(this);

        mRightHeaders = findPreference(RIGHT_NOTIFICATION_HEADERS);
        mRightHeaders.setChecked((Settings.System.getInt(resolver,
                Settings.System.RIGHT_NOTIFICATION_HEADERS, 1) == 1));
        mRightHeaders.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        final ContentResolver resolver = getActivity().getContentResolver();
	if (preference == mShowHeaders) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.NOTIFICATION_HEADERS, value ? 1 : 0);
            ActionUtils.showSystemUiRestartDialog(getContext());
            return true;
	} else if (preference == mCenterHeaders) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.CENTER_NOTIFICATION_HEADERS, value ? 1 : 0);
            ActionUtils.showSystemUiRestartDialog(getContext());
            return true;
        } else if (preference == mRightHeaders) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver,
                    Settings.System.RIGHT_NOTIFICATION_HEADERS, value ? 1 : 0);
            ActionUtils.showSystemUiRestartDialog(getContext());
            return true;
        }
        return true;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KEEPQASSA;
    }
}
