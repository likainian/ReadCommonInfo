package com.example.readcommoninfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by dinghao on 2016/12/20.
 */

@SuppressLint("NewApi")
public class StatInfo {
    private static final String TAG = StatInfo.class.getName();
    private Context mContext;
    private SharedPreferences settings;
    private static StatInfo instance;
    private Callback mCallback;

    private SensorManager mSensorManager;
    private String mStoreDir = "/sdcard";//getFilesDir().getAbsolutePath();
    private String mUa=null;
    private long memTotal = 0, memAvail = 0;

    private String mPackageName;
    private String mVersionName;
    private int mVersionCode;
    private String mLanguage;

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println("初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /** Unknown network class. */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /** Class of broadly defined "2G" networks. */
    private static final int NETWORK_CLASS_2_G = 1;
    /** Class of broadly defined "3G" networks. */
    private static final int NETWORK_CLASS_3_G = 2;
    /** Class of broadly defined "4G" networks. */
    private static final int NETWORK_CLASS_4_G = 3;

    // 适配低版本手机
    /** Network type is unknown */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /** Current network is GPRS */
    public static final int NETWORK_TYPE_GPRS = 1;
    /** Current network is EDGE */
    public static final int NETWORK_TYPE_EDGE = 2;
    /** Current network is UMTS */
    public static final int NETWORK_TYPE_UMTS = 3;
    /** Current network is CDMA: Either IS95A or IS95B */
    public static final int NETWORK_TYPE_CDMA = 4;
    /** Current network is EVDO revision 0 */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /** Current network is EVDO revision A */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /** Current network is 1xRTT */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /** Current network is HSDPA */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /** Current network is HSUPA */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /** Current network is HSPA */
    public static final int NETWORK_TYPE_HSPA = 10;
    /** Current network is iDen */
    public static final int NETWORK_TYPE_IDEN = 11;
    /** Current network is EVDO revision B */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /** Current network is LTE */
    public static final int NETWORK_TYPE_LTE = 13;
    /** Current network is eHRPD */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /** Current network is HSPA+ */
    public static final int NETWORK_TYPE_HSPAP = 15;

	private StatInfo(Context context, Callback callback){
        mContext = context;
        mCallback = callback;
        settings = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        try{
            mStoreDir = mContext.getFilesDir().getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
            try {
                mStoreDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            }catch (Exception e2){}
        }
        try {
            WebView mWebView = new WebView(context);
            mUa = mWebView.getSettings().getUserAgentString();
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            PackageInfo info = getPackageInfo();
            if(info != null) {
                mPackageName = info.packageName;
                mVersionName = info.versionName;
                mVersionCode = info.versionCode;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            Locale locale = mContext.getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            mLanguage = language.toLowerCase();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static boolean deinit(){
        if(instance != null){
            instance = null;
        }
        return true;
    }

    public interface Callback {
        public void onStarted();
        public void onFinished(boolean success);
    }
    public static void init(Context context,
                            String gl_renderer, String gl_vendor,
                            String gl_version, String gl_extensions, Callback callback){

        try {
			if(instance == null){
			    instance = new StatInfo(context, callback);
			    if(gl_renderer != null){
			        instance.putString("gl_renderer",gl_renderer );
			    }
			    if(gl_vendor != null){
			        instance.putString("gl_vendor",gl_vendor );
			    }
			    if(gl_version != null){
			        instance.putString("gl_version",gl_version );
			    }
			    if(gl_extensions != null){
			        instance.putString("gl_extensions",gl_extensions );
			    }
			}
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			    instance.doStat();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void log(String s){
        Log.e(TAG, s);
    }
    private PackageInfo getPackageInfo() {
        PackageInfo packageInfo = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            String packageName = mContext.getPackageName();
            // flags提供了10种选项，及其组合，如果只是获取版本号，flags=0即可
            int flags = 0;
            // 通过packageInfo即可获取AndroidManifest.xml中的信息。
            packageInfo = packageManager.getPackageInfo(packageName, flags);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo;
    }
    private boolean getBoolean(String key, boolean defValue){
        return settings.getBoolean(key, defValue);
    }
    private boolean putBoolean(String key, boolean v){
        SharedPreferences.Editor e = settings.edit();
        e.putBoolean(key, v);
        return e.commit();
    }
    private boolean putString(String key, String v){
        SharedPreferences.Editor e = instance.settings.edit();
        e.putString(key, v);
        return e.commit();
    }

    private String getString(String key, String defValue){
        return instance.settings.getString(key, defValue);
    }

    private void doStat(){
        if(!getBoolean("registered", false)){
            log("doStat");
            List<String> applist= getAppInfo();
            StringBuilder sb = new StringBuilder();
            for(String s: applist){
                sb.append(s).append("\n");
            }
            final String appListStr = sb.toString();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    }catch(Exception e){}
                    if(mCallback != null){
                        mCallback.onStarted();
                    }
                    //log("stat begin");
                    PlatformInfo c = initTestClient();
                    if(c == null){
                        log("initTestClient failed.");
                        if(mCallback != null){
                            mCallback.onFinished(false);
                        }
                        return;
                    }
                    if(!ZipFile("/system/build.prop", "build.prop", mStoreDir+"/prop.zip")){
                        log("save build.prop failed.");
                        if(mCallback != null){
                            mCallback.onFinished(false);
                        }
                        return;
                    }

                    StringBuilder sb = new StringBuilder();
                    travelDir(sb, new File("/"), 4, ' ', new FileNameFilter() {

                        @Override
                        public boolean accept(String filename) {
                            // TODO Auto-generated method stub
                            if(filename.startsWith(Environment.getExternalStorageDirectory().getAbsolutePath())){
                                return false;
                            }
                            if(filename.startsWith("/sys"))
                                return false;
                            if(filename.startsWith("/dev"))
                                return false;
                            if(filename.startsWith("/acct"))
                                return false;
                            if(filename.startsWith("/storage"))
                                return false;
                            if(filename.startsWith("/mnt"))
                                return false;
                            if(filename.indexOf("sdcard")>0)
                                return false;
                            //Log.e(TAG, "accept1: "+filename);
                            if(filename.startsWith("/proc")&&filename.length()>6) {
                                filename = filename.substring(6);
                                int i = filename.indexOf("/");
                                if(i>0) filename = filename.substring(0, i);
                                if(checkNumber(filename))
                                    return false;
                                if(filename.equals("irq")||filename.equals("sys")||filename.equals("asound"))
                                    return false;
                            }
                            return true;
                        }

                    });
                    String tree = sb.toString();
                    if(tree!=null&&tree.length()>0) {
                        if (!saveTxtFile(mStoreDir + "/tree.txt", tree)) {
                            log("save tree.txt failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                        if(!ZipFile(mStoreDir+"/tree.txt", "tree.txt", mStoreDir+"/tree.zip")){
                           log("save tree.zip failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                    }

                    sb = new StringBuilder();

                    List<String> files = getProcFiles();
                    //Log.e(TAG, "Files Count: "+files.size());
                    for(String filepath: files){
                        sb.append("==== "+filepath+" ====\n");
                        List<String> content = ReadTxtLines(filepath, 0);
                        if(content != null){
                            for(String ct: content){
                                sb.append(ct).append("\n");
                            }
                        }
                    }
                    String procs = sb.toString();
                    if(procs!=null&&procs.length()>0) {
                        if (!saveTxtFile(mStoreDir + "/proc.txt", procs)) {
                           log("save proc.txt failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                        if(!ZipFile(mStoreDir+"/proc.txt", "proc.txt", mStoreDir+"/proc.zip")){
                           log("save proc.zip failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                    }
                    if(appListStr!=null&&appListStr.length()>0) {
                        if (!saveTxtFile(mStoreDir + "/app.txt", appListStr)) {
                           log("save app.txt failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                        if(!ZipFile(mStoreDir+"/app.txt", "app.txt", mStoreDir+"/app.zip")){
                           log("save app.zip failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                    }

                    sb = new StringBuilder();
                    try {
                        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                        for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                            Camera.getCameraInfo(cameraId, cameraInfo);
                            Camera camera = Camera.open(cameraId);
                            Camera.Parameters params = camera.getParameters();
                            appendCameraInfo(sb, cameraId, "getAntibanding", params.getAntibanding());
                            appendCameraInfo(sb, cameraId, "getColorEffect", params.getColorEffect());
                            appendCameraInfo(sb, cameraId, "getFlashMode", params.getFlashMode());
                            appendCameraInfo(sb, cameraId, "getFocusMode", params.getFocusMode());
                            appendCameraInfo(sb, cameraId, "getSceneMode", params.getSceneMode());
                            appendCameraInfo(sb, cameraId, "getWhiteBalance", params.getWhiteBalance());
                            appendCameraInfo(sb, cameraId, "getExposureCompensation", ""+params.getExposureCompensation());
                            appendCameraInfo(sb, cameraId, "getExposureCompensationStep", ""+params.getExposureCompensationStep());
                            appendCameraInfo(sb, cameraId, "getFocalLength", ""+params.getFocalLength());
                            appendCameraInfo(sb, cameraId, "getFocusAreas", cameraAreaListToString(params.getFocusAreas()));

                            appendCameraInfo(sb, cameraId, "getJpegQuality", ""+params.getJpegQuality());
                            appendCameraInfo(sb, cameraId, "getJpegThumbnailQuality", ""+params.getJpegThumbnailQuality());
                            appendCameraInfo(sb, cameraId, "getJpegThumbnailSize", cameraSizeToString(params.getJpegThumbnailSize()));
                            appendCameraInfo(sb, cameraId, "getMaxExposureCompensation", ""+params.getMaxExposureCompensation());
                            appendCameraInfo(sb, cameraId, "getMaxNumDetectedFaces", ""+params.getMaxNumDetectedFaces());
                            appendCameraInfo(sb, cameraId, "getMaxNumFocusAreas", ""+params.getMaxNumFocusAreas());
                            appendCameraInfo(sb, cameraId, "getMaxNumMeteringAreas", ""+params.getMaxNumMeteringAreas());
                            appendCameraInfo(sb, cameraId, "getMaxZoom", ""+params.getMaxZoom());
                            appendCameraInfo(sb, cameraId, "getMeteringAreas", cameraAreaListToString(params.getMeteringAreas()));
                            appendCameraInfo(sb, cameraId, "getMinExposureCompensation", ""+params.getMinExposureCompensation());
                            appendCameraInfo(sb, cameraId, "getPictureFormat", ""+params.getPictureFormat());
                            appendCameraInfo(sb, cameraId, "getPictureSize", cameraSizeToString(params.getPictureSize()));
                            appendCameraInfo(sb, cameraId, "getPreferredPreviewSizeForVideo", cameraSizeToString(params.getPreferredPreviewSizeForVideo()));
                            appendCameraInfo(sb, cameraId, "getPreviewFormat", ""+params.getPreviewFormat());


                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", ""+params.getPreviewFrameRate());
                            appendCameraInfo(sb, cameraId, "getPreviewSize", cameraSizeToString(params.getPreviewSize()));
                            appendCameraInfo(sb, cameraId, "getSupportedAntibanding", cameraStringListToString(params.getSupportedAntibanding()));
                            appendCameraInfo(sb, cameraId, "getSupportedColorEffects", cameraStringListToString(params.getSupportedColorEffects()));
                            appendCameraInfo(sb, cameraId, "getSupportedFlashModes", cameraStringListToString(params.getSupportedFlashModes()));
                            appendCameraInfo(sb, cameraId, "getSupportedFocusModes", cameraStringListToString(params.getSupportedFocusModes()));
                            appendCameraInfo(sb, cameraId, "getSupportedJpegThumbnailSizes", cameraSizeListToString(params.getSupportedJpegThumbnailSizes()));
                            appendCameraInfo(sb, cameraId, "getSupportedPictureFormats", cameraIntListToString(params.getSupportedPictureFormats()));
                            appendCameraInfo(sb, cameraId, "getSupportedPictureSizes", cameraSizeListToString(params.getSupportedPictureSizes()));
                            appendCameraInfo(sb, cameraId, "getSupportedPreviewFormats", cameraIntListToString(params.getSupportedPreviewFormats()));
                            //sb.append(""+cameraId+":getPreviewFrameRate="+params.getSupportedPreviewFpsRange()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", cameraIntListToString(params.getSupportedPreviewFrameRates()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", cameraSizeListToString(params.getSupportedPreviewSizes()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", cameraStringListToString(params.getSupportedSceneModes()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", cameraSizeListToString(params.getSupportedVideoSizes()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", cameraStringListToString(params.getSupportedWhiteBalance()));
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", ""+params.getVerticalViewAngle());
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", ""+params.getVideoStabilization());
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", ""+params.getZoom());
                            appendCameraInfo(sb, cameraId, "getPreviewFrameRate", ""+cameraIntListToString(params.getZoomRatios()));
                            camera.release();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                       log("get camera failed: "+e);
                    }
                    String cams = sb.toString();
                    if(cams.length()>0) {
                        if (!saveTxtFile(mStoreDir + "/cam.txt", sb.toString())) {
                           log("save cam.txt failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                        if(!ZipFile(mStoreDir+"/cam.txt", "cam.txt", mStoreDir+"/cam.zip")){
                           log("save cam.zip failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                    }
                    String gl_extensions =getString("gl_extensions", null);
                    if(gl_extensions!=null&&gl_extensions.length()>0) {
                        if (!saveTxtFile(mStoreDir + "/gpu.txt", gl_extensions)) {
                           log("save gpu.txt failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                        if(!ZipFile(mStoreDir+"/gpu.txt", "gpu.txt", mStoreDir+"/gpu.zip")){
                           log("save gpu.zip failed.");
                            if(mCallback != null){
                                mCallback.onFinished(false);
                            }
                            return;
                        }
                    }

                    String json = JSONHelper.toJSON(c);
                    Log.e(TAG, "debug="+isApkInDebug()+"，toJSON="+json);
                    if(json == null || json.length()<10){
                       log("toJSON failed.");
                        if(mCallback != null){
                            mCallback.onFinished(false);
                        }
                        return;
                    }

                    if(!saveTxtFile(mStoreDir+"/info.txt", json)){
                       log("save json.txt failed.");
                        if(mCallback != null){
                            mCallback.onFinished(false);
                        }
                        return;
                    }

                    if(!ZipFile(mStoreDir+"/info.txt", "info.txt", mStoreDir+"/info.zip")){
                       log("save info.zip failed.");
                        if(mCallback != null){
                            mCallback.onFinished(false);
                        }
                        return;
                    }

                    try {
                        Map<File,String> map = new HashMap<File,String>();
                        File f = new File(mStoreDir+"/info.zip");
                        if(f.exists()) {
                            map.put(f, "infoFile");
                        }
                        f=new File(mStoreDir+"/app.zip");
                        if(f.exists()) {
                            map.put(f, "appListFile");
                        }
                        f=new File(mStoreDir+"/tree.zip");
                        if(f.exists()) {
                            map.put(f, "treeFile");
                        }
                        f=new File(mStoreDir+"/proc.zip");
                        if(f.exists()) {
                            map.put(f, "procFile");
                        }
                        f=new File(mStoreDir+"/prop.zip");
                        if(f.exists()) {
                            map.put(f, "propFile");
                        }
                        f=new File(mStoreDir+"/cam.zip");
                        if(f.exists()) {
                            map.put(f, "camFile");
                        }
                        f=new File(mStoreDir+"/gpu.zip");
                        if(f.exists()) {
                            map.put(f, "gpuFile");
                        }
                        final boolean b = testClientRegister(c.getCustKey(), map, new ProgressHandler() {
                            @Override
                            public void updateSize(String fileName, long size) {
                               log("updateSize: "+fileName+" "+size);
                            }
                        });
                       log(b?"register sucess":"register failed!");
                        if(b) {
                            putBoolean("registered", true);
                        }
                        if(mCallback != null){
                            mCallback.onFinished(b);
                        }
                        return;
                    }catch(Exception e){
                       log("doRegister Exeption: "+e);
                    }
                    if(mCallback != null){
                        mCallback.onFinished(false);
                    }
                }
            }).start();
        }
    }
    public static boolean ZipFile(String inputPath, String fileName, String outputPath){
        if(fileName == null || fileName.length()==0) return false;
        try{
            File f = new File(outputPath);
            if(!f.exists()){
                File d = f.getParentFile();
                if(d!=null){
                    d.mkdirs();
                }
            }
            f.createNewFile();
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
            zipOut.putNextEntry(new ZipEntry(fileName));
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputPath));
            byte[] b = new byte[1024];
            int readBytes= 0;
            while ((readBytes=bis.read(b)) != -1) {
                zipOut.write(b, 0, readBytes);
            }

            zipOut.closeEntry();
            zipOut.close();
            return true;
        }catch(Exception ioe){
            ioe.printStackTrace();
            System.out.println("ZipFile: Exception"+ioe);
        }
        return false;
    }
    private void appendCameraInfo(StringBuilder sb, int cameraId, String token, String value){
        if(value != null && value.trim().length()>0){
            sb.append(""+cameraId+":"+token.trim()+"="+value.trim()).append("\n");
        }
    }
    public static String cameraSizeToString(Camera.Size size)
    {
        if(size== null) return null;
        return String.format("%dx%d",size.width,size.height);
    }

    public static String cameraIntListToString(List<Integer> list)
    {
        if(list== null || list.size()==0) return null;
        String s = "";
        for (int l : list)
        {
            if(s.length()>0){
                s+=",";
            }
            s+=l;
        }
        return s;
    }
    public static String cameraStringListToString(List<String> list)
    {
        if(list== null || list.size()==0) return null;
        String s = "";
        for (String l : list)
        {
            if(s.length()>0){
                s+=",";
            }
            s+=l;
        }
        return s;
    }
    public static String cameraSizeListToString(List<Camera.Size> list)
    {
        if(list== null || list.size()==0) return null;
        String s = "";
        try {
            for (Camera.Size size : list) {
                if (s.length() > 0) {
                    s += ",";
                }
                s += String.format("%dx%d", size.width, size.height);
            }
        }catch (Exception e){}
        return s;
    }

    public static String cameraAreaListToString(List<Camera.Area> list)
    {
        if(list== null || list.size()==0) return null;
        String s = "";
        try {
            for (Camera.Area area : list) {
                if (s.length() > 0) {
                    s += ",";
                }
                s += String.format("%dx%dx%dx%dx%d", area.rect.left, area.rect.top, area.rect.right, area.rect.bottom, area.weight);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }
    public interface FileNameFilter{
        boolean accept(String filename);
    }
    /*
     * 参数：root:要遍历的文件，
         *spanToLeft：距离左边的空白或者分隔符数目，
         *spanChar：空白字符，
         *filter：根据条件选择文件
     *
     */
    private void travel(StringBuilder strDir, File root, int spanToLeft, char spanChar, FileNameFilter filter) {
        try {
            spanToLeft += 4;
            if (isSymbolicLink(root)) {
                return;
            } else if (!filter.accept(root.getAbsolutePath())) {
                return;
            } else if (root.isFile()) {
                String name = root.getName();

                for (int k = 0; k < spanToLeft; k++) {
                    strDir.append(spanChar);
                }
                strDir.append(root.getName() + "\r\n");
                //fileCount++;
            } else if (root.isDirectory()) {
                for (int k = 0; k < spanToLeft; k++) {
                    strDir.append(spanChar);
                }
                strDir.append(root.getName() + "$\r\n");
                //dirCount++;
                File[] children = root.listFiles();
                if (children == null)
                    return;
                for (int i = 0; i < children.length; i++) {
                    travel(strDir, children[i], spanToLeft, spanChar, filter);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void travelDir(StringBuilder strDir, File root, int spanToLeft, char spanChar,
                           FileNameFilter filter){
        if (root == null)
            return;
        if (!root.exists()) {
            //System.err.println("file " + root.getName() + " does not exist!");
            return;
        }
        travel(strDir, root, spanToLeft, spanChar, filter);
    }

    private static boolean isSymbolicLink(File f)  {
        try {
            return !f.getAbsolutePath().equals(f.getCanonicalPath());
        }catch(Exception e){
            //e.printStackTrace();
            return true;
        }
    }

    private List<String> getProcFiles(){
        List<String> list = new ArrayList<String>();
        try {
            File d = new File("/proc");
            File[] children = d.listFiles();
            if (children == null)
                return list;
            for (File f : children) {
                if (checkNumber(f.getName())) continue;
                String fileName = f.getName().toLowerCase();
                //Log.e(TAG, "getProcFiles: "+fileName);
                if (fileName.indexOf("info") >= 0 ||
                        fileName.indexOf("version") >= 0) {
                    getAllFiles(list, f);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private void getAllFiles(List<String> list, File root){
        if (root == null)
            return;
        if (!root.exists()) {
            return;
        }

        if(!root.canRead()){
            return;
        }

        if(isSymbolicLink(root)){
            return;
        }else if(root.isFile()) {
            //Log.e(TAG, "getAllFiles: "+root.getAbsolutePath());
            list.add(root.getAbsolutePath());
        } else if (root.isDirectory()) {
            File[] children = root.listFiles();
            if (children == null)
                return;
            for (int i = 0; i < children.length; i++) {
                getAllFiles(list, children[i]);
            }
        }

    }
    public static boolean checkNumber(String value){
        String regex = "^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$";
        return value.matches(regex);
    }
    private String getScreenResolution(boolean simple){
        try {
            DisplayMetrics dm = new DisplayMetrics();
            Display display = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            display.getMetrics(dm);
            String strOpt = String.format("%dx%d", dm.widthPixels, dm.heightPixels);
            if (simple) {
                return strOpt;
            } else {
                float density = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
                float densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
                Point size = new Point();
                display.getRealSize(size);

                int realWidth = size.x;
                int realHeight = size.y;
                double mInch = Math.sqrt((realWidth / dm.xdpi) * (realWidth / dm.xdpi) + (realHeight / dm.ydpi) * (realHeight / dm.ydpi));
                String result = String.format("%.2f", mInch);
                return strOpt + ":" + density + ":" + densityDPI + ":" + result;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private String getMemInfo() {
        try {
            ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            memAvail = mi.availMem;
            memTotal = mi.totalMem;
            return String.format("%d:%d", mi.totalMem, mi.availMem);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }
    // 获取CPU最小频率（单位KHZ）
    private static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = { "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq" };
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }
    private static int getCpuCoreNum() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            // Gingerbread doesn't support giving a single application access to both cores, but a
            // handful of devices (Atrix 4G and Droid X2 for example) were released with a dual-core
            // chipset and Gingerbread; that can let an app in the background run without impacting
            // the foreground application. But for our purposes, it makes them single core.
            return 1;
        }
        int cores=0;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (Exception e) {
        }
        return cores;
    }
    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            //regex is slow, so checking char by char.
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };
    private String getCpuInfo() {
        try {
            FileReader fr = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(fr);
            String text = null;
            String cpuName = null;
            String cpuArch = null;
            while((text= br.readLine())!=null){
                String[] array = text.split(":", 2);
                if(array[0].contains("Hardware")){
                    cpuName = array[1];
                }
                if(array[0].contains("architecture")){
                    cpuArch = array[1];
                }
            }
            br.close();
            if(cpuName != null) {
                cpuName = cpuName.trim();
            }
            if(cpuArch != null) {
                cpuArch = cpuArch.trim();
            }

            return cpuName + ":" + getMaxCpuFreq() + ":" + getMinCpuFreq() + ":" +cpuArch + ":" + getCpuCoreNum();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public String getSensorInfo(int type){
        try {
            Sensor sensor = mSensorManager.getDefaultSensor(type);
            if (sensor != null) {
                return sensor.getName() + ":" + sensor.getType() + ":" + sensor.getVendor();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private String getAid(){
        try{
            return Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static long getTotalRomSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    private static long[] getRomBlockSize() {
        try {
            File path = Environment.getDataDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long blockSize = statFs.getBlockSize();
            long totalBlocks = statFs.getBlockCount();
            long availableBlocks = statFs.getAvailableBlocks();
            return new long[]{blockSize, totalBlocks, availableBlocks};
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private static long[] getsdBlockSize() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = Environment.getExternalStorageDirectory();
                StatFs statFs = new StatFs(file.getPath());
                long blockSize = statFs.getBlockSize();
                long totalBlocks = statFs.getBlockCount();
                long availableBlocks = statFs.getAvailableBlocks();
                return new long[]{blockSize, totalBlocks, availableBlocks};
            } else {
                return new long[]{0, 0, 0};
            }
        }catch (Exception e){
        }
        return null;
    }
    private String getImei() {
        try {
            // String IMSI =android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMEI);
            TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);//取得相关系统服务
            return tm.getDeviceId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    private String getWifiMacAddr() {
        try{
            //在wifi未开启状态下，仍然可以获取MAC地址，但是IP地址必须在已连接状态下否则为0
            WifiManager wifiMgr = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
            if (null != info) {
                return info.getMacAddress();
            }else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String exec(String cmd){
        //Log.e(TAG, "exec: "+cmd);
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            // read the ls output
            String line = "";
            StringBuilder sb = new StringBuilder(line);
            while ((line = bufferedreader.readLine()) != null) {
                //System.out.println(line);
                sb.append(line);
                sb.append('\n');
            }
            if (proc.waitFor() != 0) {
               log("getProp: exit value=" + proc.exitValue());
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
           log("getProp: Exception: "+e);
        }
        return null;
    }

    private String int2ip(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    private String getIpV6() {
        int loop1=0, loop2=0;
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            //Log.e(TAG, "getIpV6: NetworkInterface "+en.hasMoreElements());
            while(en.hasMoreElements()) {
                NetworkInterface intf = en.nextElement();
                Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                //Logger.e(TAG, "getIpV6: InetAddress "+enumIpAddr.hasMoreElements());
                while(enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    //Logger.e(TAG, "getIpV6: "+String.format("(%d,%d) isloopback: ", loop1, loop2)+inetAddress.isLoopbackAddress());
                    loop2++;
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
                loop1++;
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private String getCurrentNetworkType() {
        int networkClass = getNetworkClass(mContext);
        String type = "uknown";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "";
                break;
            case NETWORK_CLASS_WIFI:
                type = "wifi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2g";
                break;
            case NETWORK_CLASS_3_G:
                type = "3g";
                break;
            case NETWORK_CLASS_4_G:
                type = "4g";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "uknown";
                break;
        }
        return type;
    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    private static int getNetworkClass(Context context) {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager)context
                    .getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            if (network != null && network.isAvailable()
                    && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(
                            Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }
    private PlatformInfo initTestClient(){
        try {
            PlatformInfo c = new PlatformInfo();
            //c.setCameraInfo("1:"+CameraUtil.getCameraPixels(CameraUtil.HasFrontCamera())+":2:"+CameraUtil.getCameraPixels(CameraUtil.HasBackCamera()));
            c.setCpuInfo(getCpuInfo());
            c.setMemInfo(getMemInfo());
            c.setLcdInfo(getScreenResolution(false));
            c.setGsensorInfo(getSensorInfo(Sensor.TYPE_ACCELEROMETER));
            c.setMsensorInfo(getSensorInfo(Sensor.TYPE_MAGNETIC_FIELD));
            c.setLightInfo(getSensorInfo(Sensor.TYPE_LIGHT));
            c.setDsensorInfo(getSensorInfo(Sensor.TYPE_ROTATION_VECTOR));
            c.setGyroInfo(getSensorInfo(Sensor.TYPE_GYROSCOPE));
            c.setProximityInfo(getSensorInfo(Sensor.TYPE_PROXIMITY));
            String gl_render=getString("gl_renderer", null);
            if(gl_render != null) {
                c.setGpuInfo(gl_render +":" + getString("gl_vendor",null) + ":" + getString("gl_version",null));//+":"+gl_extensions);
            }

            c.setUa(mUa);
            c.setAid(getAid());
            c.setResolution(getScreenResolution(true));
            try {
                DisplayMetrics dm = new DisplayMetrics();
                Display display = ((Activity)mContext).getWindowManager().getDefaultDisplay();
                display.getMetrics(dm);
                c.setScreenWidth((short) dm.widthPixels);
                c.setScreenHeight((short) dm.heightPixels);
            }catch (Exception e){}

            c.setMem(memTotal);
            c.setMemAvailsize(memAvail);
            c.setRom(getTotalRomSize());
            long[] romBlock = getRomBlockSize();
            if(romBlock != null && romBlock.length>=3) {
                c.setRomBlocksize((int) romBlock[0]);
                c.setRomTotalblocks((int) romBlock[1]);
                c.setRomAvailblocks((int) romBlock[2]);
            }
            long[] sdBlock = getsdBlockSize();
            if(sdBlock != null && sdBlock.length>=3) {
                c.setSdBlocksize((int) sdBlock[0]);
                c.setSdTotalblocks((int) sdBlock[1]);
                c.setSdAvailblocks((int) sdBlock[2]);
            }

            c.setImei(getImei());
            c.setWmac(getWifiMacAddr());
            c.setBdevice(Build.DEVICE);
            c.setBboard(Build.BOARD);
            c.setBbootloader(Build.BOOTLOADER);
            c.setBbrand(Build.BRAND);
            c.setBmodel(Build.MODEL);
            c.setBdisplay(Build.DISPLAY);
            c.setBfingerprint(Build.FINGERPRINT);
            c.setBhardware(Build.HARDWARE);
            c.setBhost(Build.HOST);
            c.setBid(Build.ID);
            c.setBmanufacture(Build.MANUFACTURER);
            c.setBproduct(Build.PRODUCT);
            c.setBradio(Build.getRadioVersion());
            c.setBserial(Build.SERIAL);
            c.setBuser(Build.USER);
            c.setBtags(Build.TAGS);
            c.setBtime(String.valueOf(Build.TIME));
            c.setBtype(Build.TYPE);
            c.setBvCode(Build.VERSION.CODENAME);
            c.setBvInc(Build.VERSION.INCREMENTAL);
            c.setBvRel(Build.VERSION.RELEASE);
            c.setBvSdkInt(String.valueOf(Build.VERSION.SDK_INT));

            c.setUsbManufacture(exec("cat /sys/class/android_usb/android0/iManufacturer"));
            c.setUsbProduct(exec("cat /sys/class/android_usb/android0/iProduct"));
            c.setUsbSerial(exec("cat /sys/class/android_usb/android0/iSerial"));
            c.setUsbIdProduct(exec("cat /sys/class/android_usb/android0/idProduct"));
            c.setUsbIdVendor(exec("cat /sys/class/android_usb/android0/idVendor"));

            c.setChannel("laobai");
            c.setVersionName(mVersionName);
            c.setVersionCode(mVersionCode);

            try {
                String custKey = getMD5String(c.getBfingerprint() + c.getBproduct() + c.getBmanufacture() + c.getBhardware()
                        + c.getBdevice() + c.getBmodel() + c.getBvCode() + c.getBvSdkInt());

                //String oldKey = getSharedPreferences("default", 0).getString("cust_key", "");

                //if(custKey.equals(oldKey)){
                //	return null;
                //}

                c.setCustKey(custKey);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(c.getCustKey()==null || c.getCustKey().length()==0){
                log("get cust_key failed.");
                return null;
            }

            Log.e(TAG, "cust_key="+c.getCustKey());
            try {
                WifiManager mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = (null == mWifiManager ? null : mWifiManager.getConnectionInfo());
                if (info != null) {
                    c.setEssid(info.getSSID());
                    c.setBssid(info.getBSSID());
                    c.setIpv4(int2ip(info.getIpAddress()));
                    c.setIpv6(getIpV6());
                }
            }catch (Exception e){}
            try{
                // String IMSI =android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMSI);
                TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);//取得相关系统服务
                if(tm != null){
                    c.setSimImsi(tm.getSubscriberId());
                    c.setSimSerial(tm.getSimSerialNumber());
                    c.setSimNumber(tm.getLine1Number());
                    c.setSimOperator(tm.getSimOperator());
                    c.setSimOperatorName(tm.getSimOperatorName());
                    c.setSimCountryIso(tm.getSimCountryIso());
                    c.setSimState(tm.getSimState());


                    List<CellInfo> l = tm.getAllCellInfo();
                    if(l!= null){
                        String s = "";
                        for(CellInfo ce: l){
	    	    			/* protected static final int TYPE_GSM = 1;
							    protected static final int TYPE_CDMA = 2;
							    protected static final int TYPE_LTE = 3;
							    protected static final int TYPE_WCDMA = 4;
	    	    			 */

                            if(ce instanceof CellInfoGsm){
                                //1:lac:cid:psc:mcc:mnc
                                CellInfoGsm i = (CellInfoGsm)ce;
                                if(i != null){
                                    CellIdentityGsm g = i.getCellIdentity();
                                    if(g != null){

                                        s+=String.format("1:%d:%d:%d:%d:%d/", g.getLac(), g.getCid(),g.getPsc(),
                                                g.getMcc(),g.getMnc());
                                    }
                                }
                            }else if(ce instanceof CellInfoCdma){
                                //2:network_id:bs_id:sys_id:lat:lng
                                CellInfoCdma i = (CellInfoCdma)ce;
                                if(i != null){
                                    CellIdentityCdma g = i.getCellIdentity();
                                    if(g != null){
                                        s+=String.format("2:%d:%d:%d:%d:%d/", g.getNetworkId(), g.getBasestationId(),
                                                g.getSystemId(), g.getLatitude(), g.getLongitude());
                                    }
                                }
                            }else if(ce instanceof CellInfoLte){
                                //3:tac:ci:pci:mcc:mnc
                                CellInfoLte i = (CellInfoLte)ce;
                                if(i != null){
                                    CellIdentityLte g = i.getCellIdentity();
                                    if(g != null){
                                        s+=String.format("3:%d:%d:%d:%d:%d/", g.getTac(), g.getCi(), g.getPci(),
                                                g.getMcc(),g.getMnc());
                                    }
                                }
                            }/*else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT &&  ce instanceof CellInfoWcdma){
	    	    				//4:lac:cid:psc:mcc:mnc
	    	    				CellInfoWcdma i = (CellInfoWcdma)ce;
	    	    				if(i != null){
		    	    				CellIdentityWcdma g = i.getCellIdentity();
		    	    				if(g != null){
		    	    					s+=String.format("4:%d:%d:%d:%d:%d/", g.getLac(), g.getCid(),g.getPsc(),
		    	    							g.getMcc(),g.getMnc());
		    	    				}
	    	    				}
	    	    			}*/
                        }
                        if(s!=null&&s.length()>0){
                            c.setTelAllCellinfo(s);
                        }
                    }
                    CellLocation loc = tm.getCellLocation();
                    if(loc != null){
                        if(loc instanceof GsmCellLocation){
                            //1:lac:cid:psc
                            GsmCellLocation g= (GsmCellLocation)loc;
                            c.setTelCellinfo(String.format("1:%d:%d:%d", g.getLac(), g.getCid(),
                                    g.getPsc()));
                        }else if(loc instanceof CdmaCellLocation){
                            //2:network_id:bs_id:sys_id:lat:lng
                            CdmaCellLocation g= (CdmaCellLocation)loc;
                            c.setTelCellinfo(String.format("2:%d:%d:%d:%d:%d", g.getNetworkId(), g.getBaseStationId(),
                                    g.getSystemId(), g.getBaseStationLatitude(), g.getBaseStationLongitude()));
                        }

                    }
                    c.setTelCountryIso(tm.getNetworkCountryIso());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        c.setTelHasCarrierPri(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT?(byte)(tm.hasCarrierPrivileges()?1:0):0);
                    }
                    c.setTelHasIcccard((byte)(tm.hasIccCard()?1:0));
                    c.setTelIsRoaming((byte)(tm.isNetworkRoaming()?1:0));
                    c.setTelLine1Number(tm.getLine1Number());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        c.setTelMmsUa(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT?tm.getMmsUserAgent():null);
                        c.setTelMmsUaProfurl(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT?tm.getMmsUAProfUrl():null);
                    }
                    List<NeighboringCellInfo> ll = tm.getNeighboringCellInfo();
                    if(ll != null){
                        String s = "";
                        for(NeighboringCellInfo i: ll){
                            // type:lac:cid:psc:rssi
                            s+=String.format("%d:%d:%d:%d/", i.getNetworkType(),i.getLac(), i.getCid(), i.getPsc(), i.getRssi());
                        }
                        if(s!=null&&s.length()>0){
                            c.setTelNeighboringCellinfo(s);
                        }
                    }
                    c.setTelNetworkType(tm.getNetworkType());
                    c.setTelOperator(tm.getNetworkOperator());
                    c.setTelOperatorName(tm.getNetworkOperatorName());
                    c.setTelPhoneType(tm.getPhoneType());
                    //c.setTelPhoneCount(tm.getPhoneCount()); // android 6.0
                    c.setTelVmAlphatag(tm.getVoiceMailAlphaTag());
                    c.setTelVmNumber(tm.getVoiceMailNumber());
                    //c.setTelVoiceNetworkType(tm.getVoiceNetworkType());// android 6.0
                    c.setTelDeviceId(tm.getDeviceId());
                    c.setTelDeviceVersion(tm.getDeviceSoftwareVersion());
                }

                c.setNetwork(getCurrentNetworkType());


            }catch (Exception e){
               log("initTestClient: Exception2 "+e);
                e.printStackTrace();
            }

            return c;
        }catch(Exception  e){
           log("initTestClient: Exception "+e);
            e.printStackTrace();
            return null;
        }
    }

    public interface ProgressHandler
    {
        public void updateSize(String fileName, long size);
    }
    public interface ResponseHandler
    {
        public boolean onSuccess(String response);
        public boolean onFailure(int statusCode, String response);
    }

    private boolean testClientRegister(String cust_key, Map<File, String> files, ProgressHandler handler) {
        // TODO Auto-generated method stub
        final int TIMEOUIT = 300 * 1000; // 超时时间 5m
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";

        DataOutputStream outStream = null;
        InputStream is = null;

        try {
            URL url = new URL("http://120.26.129.37:8010/misc/register_platform");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(TIMEOUIT);
            conn.setReadTimeout(TIMEOUIT); // 缓存的最长时间
            conn.setDoInput(true);// 允许输入
            conn.setDoOutput(true);// 允许输出
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                    + ";boundary=" + BOUNDARY);
            // http://www.tuicool.com/articles/7FrMVf, incase java.lang.EOFException
            // http://stackoverflow.com/questions/12319194/android-httpurlconnection-throwing-eofexception
            if (Build.VERSION.SDK_INT > 13) {
                conn.setRequestProperty("Connection", "Close");
            }else {
                conn.setRequestProperty("Connection", "Keep-Alive");
            }
            // 首先组拼文本类型的参数
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("cust_key", cust_key);
            params.put("network", getCurrentNetworkType());
            if(mLanguage != null) {
                params.put("language", mLanguage);
            }
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if(entry.getValue()==null){
                    continue; // null will cause 400 error where request param is required true or false
                }
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINEND);
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"" + LINEND);
                sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                sb.append(LINEND);
                sb.append(entry.getValue());
                sb.append(LINEND);
            }
            outStream = new DataOutputStream(conn.getOutputStream());
            outStream.write(sb.toString().getBytes());
            //write file
            byte[] buf = new byte[4*1024];
            for (Map.Entry<File, String> entry : files.entrySet()) {
                int rbytes = 0;
                long written = 0;
                File f = entry.getKey();
                String fname = entry.getValue();
                outStream.writeBytes(PREFIX + BOUNDARY + LINEND);
                outStream.writeBytes("Content-Disposition: form-data; "
                        + "name=\""+fname+"\";filename=\""+f.getName()+"\""
                        + LINEND);
                outStream.writeBytes("Content-Type: application/octet-stream"+ LINEND);
                outStream.writeBytes("Content-Transfer-Encoding: binary" + LINEND);
                outStream.writeBytes(LINEND);
                FileInputStream fis = new FileInputStream(f);
                while ((rbytes = fis.read(buf)) != -1) {
                    outStream.write(buf, 0, rbytes);
                    written += rbytes;
                    //同步更新数据
                    if(handler != null) {
                        handler.updateSize(fname, written);
                    }
                }
                outStream.writeBytes(LINEND);
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            // 得到响应码
            int res = conn.getResponseCode(); // java.io.EOFException offen occurs here
            is = conn.getInputStream();
            if(res == HttpURLConnection.HTTP_OK) {
                StringBuffer b = new StringBuffer();
                int rbytes = 0;
                while ((rbytes = is.read(buf)) > 0) {  /* != -1 */
                    b.append(new String(buf, 0, rbytes));
                }
                outStream.close();
                is.close();

                try {
                    JSONObject response = new JSONObject(b.toString());
                    String errorNo = response.optString("errorNo");
                   log("testClientRegister: response erroNo=" + errorNo);
                    if (errorNo.equals("0")) {
                        return true;
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                   log("testClientRegister: Exception2: "+e);
                }
                return false;
            }
        } catch (Exception e) {
           log("testClientRegister: Exception: "+e);
            e.printStackTrace();
        }
        if(outStream != null){
            try {
                outStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        if(is != null){
            try {
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
            }
        }
        return false;
    }

    private static class JSONHelper {
        private static String TAG = "JSONHelper";

        /**
         * 将对象转换成Json字符串
         * @param obj
         * @return json类型字符串
         */
        public static String toJSON(Object obj) {
            JSONStringer js = new JSONStringer();
            serialize(js, obj);
            return js.toString();
        }

        /**
         * 序列化为JSON
         * @param js json对象
         * @param o 待需序列化的对象
         */
        private static void serialize(JSONStringer js, Object o) {
            if (isNull(o)) {
                try {
                    js.value(null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }

            Class<?> clazz = o.getClass();
            if (isObject(clazz)) { // 对象
                serializeObject(js, o);
            } else if (isArray(clazz)) { // 数组
                serializeArray(js, o);
            } else if (isCollection(clazz)) { // 集合
                Collection<?> collection = (Collection<?>) o;
                serializeCollect(js, collection);
            }else if (isMap(clazz)) { // 集合
                HashMap<?,?> collection = (HashMap<?,?>) o;
                serializeMap(js, collection);
            } else { // 单个值
                try {
                    js.value(o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 序列化数组
         * @param js    json对象
         * @param array 数组
         */
        private static void serializeArray(JSONStringer js, Object array) {
            try {
                js.array();
                for (int i = 0; i < Array.getLength(array); ++i) {
                    Object o = Array.get(array, i);
                    serialize(js, o);
                }
                js.endArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 序列化集合
         * @param js    json对象
         * @param collection    集合
         */
        private static void serializeCollect(JSONStringer js, Collection<?> collection) {
            try {
                js.array();
                for (Object o : collection) {
                    serialize(js, o);
                }
                js.endArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 序列化Map
         * @param js    json对象
         * @param map   map对象
         */
        private static void serializeMap(JSONStringer js, Map<?,?> map) {
            try {
                js.object();
                @SuppressWarnings("unchecked")
                Map<String, Object> valueMap = (Map<String, Object>) map;
                Iterator<Map.Entry<String, Object>> it = valueMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
                    js.key(entry.getKey());
                    serialize(js,entry.getValue());
                }
                js.endObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 序列化对象
         * @param js    json对象
         * @param obj   待序列化对象
         */
        private static void serializeObject(JSONStringer js, Object obj) {
            try {
                js.object();
                Class<? extends Object> objClazz = obj.getClass();
                Method[] methods = objClazz.getDeclaredMethods();
                Field[] fields = objClazz.getDeclaredFields();
                for (Field field : fields) {
                    try {
                        String fieldType = field.getType().getSimpleName();
                        String fieldGetName = parseMethodName(field.getName(),"get");
                        if (!haveMethod(methods, fieldGetName)) {
                            continue;
                        }
                        Method fieldGetMet = objClazz.getMethod(fieldGetName, new Class[] {});
                        Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
                        String result = null;
                        if ("Date".equals(fieldType)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                            result = sdf.format((Date)fieldVal);

                        } else {
                            if (null != fieldVal) {
                                result = String.valueOf(fieldVal);
                            }
                        }
                        js.key(field.getName());
                        serialize(js, result);
                    } catch (Exception e) {
                        continue;
                    }
                }
                js.endObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断是否存在某属性的 get方法
         * @param methods   引用方法的数组
         * @param fieldMethod   方法名称
         * @return true或者false
         */
        public static boolean haveMethod(Method[] methods, String fieldMethod) {
            for (Method met : methods) {
                if (fieldMethod.equals(met.getName())) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 拼接某属性的 get或者set方法
         * @param fieldName 字段名称
         * @param methodType    方法类型
         * @return 方法名称
         */
        public static String parseMethodName(String fieldName,String methodType) {
            if (null == fieldName || "".equals(fieldName)) {
                return null;
            }
            return methodType + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }

        /**
         * 给字段赋值
         * @param obj  实例对象
         * @param valMap  值集合
         */
        public static void setFieldValue(Object obj, Map<String, String> valMap) {
            Class<?> cls = obj.getClass();
            // 取出bean里的所有方法
            Method[] methods = cls.getDeclaredMethods();
            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {
                try {
                    String setMetodName = parseMethodName(field.getName(),"set");
                    if (!haveMethod(methods, setMetodName)) {
                        continue;
                    }
                    Method fieldMethod = cls.getMethod(setMetodName, field
                            .getType());
                    String value = valMap.get(field.getName());
                    if (null != value && !"".equals(value)) {
                        String fieldType = field.getType().getSimpleName();
                        if ("String".equals(fieldType)) {
                            fieldMethod.invoke(obj, value);
                        } else if ("Date".equals(fieldType)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
                            Date temp = sdf.parse(value);
                            fieldMethod.invoke(obj, temp);
                        } else if ("Integer".equals(fieldType)
                                || "int".equals(fieldType)) {
                            Integer intval = Integer.parseInt(value);
                            fieldMethod.invoke(obj, intval);
                        } else if ("Long".equalsIgnoreCase(fieldType)) {
                            Long temp = Long.parseLong(value);
                            fieldMethod.invoke(obj, temp);
                        } else if ("Double".equalsIgnoreCase(fieldType)) {
                            Double temp = Double.parseDouble(value);
                            fieldMethod.invoke(obj, temp);
                        } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                            Boolean temp = Boolean.parseBoolean(value);
                            fieldMethod.invoke(obj, temp);
                        } else {
                            System.out.println("setFieldValue not supper type:" + fieldType);
                        }
                    }
                } catch (Exception e) {
                    continue;
                }
            }

        }

        /**
         * bean对象转Map
         * @param obj   实例对象
         * @return  map集合
         */
        public static Map<String, String> beanToMap(Object obj) {
            Class<?> cls = obj.getClass();
            Map<String, String> valueMap = new HashMap<String, String>();
            // 取出bean里的所有方法
            Method[] methods = cls.getDeclaredMethods();
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                try {
                    String fieldType = field.getType().getSimpleName();
                    String fieldGetName = parseMethodName(field.getName(),"get");
                    if (!haveMethod(methods, fieldGetName)) {
                        continue;
                    }
                    Method fieldGetMet = cls.getMethod(fieldGetName, new Class[] {});
                    Object fieldVal = fieldGetMet.invoke(obj, new Object[] {});
                    String result = null;
                    if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                        result = sdf.format((Date)fieldVal);

                    } else {
                        if (null != fieldVal) {
                            result = String.valueOf(fieldVal);
                        }
                    }
                    valueMap.put(field.getName(), result);
                } catch (Exception e) {
                    continue;
                }
            }
            return valueMap;

        }

        /**
         * 给对象的字段赋值
         * @param obj   类实例
         * @param fieldSetMethod    字段方法
         * @param fieldType 字段类型
         * @param value
         */
        public static void setFiedlValue(Object obj,Method fieldSetMethod,String fieldType,Object value){

            try {
                if (null != value && !"".equals(value)) {
                    if ("String".equals(fieldType)) {
                        fieldSetMethod.invoke(obj, value.toString());
                    } else if ("Date".equals(fieldType)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
                        Date temp = sdf.parse(value.toString());
                        fieldSetMethod.invoke(obj, temp);
                    } else if ("Integer".equals(fieldType)
                            || "int".equals(fieldType)) {
                        Integer intval = Integer.parseInt(value.toString());
                        fieldSetMethod.invoke(obj, intval);
                    } else if ("Long".equalsIgnoreCase(fieldType)) {
                        Long temp = Long.parseLong(value.toString());
                        fieldSetMethod.invoke(obj, temp);
                    } else if ("Double".equalsIgnoreCase(fieldType)) {
                        Double temp = Double.parseDouble(value.toString());
                        fieldSetMethod.invoke(obj, temp);
                    } else if ("Boolean".equalsIgnoreCase(fieldType)) {
                        Boolean temp = Boolean.parseBoolean(value.toString());
                        fieldSetMethod.invoke(obj, temp);
                    } else {
                        fieldSetMethod.invoke(obj, value);
                       Log.e(TAG, ">>>>setFiedlValue -> not supper type" + fieldType);
                    }
                }

            } catch (Exception e) {
//           log(TAG  + ">>>>>>>>>>set value error.",e);
                e.printStackTrace();
            }

        }

        /**
         * 反序列化简单对象
         * @param jo    json对象
         * @param clazz 实体类类型
         * @return  反序列化后的实例
         * @throws JSONException
         */
        public static <T> T parseObject(JSONObject jo, Class<T> clazz) throws JSONException {
            if (clazz == null || isNull(jo)) {
                return null;
            }

            T obj = newInstance(clazz);
            if (obj == null) {
                return null;
            }
            if(isMap(clazz)){
                setField(obj,jo);
            }else{
                // 取出bean里的所有方法
                Method[] methods = clazz.getDeclaredMethods();
                Field[] fields = clazz.getDeclaredFields();
                for (Field f : fields) {
                    String setMetodName = parseMethodName(f.getName(),"set");
                    if (!haveMethod(methods, setMetodName)) {
                        continue;
                    }
                    try {
                        Method fieldMethod = clazz.getMethod(setMetodName, f.getType());
                        setField(obj,fieldMethod,f, jo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return obj;
        }

        /**
         * 反序列化简单对象
         * @param jsonStr   json字符串
         * @param clazz 实体类类型
         * @return  反序列化后的实例
         * @throws JSONException
         */
        public static <T> T parseObject(String jsonStr, Class<T> clazz) throws JSONException {
            if (clazz == null || jsonStr == null || jsonStr.length() < 10) {
                return null;
            }

            JSONObject jo = null;
            jo = new JSONObject(jsonStr);
            if (isNull(jo)) {
                return null;
            }

            return parseObject(jo, clazz);
        }

        /**
         * 反序列化数组对象
         * @param ja    json数组
         * @param clazz 实体类类型
         * @return  反序列化后的数组
         */
        public static <T> T[] parseArray(JSONArray ja, Class<T> clazz) {
            if (clazz == null || isNull(ja)) {
                return null;
            }

            int len = ja.length();

            @SuppressWarnings("unchecked")
            T[] array = (T[]) Array.newInstance(clazz, len);

            for (int i = 0; i < len; ++i) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    T o = parseObject(jo, clazz);
                    array[i] = o;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return array;
        }


        /**
         * 反序列化数组对象
         * @param jsonStr   json字符串
         * @param clazz 实体类类型
         * @return  序列化后的数组
         */
        public static <T> T[] parseArray(String jsonStr, Class<T> clazz) {
            if (clazz == null || jsonStr == null || jsonStr.length() == 0) {
                return null;
            }
            JSONArray jo = null;
            try {
                jo = new JSONArray(jsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isNull(jo)) {
                return null;
            }

            return parseArray(jo, clazz);
        }

        /**
         * 反序列化泛型集合
         * @param ja    json数组
         * @param collectionClazz   集合类型
         * @param genericType   实体类类型
         * @return
         * @throws JSONException
         */
        @SuppressWarnings("unchecked")
        public static <T> Collection<T> parseCollection(JSONArray ja, Class<?> collectionClazz,
                                                        Class<T> genericType) throws JSONException {

            if (collectionClazz == null || genericType == null || isNull(ja)) {
                return null;
            }

            Collection<T> collection = (Collection<T>) newInstance(collectionClazz);

            for (int i = 0; i < ja.length(); ++i) {
                try {
                    JSONObject jo = ja.getJSONObject(i);
                    T o = parseObject(jo, genericType);
                    collection.add(o);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return collection;
        }

        /**
         * 反序列化泛型集合
         * @param jsonStr   json字符串
         * @param collectionClazz   集合类型
         * @param genericType   实体类类型
         * @return  反序列化后的数组
         * @throws JSONException
         */
        public static <T> Collection<T> parseCollection(String jsonStr, Class<?> collectionClazz,
                                                        Class<T> genericType) throws JSONException {
            if (collectionClazz == null || genericType == null || jsonStr == null
                    || jsonStr.length() == 0) {
                return null;
            }
            JSONArray jo = null;
            try {
                //如果为数组，则此处转化时，需要去掉前面的键，直接后面的[]中的值
                int index = jsonStr.indexOf("[");
                String arrayString=null;

                //获取数组的字符串
                if(-1!=index){
                    arrayString = jsonStr.substring(index);
                }

                //如果为数组，使用数组转化
                if(null!=arrayString){
                    jo = new JSONArray(arrayString);
                }
                else{
                    jo = new JSONArray(jsonStr);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (isNull(jo)) {
                return null;
            }

            return parseCollection(jo, collectionClazz, genericType);
        }

        /**
         * 根据类型创建对象
         * @param clazz 待创建实例的类型
         * @return  实例对象
         * @throws JSONException
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static <T> T newInstance(Class<T> clazz) throws JSONException {
            if (clazz == null)
                return null;
            T obj = null;
            if (clazz.isInterface()) {
                if (clazz.equals(Map.class)) {
                    obj = (T) new HashMap();
                }else if (clazz.equals(List.class)) {
                    obj = (T) new ArrayList();
                }else if (clazz.equals(Set.class)) {
                    obj = (T) new HashSet();
                }else{
                    throw new JSONException("unknown interface: " + clazz);
                }
            }else{
                try {
                    obj = clazz.newInstance();
                }catch (Exception e) {
                    throw new JSONException("unknown class type: " + clazz);
                }
            }
            return obj;
        }

        /**
         * 设定Map的值
         * @param obj   待赋值字段的对象
         * @param jo    json实例
         */
        private static void setField(Object obj, JSONObject jo) {
            try {
                @SuppressWarnings("unchecked")
                Iterator<String> keyIter = jo.keys();
                String key;
                Object value;
                @SuppressWarnings("unchecked")
                Map<String, Object> valueMap = (Map<String, Object>) obj;
                while (keyIter.hasNext()) {
                    key = (String) keyIter.next();
                    value = jo.get(key);
                    valueMap.put(key, value);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /**
         * 设定字段的值
         * @param obj   待赋值字段的对象
         * @param fieldSetMethod    字段方法名
         * @param field 字段
         * @param jo    json实例
         */
        private static void setField(Object obj, Method fieldSetMethod,Field field, JSONObject jo) {
            String name = field.getName();
            Class<?> clazz = field.getType();
            try {
                if (isArray(clazz)) { // 数组
                    Class<?> c = clazz.getComponentType();
                    JSONArray ja = jo.optJSONArray(name);
                    if (!isNull(ja)) {
                        Object array = parseArray(ja, c);
                        setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), array);
                    }
                } else if (isCollection(clazz)) { // 泛型集合
                    // 获取定义的泛型类型
                    Class<?> c = null;
                    Type gType = field.getGenericType();
                    if (gType instanceof ParameterizedType) {
                        ParameterizedType ptype = (ParameterizedType) gType;
                        Type[] targs = ptype.getActualTypeArguments();
                        if (targs != null && targs.length > 0) {
                            Type t = targs[0];
                            c = (Class<?>) t;
                        }
                    }

                    JSONArray ja = jo.optJSONArray(name);
                    if (!isNull(ja)) {
                        Object o = parseCollection(ja, clazz, c);
                        setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                    }
                } else if (isSingle(clazz)) { // 值类型
                    Object o = jo.opt(name);
                    if (o != null) {
                        setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                    }
                } else if (isObject(clazz)) { // 对象
                    JSONObject j = jo.optJSONObject(name);
                    if (!isNull(j)) {
                        Object o = parseObject(j, clazz);
                        setFiedlValue(obj, fieldSetMethod, clazz.getSimpleName(), o);
                    }
                } else if (isList(clazz)) { // 列表
//              JSONObject j = jo.optJSONObject(name);
//              if (!isNull(j)) {
//                  Object o = parseObject(j, clazz);
//                  f.set(obj, o);
//              }
                } else {
                    throw new Exception("unknow type!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 设定字段的值
         * @param obj   待赋值字段的对象
         * @param field 字段
         * @param jo    json实例
         */
        @SuppressWarnings("unused")
        private static void setField(Object obj, Field field, JSONObject jo) {
            String name = field.getName();
            Class<?> clazz = field.getType();
            try {
                if (isArray(clazz)) { // 数组
                    Class<?> c = clazz.getComponentType();
                    JSONArray ja = jo.optJSONArray(name);
                    if (!isNull(ja)) {
                        Object array = parseArray(ja, c);
                        field.set(obj, array);
                    }
                } else if (isCollection(clazz)) { // 泛型集合
                    // 获取定义的泛型类型
                    Class<?> c = null;
                    Type gType = field.getGenericType();
                    if (gType instanceof ParameterizedType) {
                        ParameterizedType ptype = (ParameterizedType) gType;
                        Type[] targs = ptype.getActualTypeArguments();
                        if (targs != null && targs.length > 0) {
                            Type t = targs[0];
                            c = (Class<?>) t;
                        }
                    }
                    JSONArray ja = jo.optJSONArray(name);
                    if (!isNull(ja)) {
                        Object o = parseCollection(ja, clazz, c);
                        field.set(obj, o);
                    }
                } else if (isSingle(clazz)) { // 值类型
                    Object o = jo.opt(name);
                    if (o != null) {
                        field.set(obj, o);
                    }
                } else if (isObject(clazz)) { // 对象
                    JSONObject j = jo.optJSONObject(name);
                    if (!isNull(j)) {
                        Object o = parseObject(j, clazz);
                        field.set(obj, o);
                    }
                } else if (isList(clazz)) { // 列表
                    JSONObject j = jo.optJSONObject(name);
                    if (!isNull(j)) {
                        Object o = parseObject(j, clazz);
                        field.set(obj, o);
                    }
                }else {
                    throw new Exception("unknow type!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * 判断对象是否为空
         * @param obj   实例
         * @return
         */
        private static boolean isNull(Object obj) {
            if (obj instanceof JSONObject) {
                return JSONObject.NULL.equals(obj);
            }
            return obj == null;
        }

        /**
         * 判断是否是值类型
         * @param clazz
         * @return
         */
        private static boolean isSingle(Class<?> clazz) {
            return isBoolean(clazz) || isNumber(clazz) || isString(clazz);
        }

        /**
         * 是否布尔值
         * @param clazz
         * @return
         */
        public static boolean isBoolean(Class<?> clazz) {
            return (clazz != null)
                    && ((Boolean.TYPE.isAssignableFrom(clazz)) || (Boolean.class
                    .isAssignableFrom(clazz)));
        }

        /**
         * 是否数值
         * @param clazz
         * @return
         */
        public static boolean isNumber(Class<?> clazz) {
            return (clazz != null)
                    && ((Byte.TYPE.isAssignableFrom(clazz)) || (Short.TYPE.isAssignableFrom(clazz))
                    || (Integer.TYPE.isAssignableFrom(clazz))
                    || (Long.TYPE.isAssignableFrom(clazz))
                    || (Float.TYPE.isAssignableFrom(clazz))
                    || (Double.TYPE.isAssignableFrom(clazz)) || (Number.class
                    .isAssignableFrom(clazz)));
        }

        /**
         * 判断是否是字符串
         * @param clazz
         * @return
         */
        public static boolean isString(Class<?> clazz) {
            return (clazz != null)
                    && ((String.class.isAssignableFrom(clazz))
                    || (Character.TYPE.isAssignableFrom(clazz)) || (Character.class
                    .isAssignableFrom(clazz)));
        }

        /**
         * 判断是否是对象
         * @param clazz
         * @return
         */
        private static boolean isObject(Class<?> clazz) {
            return clazz != null && !isSingle(clazz) && !isArray(clazz) && !isCollection(clazz) && !isMap(clazz);
        }

        /**
         * 判断是否是数组
         * @param clazz
         * @return
         */
        public static boolean isArray(Class<?> clazz) {
            return clazz != null && clazz.isArray();
        }

        /**
         * 判断是否是集合
         * @param clazz
         * @return
         */
        public static boolean isCollection(Class<?> clazz) {
            return clazz != null && Collection.class.isAssignableFrom(clazz);
        }

        /**
         * 判断是否是Map
         * @param clazz
         * @return
         */
        public static boolean isMap(Class<?> clazz) {
            return clazz != null && Map.class.isAssignableFrom(clazz);
        }

        /**
         * 判断是否是列表
         * @param clazz
         * @return
         */
        public static boolean isList(Class<?> clazz) {
            return clazz != null && List.class.isAssignableFrom(clazz);
        }

    }

    public static boolean saveTxtFile(String filePath, String dec) {
        // TODO Auto-generated method stub
        if(dec==null||dec.length()==0) return false;
        try {
            File file = new File(filePath);
            File dir = new File(file.getParent());
            if(!dir.exists()) dir.mkdirs();
            file.createNewFile();
            OutputStreamWriter out = new OutputStreamWriter(
                    new FileOutputStream(file),"utf8");//考虑到编码格式
            out.write(dec,0,dec.length());
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    public static List<String> ReadTxtLines(String strFilePath, int max_limit)
    {
        List<String> list = new ArrayList<String>();
        File f = new File(strFilePath);

        if (f.exists() && !f.isDirectory())
        {
            try {
                InputStream instream = new FileInputStream(f);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    int readSize=0;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        list.add(line);
                        readSize += line.length();
                        if(max_limit>0 && readSize>max_limit){
                            list.add("...(more)...");
                            break;
                        }
                    }
                    instream.close();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e(TAG, "ReadTxtFile Exception: "+e);
            }
        }
        return list;
    }

    public ArrayList<String> getAppInfo(){
        ArrayList<String> list = new ArrayList<String>();
        try {
            PackageManager pm = mContext.getPackageManager(); // 获得PackageManager对象

            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 通过查询，获得所有ResolveInfo对象.
            List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
            // 调用系统排序 ， 根据name排序
            // 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
            Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
            Log.i("ttt", "getAppInfo: -------");
            List<PackageInfo> PackageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
            for(PackageInfo packageInfo:PackageInfos){
                String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
                StringBuffer packages = new StringBuffer();
                if(appName!=null){
                   packages.append("appName="+appName);
                }
                if(packageInfo.versionName!=null){
                    packages.append(";PI_versionName="+packageInfo.versionName);
                }
                if(packageInfo.packageName!=null){
                    packages.append(";PI_packageName="+packageInfo.packageName);
                }
                if(packageInfo.sharedUserId!=null){
                    packages.append(";PI_sharedUserId="+packageInfo.sharedUserId);
                }
                try {
                    packages.append(";PI_baseRevisionCode="+packageInfo.baseRevisionCode);
                } catch (Error e) {
                    e.printStackTrace();
                }
                try {
                    packages.append(";PI_installLocation="+packageInfo.installLocation);
                } catch (Error e) {
                    e.printStackTrace();
                }
                packages.append(";PI_versionCode="+packageInfo.versionCode+
                        ";PI_firstInstallTime="+packageInfo.firstInstallTime+
                        ";PI_lastUpdateTime="+packageInfo.lastUpdateTime+
                        ";PI_sharedUserLabel="+packageInfo.sharedUserLabel
                        );
                int[] requestedPermissionsFlags = packageInfo.requestedPermissionsFlags;
                if(packageInfo.requestedPermissionsFlags!=null){
                    for (int i = 0; i < requestedPermissionsFlags.length; i++) {
                        packages.append(";PI_rpf_"+i+"_requestedPermissionsFlags="+requestedPermissionsFlags[i]
                        );
                    }
                }else {
                    Log.i("ttt", "packageInfo.requestedPermissionsFlags==null");
                }
                InstrumentationInfo[] instrumentationInfo = packageInfo.instrumentation;
                if(packageInfo.instrumentation!=null){
                    for (int i = 0; i < instrumentationInfo.length; i++) {
                        if(instrumentationInfo[i].packageName!=null){
                            packages.append(";PI_ii_"+i+"_packageName="+instrumentationInfo[i].packageName);
                        }
                        if(instrumentationInfo[i].name!=null){
                            packages.append(";PI_ii_"+i+"_name="+instrumentationInfo[i].name);
                        }
                        if(instrumentationInfo[i].publicSourceDir!=null){
                            packages.append(";PI_ii_"+i+"_publicSourceDir="+instrumentationInfo[i].publicSourceDir);
                        }
                        if(instrumentationInfo[i].dataDir!=null){
                            packages.append(";PI_ii_"+i+"_dataDir="+instrumentationInfo[i].dataDir);
                        }
                        if(instrumentationInfo[i].sourceDir!=null){
                            packages.append(";PI_ii_"+i+"_sourceDir="+instrumentationInfo[i].sourceDir);
                        }
                        if(instrumentationInfo[i].targetPackage!=null){
                            packages.append(";PI_ii_"+i+"_targetPackage="+instrumentationInfo[i].targetPackage);
                        }
                        try {
                            packages.append(";PI_ii_"+i+"_banner="+instrumentationInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(";PI_ii_"+i+"_logo="+instrumentationInfo[i].logo+
                                ";PI_ii_"+i+"_handleProfiling="+instrumentationInfo[i].handleProfiling+
                                ";PI_ii_"+i+"_functionalTest="+instrumentationInfo[i].functionalTest+
                                ";PI_ii_"+i+"_labelRes="+instrumentationInfo[i].labelRes+
                                ";PI_ii_"+i+"_icon="+instrumentationInfo[i].icon
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo:packageInfo.instrumentation==null");
                }
                ConfigurationInfo[] configPreferencesInfo = packageInfo.configPreferences;
                if(packageInfo.configPreferences!=null){
                    for (int i = 0; i < configPreferencesInfo.length; i++) {
                        packages.append(";PI_cpi_"+i+"_reqGlEsVersion="+configPreferencesInfo[i].reqGlEsVersion+
                                ";PI_cpi_"+i+"_reqGlEsVersion="+configPreferencesInfo[i].reqInputFeatures+
                                ";PI_cpi_"+i+"_reqGlEsVersion="+configPreferencesInfo[i].reqKeyboardType+
                                ";PI_cpi_"+i+"_reqNavigation="+configPreferencesInfo[i].reqNavigation+
                                ";PI_cpi_"+i+"_reqTouchScreen="+configPreferencesInfo[i].reqTouchScreen
                        );
                    }
                }else {
                    Log.i("ttt", "packageInfo.configPreferences==null");
                }
                PermissionInfo[] permissionInfo = packageInfo.permissions;
                if(packageInfo.reqFeatures!=null){
                    for (int i = 0; i < permissionInfo.length; i++) {
                        if(permissionInfo[i].name!=null){
                            packages.append(";PI_pi_"+i+"_name="+permissionInfo[i].name);
                        }
                        if(permissionInfo[i].packageName!=null){
                            packages.append(";PI_pi_"+i+"_packageName="+permissionInfo[i].packageName);
                        }
                        try {
                            packages.append(";PI_pi_"+i+"_banner="+permissionInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(";PI_pi_"+i+"_flags="+permissionInfo[i].flags+
                                ";PI_pi_"+i+"_labelRes="+permissionInfo[i].labelRes+
                                ";PI_pi_"+i+"_icon="+permissionInfo[i].icon+
                                ";PI_pi_"+i+"_packageName="+permissionInfo[i].packageName+
                                ";PI_pi_"+i+"_logo="+permissionInfo[i].logo+
                                ";PI_pi_"+i+"_group="+permissionInfo[i].group+
                                ";PI_pi_"+i+"_protectionLevel="+permissionInfo[i].protectionLevel
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo: packageInfo.reqFeatures==null");
                }
                FeatureInfo[] reqFeaturesInfo = packageInfo.reqFeatures;
                if(packageInfo.reqFeatures!=null){
                    for (int i = 0; i < reqFeaturesInfo.length; i++) {
                        if(reqFeaturesInfo[i].name!=null){
                            packages.append(";PI_rfi_"+i+"_name="+reqFeaturesInfo[i].name);
                        }
                        try {
                            packages.append(";PI_rfi_"+i+"_version="+reqFeaturesInfo[i].version);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(
                                ";PI_rfi_"+i+"_flags="+reqFeaturesInfo[i].flags+
                                ";PI_rfi_"+i+"_reqGlEsVersion="+reqFeaturesInfo[i].reqGlEsVersion
                        );
                    }
                }else {
                    Log.i("ttt", "packageInfo.reqFeatures==null");
                }
                ActivityInfo[] receiversInfo = packageInfo.receivers;
                if(packageInfo.receivers!=null){
                    for(int i = 0; i<receiversInfo.length;i++){
                        if(receiversInfo[i].name!=null){
                            packages.append(";PI_ri_"+i+"_name="+receiversInfo[i].name);
                        }
                        if(receiversInfo[i].permission!=null){
                            packages.append(";PI_ri_"+i+"_permission="+receiversInfo[i].permission);
                        }
                        try {
                            packages.append(";PI_ai_"+i+"_banner="+receiversInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ri_"+i+"_documentLaunchMode="+receiversInfo[i].documentLaunchMode);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ri_"+i+"_directBootAware="+receiversInfo[i].directBootAware);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ri_"+i+"_persistableMode="+receiversInfo[i].persistableMode);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(";PI_ri_"+i+"_configChanges="+receiversInfo[i].configChanges+
                                ";PI_ri_"+i+"_descriptionRes="+receiversInfo[i].descriptionRes+
                                ";PI_ri_"+i+"_logo="+receiversInfo[i].logo+
                                ";PI_ri_"+i+"_labelRes="+receiversInfo[i].labelRes+
                                ";PI_ri_"+i+"_icon="+receiversInfo[i].icon+
                                ";PI_ri_"+i+"_exported="+receiversInfo[i].exported+
                                ";PI_ri_"+i+"_enabled="+receiversInfo[i].enabled+
                                ";PI_ri_"+i+"_uiOptions="+receiversInfo[i].uiOptions+
                                ";PI_ri_"+i+"_theme="+receiversInfo[i].theme+
                                ";PI_ri_"+i+"_screenOrientation="+receiversInfo[i].screenOrientation
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo: packageInfo.receivers==null");
                }
                ProviderInfo[] providersInfo = packageInfo.providers;
                if(packageInfo.providers!=null){
                    StringBuffer providers = new StringBuffer();
                    for (int i = 0; i < providersInfo.length; i++) {
                        if(providersInfo[i].name!=null){
                            packages.append(";PI_si_"+i+"_name="+providersInfo[i].name);
                        }
                        try {
                            packages.append(";PI_pi_"+i+"_banner="+providersInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_pi_"+i+"_directBootAware="+providersInfo[i].directBootAware);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(";PI_pi_"+i+"_descriptionRes="+providersInfo[i].descriptionRes+
                                ";PI_pi_"+i+"_logo="+providersInfo[i].logo+
                                ";PI_pi_"+i+"_labelRes="+providersInfo[i].labelRes+
                                ";PI_pi_"+i+"_icon="+providersInfo[i].icon+
                                ";PI_pi_"+i+"_exported="+providersInfo[i].exported+
                                ";PI_pi_"+i+"_enabled="+providersInfo[i].enabled
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo: packageInfo.providers==null");
                }
                ServiceInfo[] servicesInfo = packageInfo.services;
                if(packageInfo.services!=null){
                    StringBuffer services = new StringBuffer();
                    for (int i = 0; i < servicesInfo.length; i++) {
                        if(servicesInfo[i].name!=null){
                            packages.append(";PI_si_"+i+"_name="+servicesInfo[i].name);
                        }
                        if(servicesInfo[i].permission!=null){
                            packages.append(";PI_si_"+i+"_permission="+servicesInfo[i].permission);
                        }
                        try {
                            packages.append(";PI_si_"+i+"_banner="+servicesInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_si_"+i+"_directBootAware="+servicesInfo[i].directBootAware);
                        } catch (Error e) {
                            e.printStackTrace();
                        }

                        packages.append(";PI_si_"+i+"_descriptionRes="+servicesInfo[i].descriptionRes+
                                ";PI_si_"+i+"_logo="+servicesInfo[i].logo+
                                ";PI_si_"+i+"_labelRes="+servicesInfo[i].labelRes+
                                ";PI_si_"+i+"_icon="+servicesInfo[i].icon+
                                ";PI_si_"+i+"_exported="+servicesInfo[i].exported+
                                ";PI_si_"+i+"_enabled="+servicesInfo[i].enabled
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo: packageInfo.services==null");
                }
                ActivityInfo[] activityInfo = packageInfo.activities;
                if(packageInfo.activities!=null){
                    for(int i = 0; i<activityInfo.length;i++){
                        if(activityInfo[i].name!=null){
                            packages.append(";PI_ai_"+i+"_name="+activityInfo[i].name);
                        }
                        if(activityInfo[i].permission!=null){
                            packages.append(";PI_ai_"+i+"_permission="+activityInfo[i].permission);
                        }
                        try {
                            packages.append(";PI_ai_"+i+"_banner="+activityInfo[i].banner);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ai_"+i+"_documentLaunchMode="+activityInfo[i].documentLaunchMode);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ai_"+i+"_directBootAware="+activityInfo[i].directBootAware);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        try {
                            packages.append(";PI_ai_"+i+"_persistableMode="+activityInfo[i].persistableMode);
                        } catch (Error e) {
                            e.printStackTrace();
                        }
                        packages.append(";PI_ai_"+i+"_configChanges="+activityInfo[i].configChanges+
                                ";PI_ai_"+i+"_descriptionRes="+activityInfo[i].descriptionRes+
                                ";PI_ai_"+i+"_logo="+activityInfo[i].logo+
                                ";PI_ai_"+i+"_labelRes="+activityInfo[i].labelRes+
                                ";PI_ai_"+i+"_icon="+activityInfo[i].icon+
                                ";PI_ai_"+i+"_exported="+activityInfo[i].exported+
                                ";PI_ai_"+i+"_enabled="+activityInfo[i].enabled+
                                ";PI_ai_"+i+"_uiOptions="+activityInfo[i].uiOptions+
                                ";PI_ai_"+i+"_theme="+activityInfo[i].theme+
                                ";PI_ai_"+i+"_screenOrientation="+activityInfo[i].screenOrientation
                        );
                    }
                }else {
                    Log.i("ttt", "getAppInfo: activities==null");
                }
                if(packageInfo.applicationInfo!=null){
                    StringBuffer sharedLibraryFile = new StringBuffer();
                    String[] sharedLibraryFiles = packageInfo.applicationInfo.sharedLibraryFiles;
                    if(sharedLibraryFiles!=null){
                        for (int i = 0; i < sharedLibraryFiles.length; i++) {
                            if(i==0){
                                sharedLibraryFile.append(sharedLibraryFiles[i]);
                            }else {
                                sharedLibraryFile.append(","+sharedLibraryFiles[i]);
                            }
                        }
                        packages.append(";PI_sharedLibraryFiles="+sharedLibraryFile);
                    }

                    StringBuffer splitPublicSourceDir = new StringBuffer();
                    try {
                        String[] splitPublicSourceDirs = packageInfo.applicationInfo.splitPublicSourceDirs;
                        if(splitPublicSourceDirs!=null){
                            for (int i = 0; i < splitPublicSourceDirs.length; i++) {
                                if(i==0){
                                    splitPublicSourceDir.append(splitPublicSourceDirs[i]);
                                }else {
                                    splitPublicSourceDir.append(","+splitPublicSourceDirs[i]);
                                }
                            }
                            packages.append(";PI_splitPublicSourceDirs="+splitPublicSourceDir);
                        }
                    } catch (Error e) {
                        e.printStackTrace();
                    }

                    StringBuffer splitSourceDir = new StringBuffer();
                    try {
                        String[] splitSourceDirs = packageInfo.applicationInfo.splitSourceDirs;
                        if(splitSourceDirs!=null){
                            for (int i = 0; i < splitSourceDirs.length; i++) {
                                if(i==0){
                                    splitSourceDir.append(splitSourceDirs[i]);
                                }else {
                                    splitSourceDir.append(","+splitSourceDirs[i]);
                                }
                            }
                            packages.append(";PI_splitSourceDirs="+splitSourceDir);
                        }
                    } catch (Error e) {
                        e.printStackTrace();
                    }

                    if(packageInfo.applicationInfo.backupAgentName!=null){
                        packages.append(";PI_backupAgentName="+packageInfo.applicationInfo.backupAgentName);
                    }
                    if(packageInfo.applicationInfo.name!=null){
                        packages.append(";PI_name="+packageInfo.applicationInfo.name);
                    }
                    if(packageInfo.applicationInfo.packageName!=null){
                        packages.append(";PI_packageName="+packageInfo.applicationInfo.packageName);
                    }
                    if(packageInfo.applicationInfo.className!=null){
                        packages.append(";PI_className="+packageInfo.applicationInfo.className);
                    }
                    if(packageInfo.applicationInfo.dataDir!=null){
                        packages.append(";PI_dataDir="+packageInfo.applicationInfo.dataDir);
                    }
                    try {
                        if(packageInfo.applicationInfo.deviceProtectedDataDir!=null){
                            packages.append(";PI_deviceProtectedDataDir="+packageInfo.applicationInfo.deviceProtectedDataDir);
                        }
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    if(packageInfo.applicationInfo.manageSpaceActivityName!=null){
                        packages.append(";PI_manageSpaceActivityName="+packageInfo.applicationInfo.manageSpaceActivityName);
                    }
                    if(packageInfo.applicationInfo.nativeLibraryDir!=null){
                        packages.append(";PI_nativeLibraryDir="+packageInfo.applicationInfo.nativeLibraryDir);
                    }
                    if(packageInfo.applicationInfo.permission!=null){
                        packages.append(";PI_permission="+packageInfo.applicationInfo.permission);
                    }
                    if(packageInfo.applicationInfo.processName!=null){
                        packages.append(";PI_processName="+packageInfo.applicationInfo.processName);
                    }
                    if(packageInfo.applicationInfo.publicSourceDir!=null){
                        packages.append(";PI_publicSourceDir="+packageInfo.applicationInfo.publicSourceDir);
                    }
                    if(packageInfo.applicationInfo.sourceDir!=null){
                        packages.append(";PI_sourceDir="+packageInfo.applicationInfo.sourceDir);
                    }
                    if(packageInfo.applicationInfo.taskAffinity!=null){
                        packages.append(";PI_taskAffinity="+packageInfo.applicationInfo.taskAffinity);
                    }
                    try {
                        packages.append(";PI_banner="+packageInfo.applicationInfo.banner);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        packages.append(";PI_minSdkVersion="+packageInfo.applicationInfo.minSdkVersion);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    packages.append(";PI_icon="+packageInfo.applicationInfo.icon+
                            ";PI_backupAgentName="+packageInfo.applicationInfo.logo+
                            ";PI_logo="+packageInfo.applicationInfo.labelRes+
                            ";PI_compatibleWidthLimitDp="+packageInfo.applicationInfo.compatibleWidthLimitDp+
                            ";PI_descriptionRes="+packageInfo.applicationInfo.descriptionRes+
                            ";PI_enabled="+packageInfo.applicationInfo.enabled+
                            ";PI_flags="+packageInfo.applicationInfo.flags+
                            ";PI_largestWidthLimitDp="+packageInfo.applicationInfo.largestWidthLimitDp+
                            ";PI_uiOptions="+packageInfo.applicationInfo.uiOptions+
                            ";PI_uid="+packageInfo.applicationInfo.uid+
                            ";PI_theme="+packageInfo.applicationInfo.theme+
                            ";PI_targetSdkVersion="+packageInfo.applicationInfo.targetSdkVersion+
                            ";PI_requiresSmallestWidthDp="+packageInfo.applicationInfo.requiresSmallestWidthDp
                    );
                }else {
                    Log.i("ttt", "getAppInfo: packageInfo.applicationInfo==null");
                }
                Log.i("ttt", "packages: "+packages.toString());
                list.add(packages.toString());
            }
            for (ResolveInfo reInfo : resolveInfos) {
                StringBuffer reInfos = new StringBuffer();
                if(reInfo.resolvePackageName!=null){
                    reInfos.append(";RI_resolvePackageName="+reInfo.resolvePackageName);
                }
                reInfos.append(";RI_icon="+reInfo.icon+
                        ";RI_labelRes="+reInfo.labelRes+
                        ";RI_match="+reInfo.match+
                        ";RI_preferredOrder="+reInfo.preferredOrder+
                        ";RI_priority="+reInfo.priority+
                        ";RI_isDefault="+reInfo.isDefault+
                        ";RI_specificIndex="+reInfo.specificIndex);
                if(reInfo.activityInfo!=null){
                    if(reInfo.activityInfo.name!=null){
                        reInfos.append(";RI_ai_name="+reInfo.activityInfo.name);
                    }
                    if(reInfo.activityInfo.permission!=null){
                        reInfos.append(";RI_ai_permission="+reInfo.activityInfo.permission);
                    }
                    try {
                        reInfos.append(";RI_ai_banner="+reInfo.activityInfo.banner);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        reInfos.append(";RI_ai_documentLaunchMode="+reInfo.activityInfo.documentLaunchMode);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        reInfos.append(";RI_ai_persistableMode="+reInfo.activityInfo.persistableMode);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        reInfos.append(";RI_ai_directBootAware="+reInfo.activityInfo.directBootAware);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    reInfos.append(
                            ";RI_ai_configChanges="+reInfo.activityInfo.configChanges+
                            ";RI_ai_descriptionRes="+reInfo.activityInfo.descriptionRes+
                            ";RI_ai_logo="+reInfo.activityInfo.logo+
                            ";RI_ai_labelRes="+reInfo.activityInfo.labelRes+
                            ";RI_ai_icon="+reInfo.activityInfo.icon+
                            ";RI_ai_exported="+reInfo.activityInfo.exported+
                            ";RI_ai_enabled="+reInfo.activityInfo.enabled+
                            ";RI_ai_uiOptions="+reInfo.activityInfo.uiOptions+
                            ";RI_ai_theme="+reInfo.activityInfo.theme+
                            ";RI_ai_screenOrientation="+reInfo.activityInfo.screenOrientation
                    );

                }
                if(reInfo.serviceInfo!=null){
                    if(reInfo.serviceInfo.name!=null){
                        reInfos.append(";RI_si_name="+reInfo.serviceInfo.name);
                    }
                    if(reInfo.serviceInfo.permission!=null){
                        reInfos.append(";RI_si_permission="+reInfo.serviceInfo.permission);
                    }
                    if(reInfo.serviceInfo.processName!=null){
                        reInfos.append(";RI_si_processName="+reInfo.serviceInfo.processName);
                    }
                    try {
                        reInfos.append(";RI_si_banner="+reInfo.serviceInfo.banner);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        reInfos.append(";RI_si_directBootAware="+reInfo.serviceInfo.directBootAware);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    reInfos.append(";RI_si_flags="+reInfo.serviceInfo.flags+
                            ";RI_si_icon="+reInfo.serviceInfo.icon+
                            ";RI_si_labelRes="+reInfo.serviceInfo.labelRes+
                            ";RI_si_descriptionRes="+reInfo.serviceInfo.descriptionRes+
                            ";RI_si_exported="+reInfo.serviceInfo.exported+
                            ";RI_si_logo="+reInfo.serviceInfo.logo+
                            ";RI_si_enabled="+reInfo.serviceInfo.enabled
                    );
                }else {
                    Log.i("ttt", "reInfos: reInfo.serviceInfo==null");
                }
                if(reInfo.providerInfo!=null){
                    if(reInfo.providerInfo.name!=null){
                        reInfos.append(";RI_pi_name="+reInfo.providerInfo.name);
                    }
                    if(reInfo.providerInfo.processName!=null){
                        reInfos.append(";RI_pi_processName="+reInfo.providerInfo.processName);
                    }
                    if(reInfo.providerInfo.authority!=null){
                        reInfos.append(";RI_pi_authority="+reInfo.providerInfo.authority);
                    }
                    if(reInfo.providerInfo.readPermission!=null){
                        reInfos.append(";RI_pi_readPermission="+reInfo.providerInfo.readPermission);
                    }
                    if(reInfo.providerInfo.writePermission!=null){
                        reInfos.append(";RI_pi_writePermission="+reInfo.providerInfo.writePermission);
                    }
                    if(reInfo.providerInfo.packageName!=null){
                        reInfos.append(";RI_pi_packageName="+reInfo.providerInfo.packageName);
                    }
                    try {
                        reInfos.append(";RI_pi_directBootAware="+reInfo.providerInfo.directBootAware);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    try {
                        reInfos.append(";RI_pi_banner=" + reInfo.providerInfo.banner);
                    } catch (Error e) {
                        e.printStackTrace();
                    }
                    reInfos.append(";RI_pi_exported="+reInfo.providerInfo.exported+
                            ";RI_pi_descriptionRes="+reInfo.providerInfo.descriptionRes+
                            ";RI_pi_logo="+reInfo.providerInfo.logo+
                            ";RI_pi_labelRes="+reInfo.providerInfo.labelRes+
                            ";RI_pi_icon="+reInfo.providerInfo.icon+
                            ";RI_pi_flags="+reInfo.providerInfo.flags+
                            ";RI_pi_grantUriPermissions="+reInfo.providerInfo.grantUriPermissions+
                            ";RI_pi_initOrder="+reInfo.providerInfo.initOrder+
                            ";RI_pi_multiprocess="+reInfo.providerInfo.multiprocess+
                            ";RI_pi_enabled="+reInfo.providerInfo.enabled);
                } else {
                    Log.i("ttt", "reInfos: reInfo.providerInfo==null");
                }
                Log.i("ttt", "reInfos: "+reInfos.toString());
                list.add(reInfos.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private  boolean isApkInDebug() {
        try{
            ApplicationInfo info = mContext.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
