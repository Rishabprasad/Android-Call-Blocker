package call.tracker.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import call.tracker.BuildConfig;
import call.tracker.PreferenceManager;


public class CallBroadcastReceiver extends BroadcastReceiver {
    public static String mynumber;
    public static boolean isSet = false;
    static boolean ring = false;
    static boolean callReceived = false;
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    static long start_time, end_time;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String numberToCall = intent
                    .getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            Log.d("CallRecorder",
                    "CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: "
                            + numberToCall);
        } else {
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }
            onCallStateChanged(context, state, number);
        }
    }

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                start_time = System.currentTimeMillis();
                String sub = number.replaceAll(" ", "");
                String sub1 = sub.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");
                mynumber = sub1.substring(Math.max(sub1.length() - 10, 0));
//contactExists(context, mynumber) ||
                if (contactExistsinApp(context, mynumber)) {

                    rejectCall(context);

                    if (PreferenceManager.GetSw3()) {

                        sendSMS(number, PreferenceManager.Getsms());
                    }
                } else if (!contactExists(context, mynumber)) {
                    rejectCall(context);
                    if (PreferenceManager.GetSw3()) {

                        sendSMS(number, PreferenceManager.Getsms());
                    }
                } else {
                    Toast.makeText(context, "Incoming Detect" + mynumber + start_time, Toast.LENGTH_SHORT).show();
                }

//                Toast.makeText(context, "Incoming Detect" + mynumber + start_time, Toast.LENGTH_SHORT).show();
                //There is incoming call
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    Toast.makeText(context, "Incoming Detect" + mynumber, Toast.LENGTH_SHORT).show();

                }
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                }

                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    //Ring but no pickup-  a miss

                }
        }
        lastState = state;
    }


    public boolean contactExists(Context context, String number) {

        if (PreferenceManager.GetSw1()) {

            Uri lookupUri = Uri.withAppendedPath(
                    ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                    Uri.encode(number));
            String[] mPhoneNumberProjection = {ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME};
            Cursor cur = context.getContentResolver().query(lookupUri, mPhoneNumberProjection, null, null, null);
            try {
                if (cur.moveToFirst()) {
                    return true;
                }
            } finally {
                if (cur != null)
                    cur.close();
            }
        }
        return false;
    }

    SQLiteDatabase db;

    public boolean contactExistsinApp(Context context, String number) {

        try {

            db = context.openOrCreateDatabase("Cslltracker.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);


            Cursor cr = db.rawQuery("select * from CT where status='true'", null);

            if (cr.getCount() > 0) {
                cr.moveToFirst();
                for (int i = 0; i < cr.getCount(); i++) {


                    Cursor cm = db.rawQuery("select * from CTM where gid='" + cr.getString(0) + "'", null);

                    if (cm.getCount() > 0) {

                        cm.moveToFirst();
                        for (int ij = 0; ij < cm.getCount(); ij++) {

                            String dcon = cm.getString(2);

                            String sub = dcon.replaceAll(" ", "");
                            String sub1 = sub.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("\\-", "");
                            String mynum = sub1.substring(Math.max(sub1.length() - 10, 0));


                            if (mynum.equals(number)) {


                                Cursor cmd = db.rawQuery("select * from CTDAY where gid='" + cr.getString(0) + "'", null);

                                if (cmd.getCount() > 0) {

                                    cmd.moveToFirst();
                                    for (int ijd = 0; ijd < cmd.getCount(); ijd++) {

                                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                        Date d = new Date();
                                        String dayOfTheWeek = sdf.format(d);

                                        Toast.makeText(context, "" + dayOfTheWeek, Toast.LENGTH_SHORT).show();


                                        String one = cmd.getString(1);
                                        String two = cmd.getString(2);
                                        String three = cmd.getString(3);
                                        String four = cmd.getString(4);
                                        String five = cmd.getString(5);
                                        String six = cmd.getString(6);
                                        String seven = cmd.getString(7);
                                        if (dayOfTheWeek.equals("Sunday")) {

                                            if (one.equals("true")) {
                                                return true;
                                            }


                                        }

                                        if (dayOfTheWeek.equals("Monday")) {

                                            if (two.equals("true")) {
                                                return true;
                                            }


                                        }
                                        if (dayOfTheWeek.equals("Tuesday")) {

                                            if (three.equals("true")) {
                                                return true;
                                            }


                                        }
                                        if (dayOfTheWeek.equals("Wednesday")) {

                                            if (four.equals("true")) {
                                                return true;
                                            }


                                        }
                                        if (dayOfTheWeek.equals("Thursday")) {

                                            if (five.equals("true")) {
                                                return true;
                                            }


                                        }
                                        if (dayOfTheWeek.equals("Friday")) {

                                            if (six.equals("true")) {
                                                return true;
                                            }


                                        }
                                        if (dayOfTheWeek.equals("Saturday")) {

                                            if (seven.equals("true")) {
                                                return true;
                                            }


                                        }

                                    }
                                } else {
                                    return true;

                                }
                            }
                        }
                        cm.close();
                    }


                }
                cr.close();
            }
            db.close();

        } catch (Exception ex) {

            Toast.makeText(context, "error" + ex, Toast.LENGTH_LONG).show();
        }

        return false;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void rejectCall(Context ctx) {
        try {
            TelephonyManager telephony = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService;
            telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendSMS(String no, String msg) {


        no = no.replaceAll("\\s+", BuildConfig.FLAVOR);
        if (no.length() < 4 || msg.trim().length() <= 0) {


        } else {


            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(no, null, msg, null, null);
            Log.e("sms", "sms send");

        }

    }
}
