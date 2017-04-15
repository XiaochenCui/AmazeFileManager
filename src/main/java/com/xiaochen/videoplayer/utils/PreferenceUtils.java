package com.xiaochen.videoplayer.utils;

import android.content.SharedPreferences;
import android.graphics.Color;

import java.util.Calendar;

/**
 * Created by Vishal on 12-05-2015.
 */
public class PreferenceUtils {
    private static int primary = -1, accent = -1, folder = -1, primaryTwo = -1;

    public static final String KEY_PRIMARY_TWO = "skin_two";
    public static final String KEY_ACCENT = "accent_skin";
    public static final String KEY_ICON_SKIN = "icon_skin";
    public static final String KEY_CURRENT_TAB = "current_tab";
    public static final String KEY_ROOT = "rootmode";
    public static final String KEY_PATH_COMPRESS = "zippath";

    public static final int DEFAULT_PRIMARY = 4;
    public static final int DEFAULT_ACCENT = 1;
    public static final int DEFAULT_ICON = -1;
    public static final int DEFAULT_CURRENT_TAB = 1;

    public static int getStatusColor(String skin) {
        int c=darker(Color.parseColor(skin),0.6f);
        return c;
    }

    public static int getStatusColor(int skin) {
        int c=darker(skin,0.6f);
        return c;
    }

    public static int darker (int color, float factor) {
        int a = Color.alpha(color);
        int r = Color.red( color );
        int g = Color.green( color );
        int b = Color.blue( color );

        return Color.argb( a,
                Math.max( (int)(r * factor), 0 ),
                Math.max( (int)(g * factor), 0 ),
                Math.max( (int)(b * factor), 0 ) );
    }

    public static int getAccent(SharedPreferences Sp) {
        if (accent == -1)
            accent = Sp.getInt(KEY_ACCENT, DEFAULT_ACCENT);
        return accent;
    }

    /**
     * Get primary color of second tab from preferences
     * @return the color position in color array; from the preferences
     */
    public static int getPrimaryTwoColor(SharedPreferences Sp) {
        return primaryTwo==-1 ? Sp.getInt(KEY_PRIMARY_TWO, DEFAULT_PRIMARY) : null;
    }

    public static int getFolderColor(SharedPreferences Sp){
        if(folder==DEFAULT_ICON) {
            int icon = Sp.getInt(KEY_ICON_SKIN, DEFAULT_ICON);
            folder = icon == DEFAULT_ICON ? Sp.getInt(KEY_ACCENT, DEFAULT_ACCENT) : icon;
        }
        return folder;
    }

    @Deprecated
    public static String getAccentString(SharedPreferences Sp) {
        return (colors[getAccent(Sp)]);
    }

    public static void reset(){
        primary=accent=folder=primaryTwo=-1;
    }

    @Deprecated
    public final static String[] colors = new String[]{
            "#F44336",
            "#e91e63",
            "#9c27b0",
    };

    @Deprecated
    public static int hourOfDay() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour <= 6 || hour >= 18) {
            return 1;
        } else
            return 0;
    }
}
