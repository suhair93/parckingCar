package com.parcking.customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.parcking.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainCustomer extends AppCompatActivity {

    public static TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    SharedPreferences prefs;
    private int[] tabIcons = {
            R.drawable.add,
            R.drawable.view


    };
    public  static TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_customer);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        /***********************************************************/

        if (Build.VERSION.SDK_INT >= 21) {
            Window window1 = getWindow();
            window1.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window1.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window1.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }


        Button add = findViewById(R.id.add);
        Button view = findViewById(R.id.views);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainCustomer.this,SearchParking.class);
                startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainCustomer.this,All_reservation.class);
                startActivity(i);
            }
        });

    }


}