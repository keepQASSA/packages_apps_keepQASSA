/*
 *  Copyright (C) 2018 Rebellion-OS
 *  Copyright (C) 2019 Ancient-OS
 *  Copyright (C) 2022 QASSA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepqassa.settings;

import com.android.internal.logging.nano.MetricsProto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import androidx.preference.Preference;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.keepqassa.settings.fragments.Animations;
import com.keepqassa.settings.fragments.Battery;
import com.keepqassa.settings.fragments.Buttons;
import com.keepqassa.settings.fragments.Gestures;
import com.keepqassa.settings.fragments.LockScreen;
import com.keepqassa.settings.fragments.Misc;
import com.keepqassa.settings.fragments.Navigation;
import com.keepqassa.settings.fragments.Notifications;
import com.keepqassa.settings.fragments.QuickSettings;
import com.keepqassa.settings.fragments.StatusBar;

public class keepQASSA extends SettingsPreferenceFragment {

    private static final String TAG = "keepQASSA";
    ViewPager mViewPager;
    ViewGroup mContainer;
    PagerSlidingTabStrip mTabs;
    SectionsPagerAdapter mSectionsPagerAdapter;
    protected Context mContext;

    private static final int MENU_RESET = Menu.FIRST;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.keepqassa_settings_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContainer = container;
        View view = inflater.inflate(R.layout.keepqassa_settings, container, false);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setClipChildren(true);
        mViewPager.setClipToPadding(true);
        mTabs.setViewPager(mViewPager);
        mContext = getActivity().getApplicationContext();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle saveState) {
        super.onSaveInstanceState(saveState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, MENU_RESET, 0, R.string.reset_settings_title)
                .setIcon(R.drawable.ic_reset)
                .setAlphabeticShortcut('r')
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                        MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    }

    public void resetAll(Context context) {
        new ResetAllTask(context).execute();
    }

    public void showResetAllDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.reset_settings_title)
                .setMessage(R.string.reset_settings_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        resetAll(context);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private class ResetAllTask extends AsyncTask<Void, Void, Void> {
        private Context rContext;

        public ResetAllTask(Context context) {
            super();
            rContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            StatusBar.reset(rContext);
            QuickSettings.reset(rContext);
            LockScreen.reset(rContext);
            Battery.reset(rContext);
            Misc.reset(rContext);
            finish();
            startActivity(getIntent());
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_RESET:
                 showResetAllDialog(getActivity());
                return true;
            default:
                return false;
        }
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new StatusBar();
            frags[1] = new QuickSettings();
            frags[2] = new Buttons();
            frags[3] = new Gestures();
            frags[4] = new LockScreen();
            frags[5] = new Notifications();
            frags[6] = new Animations();
            frags[7] = new Battery();
            frags[8] = new Misc();
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[] {
            getString(R.string.statusbar_title),
            getString(R.string.quicksettings_title),
            getString(R.string.buttons_title),
            getString(R.string.gestures_title),
            getString(R.string.lockscreen_title),
            getString(R.string.notifications_title),
            getString(R.string.animations_title),
            getString(R.string.battery_title),
            getString(R.string.misc_title),
        };
        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.KEEPQASSA;
    }
}
