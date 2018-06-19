package com.matwoosh.heboy;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;

/**
 * Created by Mateusz on 24/10/2015.
 * drugi ekran!!!!
 */
public class DescriptionActivity extends  AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getTitle());*/

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DescriptionActivity.this, ScanningActivity.class);
//                intent.putExtra("extrasTargetActivity", NotifyActivity.class.getName());
                startActivity(intent);
            }
        });
    }
}
