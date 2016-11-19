package com.shic.shic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class NgoListAdapter extends BaseAdapter{
    ArrayList<String> names;
    ArrayList<String> causes;
    Context context;
    private static LayoutInflater inflater=null;
    NgoListAdapter(Activity callerActivity, ArrayList<String> names, ArrayList<String> causes) {
        context = callerActivity;
        this.names = names;
        this.causes = causes;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return names.size();
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
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_ngo_elem, null);
        holder.name = (TextView) rowView.findViewById(R.id.name);
        holder.name.setText(names.get(position));
        holder.cause = (TextView) rowView.findViewById(R.id.causes);
        holder.cause.setText(causes.get(position));
        holder.distance = (TextView) rowView.findViewById(R.id.distance);
        //TODO compute distance somewhere
        holder.distance.setText("");
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "You Clicked "+names.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}
