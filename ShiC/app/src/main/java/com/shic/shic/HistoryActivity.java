package com.shic.shic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HistoryActivity extends Activity {

    ListView lv;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog mProgressDialog;

    ArrayList<HistoryTuple> donations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mAuth = FirebaseAuth.getInstance();
        showProgressDialog();

        donations = new ArrayList<>();

        lv = (ListView)findViewById(R.id.listViewHistory);
        
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.mipmap.ic_launcher);
        getActionBar().setDisplayUseLogoEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6f4ea")));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {

                    donations.add(new HistoryTuple(child.getKey(),
                        Integer.parseInt(child.child("donation_category").getValue().toString()),
                        child.child("donation_location").getValue().toString(),
                        child.child("donation_status").getValue().toString(),
                        child.child("timestamp").getValue().toString()
                    ));
                }
                Collections.reverse(donations);
                lv.setAdapter(new HistoryAdapter(HistoryActivity.this, donations));
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    class HistoryTuple {
        int category;
        String name;
        String status;
        String timestamp;
        String donationID;
        HistoryTuple(String donationID, int category, String name, String status, String timestamp) {
            this.donationID = donationID;
            this.category = category;
            this.name = name;
            this.status = status;
            this.timestamp = timestamp;
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
