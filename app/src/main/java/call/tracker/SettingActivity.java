package call.tracker;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Admin on 12-Jun-17.
 */

public class SettingActivity extends AppCompatActivity {
    SwitchCompat unknown, theme, sms;
    TextView tv,unknownt,themet,smst;
    LinearLayout msgc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(PreferenceManager.GetSw2() ? R.style.themDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        unknown = (SwitchCompat) findViewById(R.id.sw1);
        theme = (SwitchCompat) findViewById(R.id.sw2);
        sms = (SwitchCompat) findViewById(R.id.sw3);
        tv = (TextView) findViewById(R.id.tvsms);
        unknownt = (TextView) findViewById(R.id.unknown);
        themet = (TextView) findViewById(R.id.theme);
        smst = (TextView) findViewById(R.id.sms);

        msgc = (LinearLayout) findViewById(R.id.msgc);


        if (PreferenceManager.GetSw1()) {
            unknown.setChecked(true);
        } else {
            unknown.setChecked(false);
        }

        unknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (unknown.isChecked()) {

                    unknown.setChecked(true);
                    PreferenceManager.SetSw1(true);


                } else {
                    unknown.setChecked(false);
                    PreferenceManager.SetSw1(false);


                }
            }
        });

        if (PreferenceManager.GetSw2()) {
            theme.setChecked(true);
            unknownt.setTextColor(Color.parseColor("#24c8f1"));
            themet.setTextColor(Color.parseColor("#24c8f1"));
            smst.setTextColor(Color.parseColor("#24c8f1"));
            smst.setTextColor(Color.parseColor("#24c8f1"));
            tv.setTextColor(Color.parseColor("#24c8f1"));
        } else {
            theme.setChecked(false);
        }

        theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (theme.isChecked()) {

                    SettingActivity.this.recreate();
                    theme.setChecked(true);
                    PreferenceManager.SetSw2(true);

                    Intent i = new Intent(SettingActivity.this,Homepage.class);

                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(i);

                } else {
                    SettingActivity.this.recreate();
                    theme.setChecked(false);
                    PreferenceManager.SetSw2(false);
                    Intent i = new Intent(SettingActivity.this,Homepage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);

                }
            }
        });

        if (PreferenceManager.GetSw3()) {
            sms.setChecked(true);
            msgc.setVisibility(View.VISIBLE);
            tv.setVisibility(View.VISIBLE);
            tv.setText(PreferenceManager.Getsms());
        } else {
            sms.setChecked(false);
            msgc.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
        }

        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sms.isChecked()) {
                    sms.setChecked(true);
                    PreferenceManager.SetSw3(true);

                    CustomDialog();


                } else {
                    sms.setChecked(false);
                    PreferenceManager.SetSw3(false);
                    msgc.setVisibility(View.INVISIBLE);
                    tv.setVisibility(View.INVISIBLE);
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog();
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void CustomDialog() {


        final Dialog d = new Dialog(SettingActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        d.setContentView(R.layout.smstext);
        d.setTitle("Msg Content");

        final EditText et = (EditText) d.findViewById(R.id.et);
        Button b = (Button) d.findViewById(R.id.b);


        if (!PreferenceManager.Getsms().equals("")) {

            et.setText(PreferenceManager.Getsms());
        }
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = et.getText().toString().trim();

                if (s.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty!", Toast.LENGTH_LONG).show();
                } else {
                    PreferenceManager.Setsms(s);
                    et.getText().clear();
                    d.dismiss();
                    msgc.setVisibility(View.VISIBLE);
                    tv.setVisibility(View.VISIBLE);
                    tv.setText(PreferenceManager.Getsms());


                }


            }
        });
        d.show();
    }
}
