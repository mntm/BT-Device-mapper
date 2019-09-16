package com.bluetooth.googlemap.manager;

import android.graphics.Color;
import com.bluetooth.googlemap.DeviceAdapter;

public class ColorManager {
    private static ColorManager ourInstance = null;

    public boolean isThemeDark = false;

    public final static int DARK_PRIMARY_DARK = Color.rgb(0, 0, 0);
    public final static int DARK_PRIMARY = Color.rgb(50, 50, 50);
    public final static int DARK_BACKGROUND = Color.rgb(100, 100, 100);

    public final static int BRIGHT_PRIMARY_DARK = Color.rgb(150, 150, 150);
    public final static int BRIGHT_PRIMARY = Color.rgb(200, 200, 200);
    public final static int BRIGHT_BACKGROUND = Color.rgb(255, 255, 255);

    public final static int DARK_TEXT = Color.rgb(255, 255, 255);
    public final static int BRIGHT_TEXT = Color.rgb(190, 190, 190);

    private ColorManager() {}
    public static ColorManager getInstance() {
        if (ourInstance == null)
            ourInstance = new ColorManager();
        return ourInstance;
    }

}
