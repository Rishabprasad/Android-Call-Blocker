package call.tracker;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("CommitPrefEdits")
public class PreferenceManager extends Application {

    static SharedPreferences preferences;
    static SharedPreferences.Editor prefEditor;
    Context _context;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences("Community", MODE_PRIVATE);
        prefEditor = preferences.edit();
    }
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PreferenceManager(Context context) {
        this._context = context;
        preferences = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        prefEditor = preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        prefEditor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        prefEditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static void SetStatus(Boolean status) {
        prefEditor.putBoolean("status", status).commit();
    }

    public static Boolean GetStatus() {
        return preferences.getBoolean("status", false);
    }

    public static void SetMStatus(Boolean status) {
        prefEditor.putBoolean("Mstatus", status).commit();
    }

    public static Boolean GetMStatus() {
        return preferences.getBoolean("Mstatus", false);
    }

    public static void SetloopStatus(Boolean status) {
        prefEditor.putBoolean("loopstatus", status).commit();
    }

    public static Boolean GetloopStatus() {
        return preferences.getBoolean("loopstatus", false);
    }

    public static void SetloopStartStatus(Boolean status) {
        prefEditor.putBoolean("startloopstatus", status).commit();
    }

    public static Boolean GetloopStartStatus() {
        return preferences.getBoolean("startloopstatus", false);
    }

    public static void SetAppStatus(Boolean status) {
        prefEditor.putBoolean("apps", status).commit();
    }

    public static Boolean GetAppStatus() {
        return preferences.getBoolean("apps", false);
    }


    public static void SetUid(String uid) {
        prefEditor.putString("uid", uid).commit();
    }


    public static String GetUid() {
        return preferences.getString("uid", "");
    }



    public static void Setname(String name) {
        prefEditor.putString("name", name).commit();
    }


    public static String Getname() {
        return preferences.getString("name", "");
    }




    public static void SetEid(String eid) {
        prefEditor.putString("eid", eid).commit();
    }

    public static String GetEid() {
        return preferences.getString("eid", "");
    }

    public static void Setimg(String img) {
        prefEditor.putString("img", img).commit();
    }

    public static String Getimg() {
        return preferences.getString("img", "");
    }


    public static void Setmid(String mid) {
        prefEditor.putString("mid", mid).commit();
    }

    public static String Getmid() {
        return preferences.getString("mid", "");
    }

    //Patient


    public static void SetPid(String pid) {
        prefEditor.putString("pid", pid).commit();
    }


    public static String GetPid() {
        return preferences.getString("pid", "");
    }



    public static void SetPname(String pname) {
        prefEditor.putString("pname", pname).commit();
    }


    public static String GetPname() {
        return preferences.getString("pname", "");
    }




    public static void SetPeid(String peid) {
        prefEditor.putString("peid", peid).commit();
    }

    public static String GetPeid() {
        return preferences.getString("peid", "");
    }

    public static void SetPimg(String pimg) {
        prefEditor.putString("pimg", pimg).commit();
    }

    public static String GetPimg() {
        return preferences.getString("pimg", "");
    }


    public static void SetPmid(String pmid) {
        prefEditor.putString("pmid", pmid).commit();
    }

    public static String GetPmid() {
        return preferences.getString("pmid", "");
    }

    public static void SetSw1(Boolean status) {
        prefEditor.putBoolean("sw1", status).commit();
    }

    public static Boolean GetSw1() {
        return preferences.getBoolean("sw1", false);
    }

    public static void SetSw2(Boolean status) {
        prefEditor.putBoolean("sw2", status).commit();
    }

    public static Boolean GetSw2() {
        return preferences.getBoolean("sw2", false);
    }

    public static void SetSw3(Boolean status) {
        prefEditor.putBoolean("sw3", status).commit();
    }

    public static Boolean GetSw3() {
        return preferences.getBoolean("sw3", false);
    }

    public static void Setsms(String sms) {
        prefEditor.putString("sms", sms).commit();
    }


    public static String Getsms() {
        return preferences.getString("sms", "");
    }











}