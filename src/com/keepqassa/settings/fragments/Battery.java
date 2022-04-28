/*
 * Copyright (C) 2018 Havoc-OS
 * Copyright (C) 2022 AOSQP
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
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import androidx.preference.*;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.keepqassa.settings.preferences.SystemSettingListPreference;
import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;

import com.android.internal.logging.nano.MetricsProto;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.Set;

public class Battery extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "Battery";

    private static final String CATEGORY_BATTERY = "status_bar_battery_key";

    private static final String ICON_BLACKLIST = "icon_blacklist";

    private static final String STATUS_BAR_BATTERY_STYLE = "status_bar_battery_style";
    private static final String STATUS_BAR_SHOW_BATTERY_PERCENT = "status_bar_show_battery_percent";

    private static final int STATUS_BAR_BATTERY_STYLE_TEXT = 2;

    private SystemSettingListPreference mStatusBarBattery;
    private SystemSettingListPreference mStatusBarBatteryShowPercent;

    private PreferenceCategory mStatusBarBatteryCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.keepqassa_settings_battery);

        mStatusBarBatteryShowPercent =
                (SystemSettingListPreference) findPreference(STATUS_BAR_SHOW_BATTERY_PERCENT);
        mStatusBarBattery =
                (SystemSettingListPreference) findPreference(STATUS_BAR_BATTERY_STYLE);
        mStatusBarBattery.setOnPreferenceChangeListener(this);
        enableStatusBarBatteryDependents(mStatusBarBattery.getIntValue(2));

        mStatusBarBatteryCategory =
                (PreferenceCategory) getPreferenceScreen().findPreference(CATEGORY_BATTERY);
    }

    @Override
    public void onResume() {
        super.onResume();

        final String curIconBlacklist = Settings.Secure.getString(getContext().getContentResolver(),
                ICON_BLACKLIST);

        if (TextUtils.delimitedStringContains(curIconBlacklist, ',', "battery")) {
            getPreferenceScreen().removePreference(mStatusBarBatteryCategory);
        } else {
            getPreferenceScreen().addPreference(mStatusBarBatteryCategory);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    int value = Integer.parseInt((String) newValue);
        String key = preference.getKey();
        switch (key) {
            case STATUS_BAR_BATTERY_STYLE:
                enableStatusBarBatteryDependents(value);
                break;
        }
        return true;
    }

    private void enableStatusBarBatteryDependents(int batteryIconStyle) {
        mStatusBarBatteryShowPercent.setEnabled(batteryIconStyle != STATUS_BAR_BATTERY_STYLE_TEXT);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KEEPQASSA;
    }
}
