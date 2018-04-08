package call.tracker.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import call.tracker.Addcontact;
import call.tracker.Model.Groupitem;
import call.tracker.R;


@SuppressLint("NewApi")
public class Groupcustomadapter extends BaseAdapter {
    Context ctx;
    ArrayList<Groupitem> mListItem;
    SQLiteDatabase db;

    private static LayoutInflater inflater = null;

    public Groupcustomadapter(Context ctx, ArrayList<Groupitem> mListItem) {
        // TODO Auto-generated constructor stub
        this.ctx = ctx;
        this.mListItem = mListItem;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItem.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public void MyRefresh(ArrayList<Groupitem> mListItems) {

        mListItem = mListItems;
        notifyDataSetChanged();
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


    TextView tv;
    Button date, explore;
    boolean status1 = true;
    boolean mystatus = true;
    boolean status2 = true;
    boolean status3 = true;
    boolean status4 = true;
    boolean status5 = true;
    boolean status6 = true;
    boolean status7 = true;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.groupitem, null);
        tv = (TextView) vi.findViewById(R.id.textView);
        final ImageView imageView = (ImageView) vi.findViewById(R.id.imageView);
        ImageButton dot = (ImageButton) vi.findViewById(R.id.iv);
        date = (Button) vi.findViewById(R.id.date);
        explore = (Button) vi.findViewById(R.id.explore);
        final SwitchCompat sc = (SwitchCompat) vi.findViewById(R.id.sw);
        db = ctx.openOrCreateDatabase("Cslltracker.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        if (mListItem.get(position).isStatus()) {
            sc.setChecked(true);
        } else {
            sc.setChecked(false);
        }

        sc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (sc.isChecked()) {

                    sc.setChecked(true);
                    db.execSQL("update CT set status='true' where id='" + mListItem.get(position).getId() + "'");
                    mListItem.get(position).setStatus(true);


                } else {
                    sc.setChecked(false);
                    db.execSQL("update CT set status='false' where id='" + mListItem.get(position).getId() + "'");
                    mListItem.get(position).setStatus(false);


                }
            }
        });


        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(ctx, v);
                // This activity implements OnMenuItemClickListener .
                //popup.setOnMenuItemClickListener ((OnMenuItemClickListener) this);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit:

                                final Dialog d = new Dialog(ctx, android.R.style.Theme_Material_Light_Dialog_Alert);
                                d.setContentView(R.layout.dialogaddgroup);
                                d.setTitle("Add Group");

                                final EditText et = (EditText) d.findViewById(R.id.et);
                                Button b = (Button) d.findViewById(R.id.b);

                                Cursor ce = db.rawQuery("select * from CT where Id=" + mListItem.get(position).getId(), null);
                                if (ce != null) {
                                    while (ce.moveToNext()) {
                                        String s3 = ce.getString(1);
                                        et.setText(s3);


                                    }
                                }

                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String s = et.getText().toString().trim();
                                        if (s.equals("")) {
                                            Toast.makeText(ctx, "Field Cannot be Empty!", Toast.LENGTH_LONG).show();
                                        } else {
                                            db.execSQL("update CT set Gname='" + s + "' where Id= " + mListItem.get(position).getId());
                                            et.getText().clear();
                                            d.dismiss();
                                            MyRefresh(mListItem);
                                            ArrayList<Groupitem> al = new ArrayList<>();

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
                                                MyRefresh(al);

                                            }

                                        }


                                    }
                                });
                                d.show();
                                return true;
                            case R.id.delete:
