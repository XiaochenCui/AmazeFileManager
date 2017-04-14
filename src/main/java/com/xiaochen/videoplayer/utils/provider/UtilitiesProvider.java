package com.xiaochen.videoplayer.utils.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.xiaochen.videoplayer.utils.Futils;
import com.xiaochen.videoplayer.utils.color.ColorPreference;
import com.xiaochen.videoplayer.utils.theme.AppTheme;
import com.xiaochen.videoplayer.utils.theme.AppThemeManagerInterface;
import com.xiaochen.videoplayer.utils.theme.PreferencesAppThemeManager;

/**
 * Created by piotaixr on 16/01/17.
 */

public class UtilitiesProvider implements UtilitiesProviderInterface {
    private Futils futils;
    private ColorPreference colorPreference;
    private AppThemeManagerInterface appThemeManager;


    public UtilitiesProvider(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        futils = new Futils();

        colorPreference = ColorPreference.loadFromPreferences(context, sharedPreferences);
        appThemeManager = new PreferencesAppThemeManager(sharedPreferences);
    }

    @Override
    public Futils getFutils() {
        return futils;
    }

    @Override
    public ColorPreference getColorPreference() {
        return colorPreference;
    }

    @Override
    public AppTheme getAppTheme() {
        return appThemeManager.getAppTheme();
    }

    @Override
    public AppThemeManagerInterface getThemeManager() {
        return appThemeManager;
    }
}
