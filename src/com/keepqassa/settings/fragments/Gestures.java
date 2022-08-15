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

import android.app.settings.SettingsEnums;
import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import android.provider.SearchIndexableResource;

import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.android.settingslib.search.SearchIndexable;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import com.android.settings.gestures.AssistGestureSettingsPreferenceController;
import com.android.settings.gestures.DoubleTapScreenPreferenceController;
import com.android.settings.gestures.PickupGesturePreferenceController;

@SearchIndexable
public class Gestures extends DashboardFragment
        implements Indexable {

    private static final String TAG = "Gestures";

    private AmbientDisplayConfiguration mAmbientDisplayConfig;

    @Override
    public int getMetricsCategory() {
        return SettingsEnums.SETTINGS_GESTURES;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.keepqassa_settings_gestures;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        use(AssistGestureSettingsPreferenceController.class).setAssistOnly(false);
        use(PickupGesturePreferenceController.class).setConfig(getConfig(context));
        use(DoubleTapScreenPreferenceController.class).setConfig(getConfig(context));
    }

    private AmbientDisplayConfiguration getConfig(Context context) {
        if (mAmbientDisplayConfig == null) {
            mAmbientDisplayConfig = new AmbientDisplayConfiguration(context);
        }
        return mAmbientDisplayConfig;
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
                    sir.xmlResId = R.xml.keepqassa_settings_gestures;
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
