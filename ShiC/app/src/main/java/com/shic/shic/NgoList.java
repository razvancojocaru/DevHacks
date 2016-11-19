package com.shic.shic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NgoList extends Activity {

    /* Define views */
    ListView lv;

    private DatabaseReference mDatabase;
    ProgressDialog mProgressDialog;

    ArrayList<String> names, causes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        Intent i = getIntent();
        i.getAction();
        showProgressDialog();

        names = new ArrayList<>();
        causes = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("NGOs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    if (child.child("domains").getValue() != null) {
                        names.add(child.getKey());
                        causes.add(child.child("domains").getValue().toString());
                    }
                }
                lv = (ListView) findViewById(R.id.listView);
                lv.setAdapter(new NgoListAdapter(NgoList.this, names, causes));
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
