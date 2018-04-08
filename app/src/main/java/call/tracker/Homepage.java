package call.tracker;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import call.tracker.Model.Groupitem;
import call.tracker.controller.Groupcustomadapter;

public class Homepage extends AppCompatActivity {
    SQLiteDatabase db;
    FloatingActionButton fab;
    String s;
    ListView lv;
    RelativeLayout activity_homepage;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Homepage.this.setTheme(PreferenceManager.GetSw2() ? R.style.themDark : R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lv = (ListView) findViewById(R.id.lv);
        activity_homepage=(RelativeLayout)findViewById(R.id.activity_homepage);

        db = openOrCreateDatabase("Cslltracker.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        db.execSQL("create table if not exists CT(Id integer primary key autoincrement,Gname text,status text)");
        db.execSQL("create table if not exists CTM(Id integer primary key autoincrement,name text,phone text,gid text)");
        db.execSQL("create table if not exists CTDAY(Id integer primary key autoincrement,sun text,mon text,t`ue text,wed text,thur text,fri text,sat text,gid text)");
        if (PreferenceManager.GetSw2()) {
            activity_homepage.setBackgroundColor(Color.parseColor("#000000"));

        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialog();
            }
        });

        DataLoad();

        lv.setOnScrollListener(new OnScrollObserver() {
            @Override
            public void onScrollUp() {
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollDown() {
                fab.setVisibility(View.INVISIBLE);
            }
        });
    }
    public void CustomDialog() {


        final Dialog d = new Dialog(Homepage.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        d.setContentView(R.layout.dialogaddgroup);
        d.setTitle("Add Group");

        final EditText et = (EditText) d.findViewById(R.id.et);
        Button b = (Button) d.findViewById(R.id.b);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s = et.getText().toString().trim();
                if (s.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty!", Toast.LENGTH_LONG).show();
                } else {
                    db.execSQL("insert into CT(Gname,status) values('" + s + "','false')");
                    Toast.makeText(getApplicationContext(), "Group Added", Toast.LENGTH_LONG).show();
                    et.getText().clear();
                    d.dismiss();
                    DataLoad();
                }


            }
        });
        d.show();
    }


    ArrayList<Groupitem> al = new ArrayList<>();

    private void DataLoad() {
        al.clear();
        Cursor c = db.rawQuery("select * from CT", null);
        if (c != null) {

            while (c.moveToNext()) {

                Groupitem gi = new Groupitem();
                gi.setId(c.getString(0));
                gi.setGname(c.getString(1));
                gi.setStatus(Boolean.parseBoolean(c.getString(2)));
                gi.setCnt(getCount(c.getString(0)));

                al.add(gi);
            }

            Groupcustomadapter ca = new Groupcustomadapter(Homepage.this, al);
            lv.setAdapter(ca);
        }
    }

    public String getCount(String id) {
        String cnt = "";
        Cursor c = db.rawQuery("select * from CTM where gid=" + id, null);
        if (c != null) {
            if (c.getCount() > 0) {
                cnt = c.getCount() + "";
            }

        }
        return cnt;

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataLoad();
        Homepage.this.setTheme(PreferenceManager.GetSw2() ? R.style.themDark : R.style.AppTheme);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
        public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.active) {
            activeData();
        } else if (item.getItemId() == R.id.inactive) {
            inactiveData();


        } else if (item.getItemId() == R.id.allgroup) {
            DataLoad();
        } else if (item.getItemId() == R.id.setting) {
            Intent i = new Intent(Homepage.this, SettingActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.ReplayIntro) {
            Intent i = new Intent(Homepage.this, Replay.class);
            startActivity(i);
        }


        if (item.getItemId() == R.id.m1) {


        }
//
        else if (item.getItemId() == R.id.m2) {


            Intent sharingIntent = new Intent(
                    Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent
                    .putExtra(Intent.EXTRA_SUBJECT, "");
            sharingIntent
                    .putExtra(
                            Intent.EXTRA_TEXT,
                            "Call Tracker"
                                    + "\n"
                                    + "https://play.google.com/store/apps/details?id=wap.king");
            startActivity(Intent.createChooser(sharingIntent,
                    getResources().getString(R.string.app_name)));


        }
        return super.onOptionsItemSelected(item);
    }

    private void inactiveData() {
        al.clear();
        Cursor c = db.rawQuery("select * from CT where status='false'", null);
        if (c != null) {

            while (c.moveToNext()) {

                Groupitem gi = new Groupitem();
                gi.setId(c.getString(0));
                gi.setGname(c.getString(1));
                gi.setStatus(Boolean.parseBoolean(c.getString(2)));
                gi.setCnt(getCount(c.getString(0)));

                al.add(gi);
            }

            Groupcustomadapter ca = new Groupcustomadapter(Homepage.this, al);
            lv.setAdapter(ca);
        }
    }

    private void activeData() {
        Toast.makeText(this, "Active", Toast.LENGTH_LONG).show();
        al.clear();
        Cursor c = db.rawQuery("select * from CT where status='true'", null);
        if (c != null) {

            while (c.moveToNext()) {

                Groupitem gi = new Groupitem();
                gi.setId(c.getString(0));
                gi.setGname(c.getString(1));
                gi.setStatus(Boolean.parseBoolean(c.getString(2)));
                gi.setCnt(getCount(c.getString(0)));

                al.add(gi);
            }

            Groupcustomadapter ca = new Groupcustomadapter(Homepage.this, al);
            lv.setAdapter(ca);
        }
    }


    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        // Let the system handle the back button

        super.onBackPressed();

    }


    public abstract class OnScrollObserver implements AbsListView.OnScrollListener {

        public abstract void onScrollUp();

        public abstract void onScrollDown();

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        int last = 0;
        boolean control = true;

        @Override
        public void onScroll(AbsListView view, int current, int visibles, int total) {
            if (current < last && !control) {
                onScrollUp();
                control = true;
            } else if (current > last && control) {
                onScrollDown();
                control = false;
            }

            last = current;
        }
    }
}