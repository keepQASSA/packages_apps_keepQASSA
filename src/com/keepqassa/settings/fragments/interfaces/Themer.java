/*
 * Copyright (C) 2020 DerpFest
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

package com.keepqassa.settings.fragments.interfaces;

import android.content.ContentResolver;
import android.content.Context;
import android.content.om.IOverlayManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import androidx.preference.*;
import androidx.preference.Preference.OnPreferenceChangeListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.display.OverlayCategoryPreferenceController;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

public class Themer extends DashboardFragment implements
        OnPreferenceChangeListener {
    private static final String TAG = "Themer";

    private ContentResolver mResolver;

    private static final String ACCENT_COLOR = "accent_color";
    private static final String ACCENT_COLOR_PROP = "persist.sys.theme.accentcolor";

    private IOverlayManager mOverlayService;
    private ColorPickerPreference mThemeColor;


    @Override
    public int getMetricsCategory() {
        return MetricsEvent.QASSA;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.themer;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setupAccentPref();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mThemeColor) {
            int color = (Integer) newValue;
            String hexColor = String.format("%08X", (0xFFFFFFFF & color));
            SystemProperties.set(ACCENT_COLOR_PROP, hexColor);
            try {
                 mOverlayService.reloadAndroidAssets(UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.settings", UserHandle.USER_CURRENT);
                 mOverlayService.reloadAssets("com.android.systemui", UserHandle.USER_CURRENT);
             } catch (RemoteException ignored) {
             }
        }
        return true;
    }

    private void setupAccentPref() {
        mThemeColor = (ColorPickerPreference) findPreference(ACCENT_COLOR);
        String colorVal = SystemProperties.get(ACCENT_COLOR_PROP, "-1");
        int color = "-1".equals(colorVal)
                ? Color.WHITE
                : Color.parseColor("#" + colorVal);
        mThemeColor.setNewPreviewColor(color);
        mThemeColor.setOnPreferenceChangeListener(this);
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle, Fragment fragment) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        /* controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.accent_color")); */
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.font"));
        /* controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.adaptive_icon_shape")); */
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.signal_icon"));
        controllers.add(new OverlayCategoryPreferenceController(context,
                "android.theme.customization.wifi_icon"));
        return controllers;
    }

}
