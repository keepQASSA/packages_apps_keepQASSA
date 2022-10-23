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

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.text.TextUtils;
import android.util.ArraySet;
import android.view.View;
import androidx.preference.*;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.keepqassa.settings.fragments.battery.BatteryBar;
import com.keepqassa.settings.preferences.SystemSettingMasterSwitchPreference;
import com.keepqassa.settings.preferences.SystemSettingSeekBarPreference;
import com.keepqassa.settings.preferences.SystemSettingListPreference;
import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;
import com.keepqassa.settings.utils.TelephonyUtils;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.qassa.ActionUtils;
import com.keepqassa.settings.fragments.statusbar.Clock;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.internal.util.custom.cutout.CutoutUtils;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SearchIndexable
public class StatusBar extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    private static final String ICON_BLACKLIST = "icon_blacklist";

    private static final String STATUS_BAR_CLOCK_STYLE = "status_bar_clock";
    private static final String STATUSBAR_ICONS_STYLE = "statusbar_icons_style";
    private static final String NETWORK_TRAFFIC_STATE = "network_traffic_state";

    private SystemSettingListPreference mStatusBarClock;

    private SystemSettingSwitchPreference mStatusbarIconsStyle;

    private static boolean sHasCenteredNotch;

    private SystemSettingMasterSwitchPreference mNetMonitor;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.keepqassa_settings_statusbar);
        PreferenceScreen prefSet = getPreferenceScreen();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();
        Context mContext = getActivity().getApplicationContext();

        sHasCenteredNotch = CutoutUtils.hasCenteredCutout(getActivity());

        mStatusBarClock =
                (SystemSettingListPreference) findPreference(STATUS_BAR_CLOCK_STYLE);
        mStatusBarClock.setOnPreferenceChangeListener(this);

	mStatusbarIconsStyle = (SystemSettingSwitchPreference) findPreference(STATUSBAR_ICONS_STYLE);
        mStatusbarIconsStyle.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUSBAR_ICONS_STYLE, 0) == 1));
        mStatusbarIconsStyle.setOnPreferenceChangeListener(this);

        mNetMonitor = (SystemSettingMasterSwitchPreference) findPreference(NETWORK_TRAFFIC_STATE);
        mNetMonitor.setChecked((Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.NETWORK_TRAFFIC_STATE, 0) == 1));
        mNetMonitor.setOnPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        final String curIconBlacklist = Settings.Secure.getString(getContext().getContentResolver(),
                ICON_BLACKLIST);

        final boolean disallowCenteredClock = sHasCenteredNotch;

        // Adjust status bar preferences for RTL
        if (getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            if (disallowCenteredClock) {
                mStatusBarClock.setEntries(R.array.status_bar_clock_position_entries_notch_rtl);
                mStatusBarClock.setEntryValues(R.array.status_bar_clock_position_values_notch);
            } else {
                mStatusBarClock.setEntries(R.array.status_bar_clock_position_entries_rtl);
                mStatusBarClock.setEntryValues(R.array.status_bar_clock_position_values);
            }
        } else if (disallowCenteredClock) {
            mStatusBarClock.setEntries(R.array.status_bar_clock_position_entries_notch);
            mStatusBarClock.setEntryValues(R.array.status_bar_clock_position_values_notch);
        } else {
            mStatusBarClock.setEntries(R.array.status_bar_clock_position_entries);
            mStatusBarClock.setEntryValues(R.array.status_bar_clock_position_values);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
    final ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mStatusbarIconsStyle) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUSBAR_ICONS_STYLE, value ? 1 : 0);
            ActionUtils.showSystemUiRestartDialog(getContext());
            return true;
        } else if (preference == mNetMonitor) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NETWORK_TRAFFIC_STATE, value ? 1 : 0);
            return true;
        }
        return true;
    }

    public static void reset(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Settings.System.putIntForUser(resolver,
                Settings.System.BLUETOOTH_SHOW_BATTERY, 1, UserHandle.USER_CURRENT);
        Clock.reset(mContext);
        BatteryBar.reset(mContext);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KEEPQASSA;
    }

    /**
     * For search
     */
    public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    ArrayList<SearchIndexableResource> result =
                            new ArrayList<SearchIndexableResource>();
                    SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.keepqassa_settings_statusbar;
                    result.add(sir);

                    return result;
                }
                @Override
                public List<String> getNonIndexableKeys(Context context) {
                    List<String> keys = super.getNonIndexableKeys(context);

                    return keys;
                }
            };
}
