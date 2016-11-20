package com.shic.shic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
        HistoryAdapter.Holder holder = new HistoryAdapter.Holder();
        final HistoryActivity.HistoryTuple donation = donations.get(position);
        View rowView;
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
                Intent i = new Intent(context, NGODetails.class);
                i.setAction(donation.donationID);
                context.startActivity(i);
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