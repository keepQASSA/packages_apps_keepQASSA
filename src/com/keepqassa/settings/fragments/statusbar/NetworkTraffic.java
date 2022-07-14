/*
 * Copyright (C) 2017-2019 The LineageOS Project
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
package com.keepqassa.settings.fragments.statusbar;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.keepqassa.settings.preferences.SystemSettingListPreference;
import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class NetworkTraffic extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener  {

    private static final String TAG = "NetworkTraffic";

    private SystemSettingListPreference mNetTrafficMode;
    private SystemSettingSwitchPreference mNetTrafficAutohide;
    private SystemSettingListPreference mNetTrafficUnitType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.keepqassa_settings_networktraffic);
        final ContentResolver resolver = getActivity().getContentResolver();

        mNetTrafficMode = (SystemSettingListPreference) findPreference(Settings.System.NETWORK_TRAFFIC_LOCATION);
        mNetTrafficMode.setOnPreferenceChangeListener(this);
        int mode = Settings.System.getInt(resolver,
                Settings.System.NETWORK_TRAFFIC_LOCATION, 0);
        mNetTrafficMode.setValue(String.valueOf(mode));

        mNetTrafficAutohide = (SystemSettingSwitchPreference) findPreference(Settings.System.NETWORK_TRAFFIC_AUTOHIDE);
        mNetTrafficAutohide.setOnPreferenceChangeListener(this);

        mNetTrafficUnitType = (SystemSettingListPreference) findPreference(Settings.System.NETWORK_TRAFFIC_UNIT_TYPE);
        mNetTrafficUnitType.setOnPreferenceChangeListener(this);
        int units = Settings.System.getInt(resolver,
                Settings.System.NETWORK_TRAFFIC_UNIT_TYPE, /* Mbps */ 1);
        mNetTrafficUnitType.setValue(String.valueOf(units));

        updateEnabledStates(mode);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mNetTrafficMode) {
            int mode = Integer.valueOf((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NETWORK_TRAFFIC_LOCATION, mode);
            updateEnabledStates(mode);
        } else if (preference == mNetTrafficUnitType) {
            int unitType = Integer.valueOf((String) newValue);
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.NETWORK_TRAFFIC_UNIT_TYPE, unitType);
        }
        return true;
    }

    private void updateEnabledStates(int mode) {
        final boolean enabled = mode != 0;
        mNetTrafficAutohide.setEnabled(enabled);
        mNetTrafficUnitType.setEnabled(enabled);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsEvent.KEEPQASSA;
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
                    sir.xmlResId = R.xml.keepqassa_settings_networktraffic;
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
