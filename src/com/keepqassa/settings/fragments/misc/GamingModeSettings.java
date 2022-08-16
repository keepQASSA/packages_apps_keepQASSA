/*
 * Copyright (C) 2020 The exTHmUI Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keepqassa.settings.fragments.misc;

import com.android.internal.logging.nano.MetricsProto;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.provider.SearchIndexableResource;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.SwitchPreference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.search.SearchIndexable;

import java.util.List;
import java.util.ArrayList;

import com.keepqassa.settings.preferences.PackageListPreference;
import com.keepqassa.settings.preferences.SystemSettingSeekBarPreference;

@SearchIndexable
public class GamingModeSettings extends SettingsPreferenceFragment
        implements OnPreferenceChangeListener, Indexable {

    private boolean mPerformanceSupported;
    private PackageListPreference mGamingPrefList;
    private SwitchPreference mUseMenuSwitch;
    private SwitchPreference mShowFPS;
    private Preference mDanmaku;
    private Preference mQapps;
    private SystemSettingSeekBarPreference mOpacity;

    private boolean mFpsInfoSupported;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        addPreferencesFromResource(R.xml.exthm_settings_gaming);

        final PreferenceScreen prefScreen = getPreferenceScreen();

        mGamingPrefList = (PackageListPreference) findPreference("gaming_mode_app_list");
        mGamingPrefList.setRemovedListKey(Settings.System.GAMING_MODE_REMOVED_APP_LIST);

        final PreferenceCategory perfCat = (PreferenceCategory) prefScreen
                .findPreference("performance_category");

        mPerformanceSupported = getResources().getBoolean(
                    com.android.internal.R.bool.config_gamingmode_performance);

        final String fpsInfoSysNode = getResources().getString(
                com.android.internal.R.string.config_gamingmode_fpsInfoSysNode);
        mFpsInfoSupported = !TextUtils.isEmpty(fpsInfoSysNode);

        if (!mPerformanceSupported) {
            prefScreen.removePreference(perfCat);
        }

        mUseMenuSwitch = (SwitchPreference) findPreference("gaming_mode_use_overlay_menu");
        mShowFPS = (SwitchPreference) findPreference("gaming_mode_fps_info");
        mDanmaku = (Preference) findPreference("gaming_mode_notification_danmaku");
        mQapps = (Preference) findPreference("gaming_mode_quick_start_apps");
        mOpacity = (SystemSettingSeekBarPreference) findPreference("gaming_mode_menu_opacity");

        boolean fpsEnabled = Settings.System.getInt(getContentResolver(),
                            Settings.System.GAMING_MODE_SHOW_FPSINFO, 0) == 1;

        if (mFpsInfoSupported) {
            mShowFPS.setChecked(fpsEnabled);
            mShowFPS.setOnPreferenceChangeListener(this);
        } else {
            prefScreen.removePreference(mShowFPS);
        }

        boolean menuEnabled = Settings.System.getInt(getContentResolver(),
                            Settings.System.GAMING_MODE_USE_OVERLAY_MENU, 1) == 1;
        mUseMenuSwitch.setChecked(menuEnabled);
        mUseMenuSwitch.setOnPreferenceChangeListener(this);

        mDanmaku.setEnabled(menuEnabled);
        mQapps.setEnabled(menuEnabled);
        mOpacity.setEnabled(menuEnabled);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mUseMenuSwitch) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.GAMING_MODE_USE_OVERLAY_MENU,
                    value ? 1 : 0);
            mDanmaku.setEnabled(value);
            mQapps.setEnabled(value);
            mOpacity.setEnabled(value);
//            if (mFpsInfoSupported)
//                mShowFPS.setEnabled(value);
            return true;
        } else if (preference == mShowFPS) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, Settings.System.GAMING_MODE_SHOW_FPSINFO,
                    value ? 1 : 0);
            return true;
        }
        return false;
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
                    sir.xmlResId = R.xml.exthm_settings_gaming;
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
