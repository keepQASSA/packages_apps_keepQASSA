/*
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
package com.keepqassa.settings.fragments.quicksettings;

import android.app.Activity;
import android.content.Context;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import android.provider.SearchIndexableResource;
import android.provider.Settings;

import com.keepqassa.settings.preferences.CustomSeekBarPreference;
import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class BlurQuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener, Indexable {

    public static final String TAG = "BlurQuickSettings";

    private static final String QS_BLUR_RADIUS = "qs_blur_radius";

    private CustomSeekBarPreference mQsBlurRadius;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.keepqassa_settings_blurquicksettings);

        PreferenceScreen prefScreen = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();

        mQsBlurRadius = (CustomSeekBarPreference) findPreference(QS_BLUR_RADIUS);
        final int blurRadius = Settings.System.getInt(resolver,
                Settings.System.QS_BLUR_RADIUS, 25);
            mQsBlurRadius.setValue((blurRadius));
            mQsBlurRadius.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        switch (preference.getKey()) {
        case QS_BLUR_RADIUS:
                Integer blurRadius = (Integer) newValue;
                Settings.System.putInt(resolver,
                        Settings.System.QS_BLUR_RADIUS, blurRadius);
                return true;

            default:
                return false;
        }
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
                    sir.xmlResId = R.xml.keepqassa_settings_blurquicksettings;
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
