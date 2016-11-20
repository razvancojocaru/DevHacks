package com.shic.shic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class NgoListAdapter extends BaseAdapter {
    ArrayList<NgoList.NgoTuple> ngos;
    Context context;
    String categoryIndex;
    private static LayoutInflater inflater=null;
    NgoListAdapter(Activity callerActivity, ArrayList<NgoList.NgoTuple> ngos, String categoryIndex) {
        context = callerActivity;
        this.ngos = ngos;
        this.categoryIndex = categoryIndex;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return ngos.size();
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
        TextView cause;
        TextView distance;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        final NgoList.NgoTuple ngo = ngos.get(position);
        View rowView;
        rowView = inflater.inflate(R.layout.list_ngo_elem, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.name.setText(ngo.name);
        holder.cause = (TextView) rowView.findViewById(R.id.causes);
        holder.cause.setText(ngo.cause);
        holder.distance = (TextView) rowView.findViewById(R.id.distance);
        holder.distance.setText(String.format("%.2f", ngo.distance/1000) + "km");
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NGODetails.class);
                Bundle extras = new Bundle();
                extras.putString("name",ngo.name);
                extras.putString("category",categoryIndex);
                i.putExtras(extras);
                context.startActivity(i);
            }
        });
        return rowView;
    }

}
