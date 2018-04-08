package call.tracker;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import call.tracker.Model.Addcontactitem;
import call.tracker.controller.Addcontactcustomadapter;

public class Addcontact extends AppCompatActivity {
    ListView lv;
    FloatingActionButton fab;
    String s, s1;
    SQLiteDatabase db;
    String gid = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(PreferenceManager.GetSw2() ? R.style.themDark : R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = getIntent().getExtras().getString("name");
        setTitle(name);
        db = openOrCreateDatabase("Cslltracker.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        lv = (ListView) findViewById(R.id.lv);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        gid = getIntent().getExtras().getString("gid");


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                dialog();
            }
        });

        DataLoad();

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.execSQL("delete from CTM where gid='" + gid + "' and id='" + al.get(i).getId() + "'");
                Toast.makeText(Addcontact.this, "Delete Suucess", Toast.LENGTH_SHORT).show();
                DataLoad();
                return false;
            }
        });

    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    ArrayList<Addcontactitem> al = new ArrayList<>();

    private void DataLoad() {
        al.clear();
        Cursor c = db.rawQuery("select * from CTM where gid='" + gid + "'", null);
        if (c != null) {

            while (c.moveToNext()) {

                Addcontactitem gi = new Addcontactitem();
                gi.setId(c.getString(0));
                gi.setName(c.getString(1));
                gi.setNumber(c.getString(2));
                gi.setGid(c.getString(3));
                al.add(gi);
            }

            Addcontactcustomadapter ca = new Addcontactcustomadapter(getApplicationContext(), al);
            lv.setAdapter(ca);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // check whether the result is ok
        if (resultCode == RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            switch (requestCode) {
                case 1888:
                    contactPicked(data);
                    break;
            }
        } else {
            Log.e("MainActivity", "Failed to pick contact");
        }
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     *
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            // Set the value to the textviews
            namee.setText(name);
            phone.setText(phoneNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    EditText namee, phone;

    public void dialog() {


        final Dialog d = new Dialog(Addcontact.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        d.setContentView(R.layout.dialogaddcontact);
        d.setTitle("Add Contact");

        namee = (EditText) d.findViewById(R.id.name);
        phone = (EditText) d.findViewById(R.id.phone);
        Button b = (Button) d.findViewById(R.id.b);

        namee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);

                startActivityForResult(contactPickerIntent, 1888);

            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s = namee.getText().toString().trim();
                s1 = phone.getText().toString().trim();

                if (s.equals("") && s1.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty!", Toast.LENGTH_LONG).show();
                } else {
                    db.execSQL("insert into CTM(name,phone,gid) values('" + s + "','" + s1 + "','" + gid + "')");
                    Toast.makeText(getApplicationContext(), "Contact Added", Toast.LENGTH_LONG).show();
                    namee.getText().clear();
                    phone.getText().clear();
                    DataLoad();
                    d.dismiss();


                }


            }
        });
        d.show();
    }

}
