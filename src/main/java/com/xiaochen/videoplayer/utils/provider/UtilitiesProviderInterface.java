package com.xiaochen.videoplayer.utils.provider;

import com.xiaochen.videoplayer.utils.Futils;
import com.xiaochen.videoplayer.utils.color.ColorPreference;
import com.xiaochen.videoplayer.utils.theme.AppTheme;
import com.xiaochen.videoplayer.utils.theme.AppThemeManagerInterface;

/**
 * Created by Rémi Piotaix <remi.piotaix@gmail.com> on 2016-10-17.
 */
public interface UtilitiesProviderInterface {
    Futils getFutils();

    ColorPreference getColorPreference();

    AppTheme getAppTheme();

    AppThemeManagerInterface getThemeManager();
}
