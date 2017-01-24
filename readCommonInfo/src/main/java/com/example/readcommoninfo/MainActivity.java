package com.example.readcommoninfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getName();
	private ListView lvInfo;
	private List<String> list = new ArrayList<String>();

	private ArrayAdapter<String> mAdapter;
	private String gl_renderer;
	private String gl_vendor;
	private String gl_version;
	private String gl_extensions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		fillList();
		initAdapter();

		StatInfo.init(this, gl_renderer, gl_vendor, gl_version, gl_extensions, null);
	}

	private void initAdapter() {
		mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
		lvInfo.setAdapter(mAdapter);
	}
	private void initView(){
		gl_renderer = getIntent().getStringExtra("renderer");
		gl_vendor = getIntent().getStringExtra("vendor");
		gl_version = getIntent().getStringExtra("version");
		gl_extensions = getIntent().getStringExtra("extensions");

		lvInfo = (ListView)findViewById(R.id.lvInfo);
	}
	
	
	private void fillList(){
		 
		list.clear();

		list.add("gl_renderer: "+gl_renderer);
		list.add("gl_vendor: "+gl_vendor);
		list.add("gl_version: "+gl_version);

		list.add("ID: "+Build.ID);
		list.add("DISPLAY: "+Build.DISPLAY);
		list.add("Product: "+Build.PRODUCT);
		list.add("Device: "+Build.DEVICE);
		list.add("Board: "+Build.BOARD);
		list.add("MANUFACTURER: "+Build.MANUFACTURER);
		list.add("Brand: "+Build.BRAND);
		list.add("MODEL: "+Build.MODEL);
		list.add("BOOTLOADER: "+Build.BOOTLOADER);
		list.add("HARDWARE: "+Build.HARDWARE);
		list.add("SERIAL: "+Build.SERIAL);
		list.add("VERSION.INCREMENTAL: "+Build.VERSION.INCREMENTAL);
		list.add("VERSION.RELEASE: "+Build.VERSION.RELEASE);
		//list.add("VERSION.BASE_OS: "+Build.VERSION.BASE_OS);
		//list.add("VERSION.SECURITY_PATCH: "+Build.VERSION.SECURITY_PATCH);
		list.add("VERSION.SDK_INT: "+Build.VERSION.SDK_INT);
		//list.add("VERSION.PREVIEW_SDK_INT: "+Build.VERSION.PREVIEW_SDK_INT);
		list.add("VERSION.CODENAME: "+Build.VERSION.CODENAME);
		list.add("TYPE: "+Build.TYPE);
		list.add("TAGS: "+Build.TAGS);
		list.add("FINGERPRINT: "+Build.FINGERPRINT);
		
		list.add("TIME: "+Build.TIME);
		list.add("USER: "+Build.USER);
		list.add("HOST: "+Build.HOST);
		
		list.add("RadioVersion: "+Build.getRadioVersion());
		
		list.add("ANDROID_ID: "+Settings.System.getString(getContentResolver(), Settings.System.ANDROID_ID));

	}



}
