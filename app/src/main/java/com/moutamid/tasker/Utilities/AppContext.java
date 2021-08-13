package com.moutamid.tasker.Utilities;

import android.app.Application;

import com.moutamid.tasker.Utilities.Utils;

public class AppContext extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        Stash.init(this);
        Utils.init(this);

    }
}
