package com.example.readcommoninfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LunchActivity extends Activity {

    private String renderer;
    private String vendor;
    private String version;
    private String extensions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GLSurfaceView glView = new DemoGLSurfaceView(this);
        setContentView(glView);

    }
    class DemoGLSurfaceView extends GLSurfaceView {

        DemoRenderer mRenderer;
        public DemoGLSurfaceView(Context context) {
            super(context);
            setEGLConfigChooser(8, 8, 8, 8, 0, 0);
            mRenderer = new DemoRenderer();
            setRenderer(mRenderer);
        }
    }
    class DemoRenderer implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            renderer = gl.glGetString(GL10.GL_RENDERER);
            vendor = gl.glGetString(GL10.GL_VENDOR);
            version = gl.glGetString(GL10.GL_VERSION);
            extensions = gl.glGetString(GL10.GL_EXTENSIONS);
            Log.i("ttt", "GL_VERSION = " + version);
            Log.i("ttt", "GL_EXTENSIONS = " + gl.glGetString(GL10.GL_EXTENSIONS));
            Intent intent = new Intent(LunchActivity.this, MainService.class);
            intent.putExtra("renderer",renderer);
            intent.putExtra("vendor",vendor);
            intent.putExtra("version",version);
            intent.putExtra("extensions",extensions);
            startService(intent);
            finish();
        }

        @Override
        public void onDrawFrame(GL10 arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

        }

    }
}
