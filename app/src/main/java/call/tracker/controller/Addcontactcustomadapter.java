package call.tracker.controller;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import call.tracker.Model.Addcontactitem;
import call.tracker.PreferenceManager;
import call.tracker.R;

/**
 * Created by Admin on 03-Jun-17.
 */

public class Addcontactcustomadapter extends BaseAdapter {
    Context ctx;
    ArrayList<Addcontactitem> mListItem;

    private static LayoutInflater inflater = null;

    public Addcontactcustomadapter(Context ctx, ArrayList<Addcontactitem> mListItem) {
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


    TextView tv, tv1;
    ImageView del;
    SQLiteDatabase db;

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.addcontactitem, null);
        tv = (TextView) vi.findViewById(R.id.textView);
        tv1 = (TextView) vi.findViewById(R.id.textView1);
        ImageView imageView = (ImageView) vi.findViewById(R.id.imageView);
        del = (ImageView) vi.findViewById(R.id.del);

        if (PreferenceManager.GetSw2()) {
            tv.setTextColor(Color.parseColor("#24c8f1"));
            tv1.setTextColor(Color.parseColor("#24c8f1"));

        }
        tv.setText(mListItem.get(position).getName());
        tv1.setText(mListItem.get(position).getNumber());


        db = ctx.openOrCreateDatabase("Cslltracker.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
//        TextDrawable drawable1 = TextDrawable.builder()
//                .buildRoundRect(name1.get(position).charAt(0) + "", color1, 10);

        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound(mListItem.get(position).getName().charAt(0) + "", color1);

        imageView.setImageDrawable(drawable1);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("delete from CTM where id=" + mListItem.get(position).getId());
                MyRefresh(mListItem);
                mListItem.remove(position);
            }
        });

        return vi;
    }

    public void MyRefresh(ArrayList<Addcontactitem> mListItems) {

        mListItem = mListItems;
        notifyDataSetChanged();
    }

}

