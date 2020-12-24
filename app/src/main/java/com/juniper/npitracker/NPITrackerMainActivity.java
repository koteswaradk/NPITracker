package com.juniper.npitracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.juniper.npitracker.rlistatus.RLIViewActivity;

public class NPITrackerMainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.npitracker_activity_main);
        findViewById(R.id.entertoapp).setOnClickListener(this);
        findViewById(R.id.rlistatus).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.entertoapp:
                startActivity(new Intent(NPITrackerMainActivity.this,NPITrackerPhaseActivity.class));
                finish();
                break;
            case R.id.rlistatus:
                startActivity(new Intent(NPITrackerMainActivity.this,RLIViewActivity.class));
                finish();
                break;
        }
    }
}
