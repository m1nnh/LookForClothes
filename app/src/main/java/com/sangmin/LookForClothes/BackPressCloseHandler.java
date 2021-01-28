package com.sangmin.LookForClothes;

import android.app.Activity;

public class BackPressCloseHandler {
    private long backKeyClickTime = 0;
    private Activity activity;

    public BackPressCloseHandler(Activity activity) {
        this.activity = activity;
    }
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyClickTime + 1000) {
            backKeyClickTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() <= backKeyClickTime + 1000) {
            activity.finish();
        }
    }
}
