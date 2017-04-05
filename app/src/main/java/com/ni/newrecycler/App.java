package com.ni.newrecycler;

import android.app.Application;

import com.ni.newrecycler.utils.ImageLoaderUtil;


/**
 * Created by dell on 2017/2/25.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ImageLoaderUtil.init(getApplicationContext());
    }

}