package com.shic.shic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NgoList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_list);

        Intent i = getIntent();

        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText(i.getAction());
    }
}
