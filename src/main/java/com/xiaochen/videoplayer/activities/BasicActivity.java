package com.xiaochen.videoplayer.activities;

import android.support.v7.app.AppCompatActivity;

import com.xiaochen.videoplayer.utils.AppConfig;
import com.xiaochen.videoplayer.utils.Futils;
import com.xiaochen.videoplayer.utils.color.ColorPreference;
import com.xiaochen.videoplayer.utils.provider.UtilitiesProviderInterface;
import com.xiaochen.videoplayer.utils.theme.AppTheme;
import com.xiaochen.videoplayer.utils.theme.AppThemeManagerInterface;

/**
 * Created by rpiotaix on 17/10/16.
 */
public class BasicActivity extends AppCompatActivity implements UtilitiesProviderInterface {
    private boolean initialized = false;
    private UtilitiesProviderInterface utilsProvider;

    private void initialize() {
        utilsProvider = getAppConfig().getUtilsProvider();

        initialized = true;
    }

    protected AppConfig getAppConfig() {
        return (AppConfig) getApplication();
    }

    @Override
    public Futils getFutils() {
        if (!initialized)
            initialize();

        return utilsProvider.getFutils();
    }

    public ColorPreference getColorPreference() {
        if (!initialized)
            initialize();

        return utilsProvider.getColorPreference();
    }

    @Override
    public AppTheme getAppTheme() {
        if (!initialized)
            initialize();

        return utilsProvider.getAppTheme();
    }

    @Override
    public AppThemeManagerInterface getThemeManager() {
        if (!initialized)
            initialize();

        return utilsProvider.getThemeManager();

    }
}
