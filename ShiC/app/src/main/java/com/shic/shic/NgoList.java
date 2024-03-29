package com.shic.shic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;

public class NgoList extends Activity {

    public static final String TAG = "NgoList";

    /* Define views */
    ListView lv;

    private DatabaseReference mDatabase;
    ProgressDialog mProgressDialog;

    ArrayList<String> names, causes;
    ArrayList<NgoTuple> ngos;

    String categoryIndex;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.mipmap.ic_launcher);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6f4ea")));

        Intent i = getIntent();
        categoryIndex = i.getAction();
        location = (Location)i.getExtras().get("coords");
        Log.d(TAG, location.getLongitude() + " " + location.getLatitude());
        showProgressDialog();

        names = new ArrayList<>();
        causes = new ArrayList<>();
        ngos = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("NGOs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("categories").hasChild(categoryIndex)) {
                        if (child.child("domains").getValue() != null) {
                            Location l = new Location("");
                            l.setLatitude(Double.parseDouble(child.child("latitude").getValue().toString()));
                            l.setLongitude(Double.parseDouble(child.child("longitude").getValue().toString()));
                            String name = child.getKey();
                            String cause = child.child("domains").getValue().toString();

                            names.add(name);
                            causes.add(cause);
                            double distance = location.distanceTo(l);
                            ngos.add(new NgoTuple(name, cause, distance));
                        }
                    }
                }
                Collections.sort(ngos, new DistanceComparator());
                lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(new NgoListAdapter(NgoList.this, ngos, categoryIndex));
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Searching for nearest locations...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
	}
}
    public class NgoTuple {
        public String name;
        public String cause;
        public double distance;

        NgoTuple(String name, String cause, double distance) {
            this.name = name;
            this.cause = cause;
            this.distance = distance;
        }
    }

    class DistanceComparator implements Comparator<NgoTuple> {
        @Override
        public int compare(NgoTuple ngoTuple, NgoTuple t1) {
            if (ngoTuple.distance < t1.distance)
                return -1;
            else if (ngoTuple.distance == t1.distance)
                return 0;
            else
                return 1;
        }
    }
}
