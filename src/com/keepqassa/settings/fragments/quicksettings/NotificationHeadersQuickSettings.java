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

import com.keepqassa.settings.preferences.SystemSettingSwitchPreference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.qassa.ActionUtils;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

@SearchIndexable
public class NotificationHeadersQuickSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String TAG = "NotificationHeadersQuickSettings";

    private static final String CENTER_NOTIFICATION_HEADERS = "center_notification_headers";
    private static final String RIGHT_NOTIFICATION_HEADERS = "right_notification_headers";

    private SystemSettingSwitchPreference mCenterHeaders;
    private SystemSettingSwitchPreference mRightHeaders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.keepqassa_settings_notificationheaders_quicksettings);

        final ContentResolver resolver = getActivity().getContentResolver();

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
	if (preference == mCenterHeaders) {
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
