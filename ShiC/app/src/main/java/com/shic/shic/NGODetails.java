package com.shic.shic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class NGODetails extends Activity {

    /* Define views */
    TextView ngoName;
    TextView ngoDescription;
    TextView ngoTelephone;
    TextView ngoWebsite;
    Button btn;

    String ngoLatitude;
    String ngoLongitude;

    private DatabaseReference mDatabase;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngodetails);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("ShiC");

        ngoName = (TextView) findViewById(R.id.textNGOName);
        btn = (Button) findViewById(R.id.ngoDonateButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickFunction(view);
            }
        });

        Intent i = getIntent();

        String ongName = i.getAction().toString();
        showProgressDialog();
        ngoName.setText(ongName);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("NGOs").child(ongName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("address").getValue() != null) {
                    ngoDescription = (TextView) findViewById(R.id.textNGODescription);
                    ngoTelephone = (TextView) findViewById(R.id.textNGOTelephone);
                    ngoWebsite = (TextView) findViewById(R.id.textNGOWebsite);

                    ngoLatitude = snapshot.child("latitude").getValue().toString();
                    ngoLongitude = snapshot.child("longitude").getValue().toString();

                    ngoDescription.setText(snapshot.child("address").getValue().toString());
                    ngoTelephone.setText("Phone: "+snapshot.child("telephone").getValue().toString());
                    ngoWebsite.setText(snapshot.child("website").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        hideProgressDialog();
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

    public void buttonClickFunction(View v)
    {
        Intent intent = new Intent(getApplicationContext(), DirectionsActivity.class);
        Bundle extras = new Bundle();
        extras.putString("latitude",ngoLatitude);
        extras.putString("longitude",ngoLongitude);
        intent.putExtras(extras);
        startActivity(intent);
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
