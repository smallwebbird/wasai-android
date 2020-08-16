package com.example.start.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class DisplayUtil {

        public class ScreenInfo {
            public int widthPixels;
            public int heightPixels;
            public int densityDpi;
            public float density;
            public float scaledDensity;

            ScreenInfo (int widthPixels, int heightPixels, int densityDpi, float density, float scaledDensity) {
                this.widthPixels = widthPixels;
                this.heightPixels = heightPixels;
                this.densityDpi = densityDpi;
                this.density = density;
                this.scaledDensity = scaledDensity;
            }
        }

        public ScreenInfo getScreenRelatedInformation(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                int heightPixels = outMetrics.heightPixels;
                int densityDpi = outMetrics.densityDpi;
                float density = outMetrics.density;
                float scaledDensity = outMetrics.scaledDensity;
                //可用显示大小的绝对宽度（以像素为单位）。
                //可用显示大小的绝对高度（以像素为单位）。
                //屏幕密度表示为每英寸点数。
                //显示器的逻辑密度。
                //显示屏上显示的字体缩放系数。
                ScreenInfo screenInfo = new ScreenInfo(widthPixels, heightPixels, densityDpi, density, scaledDensity);
                return screenInfo;
            }
            return null;
        }

        public ScreenInfo getRealScreenRelatedInformation(Context context) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (windowManager != null) {
                DisplayMetrics outMetrics = new DisplayMetrics();
                windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                int heightPixels = outMetrics.heightPixels;
                int densityDpi = outMetrics.densityDpi;
                float density = outMetrics.density;
                float scaledDensity = outMetrics.scaledDensity;
                //可用显示大小的绝对宽度（以像素为单位）。
                //可用显示大小的绝对高度（以像素为单位）。
                //屏幕密度表示为每英寸点数。
                //显示器的逻辑密度。
                //显示屏上显示的字体缩放系数。
                ScreenInfo screenInfo = new ScreenInfo(widthPixels, heightPixels, densityDpi, density, scaledDensity);
                return screenInfo;
            }
            return null;
        }
}
