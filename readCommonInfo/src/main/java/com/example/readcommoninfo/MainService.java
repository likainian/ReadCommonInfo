package com.example.readcommoninfo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MainService extends Service {
    private String gl_renderer;
    private String gl_vendor;
    private String gl_version;
    private String gl_extensions;
    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            gl_renderer = intent.getStringExtra("renderer");
            gl_vendor = intent.getStringExtra("vendor");
            gl_version = intent.getStringExtra("version");
            gl_extensions = intent.getStringExtra("extensions");
        }
        StatInfo.init(this, gl_renderer, gl_vendor, gl_version, gl_extensions, null);
        return super.onStartCommand(intent, flags, startId);
    }
}