//
                                db.execSQL("delete from CT where id=" + mListItem.get(position).getId());
                                MyRefresh(mListItem);
                                mListItem.remove(position);
                                return true;
                        }


                        return false;
                    }
                });
                popup.inflate(R.menu.group_item);
                popup.show();


            }
        });


        if (mListItem.get(position).getCnt().equals("")) {
            tv.setText(mListItem.get(position).getGname());
        } else {
            tv.setText(mListItem.get(position).getGname() + " (" + mListItem.get(position).getCnt() + ")");
        }


        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(name1.get(position).charAt(0) + "", color1, 10);

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(mListItem.get(position).getGname().charAt(0) + "", color1);

        imageView.setImageDrawable(drawable1);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CustomDialog(position);
            }


        });


        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ctx, Addcontact.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("gid", mListItem.get(position).getId());
                i.putExtra("name", mListItem.get(position).getGname());
                ctx.startActivity(i);
            }
        });


        return vi;

    }

    private void CustomDialog(final int position) {
        final Dialog d = new Dialog(ctx);

        d.setContentView(R.layout.dialogday);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        Button sun = (Button) d.findViewById(R.id.sun);
        Button mon = (Button) d.findViewById(R.id.mon);
        Button tue = (Button) d.findViewById(R.id.tue);
        Button wed = (Button) d.findViewById(R.id.wed);
        Button thu = (Button) d.findViewById(R.id.thu);
        Button fri = (Button) d.findViewById(R.id.fri);
        Button sat = (Button) d.findViewById(R.id.sat);

        Cursor cc = db.rawQuery("select * from CTDAY where gid=" + mListItem.get(position).getId(), null);

        if (cc != null) {
            if (cc.moveToNext()) {
                for (int i = 0; i < cc.getCount(); i++) {


                    if (cc.getString(1).equals("true")) {
                        boolean op1 = true;

                        sun.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        sun.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
//2
                    if (cc.getString(2).equals("true")) {
                        boolean op1 = true;

                        mon.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        mon.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
                    //3
                    if (cc.getString(3).equals("true")) {
                        boolean op1 = true;

                        tue.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        tue.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
                    //4
                    if (cc.getString(4).equals("true")) {
                        boolean op1 = true;

                        wed.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        wed.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
                    //5
                    if (cc.getString(5).equals("true")) {
                        boolean op1 = true;

                        thu.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        thu.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
                    //6
                    if (cc.getString(6).equals("true")) {
                        boolean op1 = true;

                        fri.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        fri.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }
                    //7
                    if (cc.getString(7).equals("true")) {
                        boolean op1 = true;

                        sat.setBackgroundResource(op1 ? R.drawable.shape1 : R.drawable.shape);

                    } else {
                        boolean op2 = true;
                        sat.setBackgroundResource(op2 ? R.drawable.shape : R.drawable.shape1);

                    }


                }
            } else {
                db.execSQL("insert into CTDAY(sun,mon,tue,wed,thur,fri,sat,gid) values (1,1,1,1,1,1,1,'" + mListItem.get(position).getId() + "')");

            }


        }


        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status1) {

                    view.setBackgroundResource(status1 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set sun='true' where gid='" + mListItem.get(position).getId() + "'");

                    status1 = false;
                } else {

                    view.setBackgroundResource(status1 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set sun='false' where gid='" + mListItem.get(position).getId() + "'");

                    status1 = true;
                }
            }
        });

        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status2) {

                    view.setBackgroundResource(status2 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set mon='true' where gid='" + mListItem.get(position).getId() + "'");

                    status2 = false;
                } else {

                    view.setBackgroundResource(status2 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set mon='false' where gid='" + mListItem.get(position).getId() + "'");

                    status2 = true;
                }
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status3) {

                    view.setBackgroundResource(status3 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set tue='true' where gid='" + mListItem.get(position).getId() + "'");

                    status3 = false;
                } else {

                    view.setBackgroundResource(status3 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set tue='false' where gid='" + mListItem.get(position).getId() + "'");

                    status3 = true;
                }
            }
        });
        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status4) {

                    view.setBackgroundResource(status4 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set wed='true' where gid='" + mListItem.get(position).getId() + "'");

                    status4 = false;
                } else {

                    view.setBackgroundResource(status4 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set wed='false' where gid='" + mListItem.get(position).getId() + "'");

                    status4 = true;
                }
            }
        });
        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status5) {

                    view.setBackgroundResource(status5 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set thur='true' where gid='" + mListItem.get(position).getId() + "'");

                    status5 = false;
                } else {

                    view.setBackgroundResource(status5 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set thur='false' where gid='" + mListItem.get(position).getId() + "'");

                    status5 = true;
                }
            }
        });
        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status6) {

                    view.setBackgroundResource(status6 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set fri='true' where gid='" + mListItem.get(position).getId() + "'");

                    status6 = false;
                } else {

                    view.setBackgroundResource(status6 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set fri='false' where gid='" + mListItem.get(position).getId() + "'");

                    status6 = true;
                }
            }
        });
        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status7) {

                    view.setBackgroundResource(status7 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set sat='true' where gid='" + mListItem.get(position).getId() + "'");

                    status7 = false;
                } else {

                    view.setBackgroundResource(status7 ? R.drawable.shape1 : R.drawable.shape);
                    db.execSQL("update CTDAY set sat='false' where gid='" + mListItem.get(position).getId() + "'");

                    status7 = true;
                }
            }
        });


        d.show();
    }


}