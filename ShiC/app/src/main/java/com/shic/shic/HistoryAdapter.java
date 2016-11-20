package com.shic.shic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by razvan on 20.11.2016.
 */

public class HistoryAdapter extends BaseAdapter {
    ArrayList<HistoryActivity.HistoryTuple> donations;
    Context context;
    private static LayoutInflater inflater=null;
    HistoryAdapter(Activity callerActivity, ArrayList<HistoryActivity.HistoryTuple> donations) {
        context = callerActivity;
        this.donations = donations;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return donations.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView name;
        ImageView image;
        TextView date;
        TextView status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final HistoryAdapter.Holder holder = new HistoryAdapter.Holder();
        final HistoryActivity.HistoryTuple donation = donations.get(position);
        final View rowView;
        rowView = inflater.inflate(R.layout.list_history_elem, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.name.setText(donation.name);
        holder.status = (TextView) rowView.findViewById(R.id.status);
        holder.status.setText(donation.status);

        Timestamp stamp = new Timestamp(Long.parseLong(donation.timestamp));
        Date date = new Date(stamp.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.date = (TextView)rowView.findViewById(R.id.date);
        holder.date.setText(sdf.format(date));

        holder.image = (ImageView) rowView.findViewById(R.id.categoryImage);
        holder.image.setImageResource(getImage(donation.category));

        //TODO create activity for completion and start here
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder( v.getRootView().getContext() )
                        .setTitle( "Confirm" )
                        .setMessage( "Do you want to confirm your donation?" )
                        .setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO modify DB

                                FirebaseAuth mAuth;
                                mAuth = FirebaseAuth.getInstance();

                                DatabaseReference mDatabase;
                                mDatabase = FirebaseDatabase.getInstance().getReference();

                                DatabaseReference ref = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());
                                donation.status="Done";
                                holder.status.setText(donation.status);
                                //TODO MODIFY DB!!!!!!
                            }
                        })
                        .setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        } )
                        .show();
            }
        });
        return rowView;
    }

    static int getImage(int imageIndex) {
        switch (imageIndex) {
            case 1:
                return R.drawable.clothes_color;
            case 2:
                return R.drawable.toys_color;
            case 3:
                return R.drawable.food_color;
            case 4:
                return R.drawable.furniture_color;
            case 5:
                return R.drawable.appliances_color;
            case 6:
                return R.drawable.misc_color;
            default:
                return R.drawable.clothes_color;
        }
    }

}