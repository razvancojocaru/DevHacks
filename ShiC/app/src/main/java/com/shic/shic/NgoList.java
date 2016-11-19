package com.shic.shic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class NgoList extends Activity {

    /* Define views */
    ListView lv;

    private DatabaseReference mDatabase;

    ArrayList<String> names, causes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        Intent i = getIntent();
        i.getAction();

        names = new ArrayList<>();
        causes = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("NGOs").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                Iterator<DataSnapshot> iterator = snapshot.getChildren().iterator();
//                while (iterator.hasNext()) {
//                    names.add(iterator.);
//                }
//                System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        lv=(ListView) findViewById(R.id.listView);
        String[] dummyONG = {"ONG1", "ONG2", "ONG3"};
        String[] dummyCauses = {"cauza1", "cauza2", "cauza3"};
        lv.setAdapter(new NgoListAdapter(this, dummyONG, dummyCauses));
    }
}
