package com.parcking.customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.adapter.ParkAdapter;
import com.parcking.models.Request;
import com.parcking.models.parking;
import com.parcking.models.user;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class reservation extends AppCompatActivity {
    RecyclerView recyclerView;
    String price="",emailEmployee="";
    int startTime = 0 , endTime=0;
    ProgressDialog dialog;
    List<parking> lList,filterList;
    FirebaseDatabase database;
    DatabaseReference ref;
    TextView price1,count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (Build.VERSION.SDK_INT >= 21) {
            Window window =  getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green));



        }

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        recyclerView = (RecyclerView)  findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
         lList = new ArrayList<parking>();
        filterList = new ArrayList<parking>();
        ParkAdapter nAdapter = new ParkAdapter(this, filterList);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Intent i = getIntent();
        price = i.getStringExtra("price");
        emailEmployee =i.getStringExtra("email");
        startTime = i.getIntExtra("start_time",0);
        endTime = i.getIntExtra("end_time",0);

        price1 = findViewById(R.id.price);
        price1.setText(price);

        count = findViewById(R.id.count);
        count.setText(filterList.size());


        Query fireQuery = ref.child("stiuation").orderByChild("emailEmployee").equalTo(emailEmployee);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(reservation.this, "Not found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    Intent i = new Intent(reservation.this,SearchParking.class);
                    startActivity(i);
                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        parking item = snapshot.getValue(parking.class);
                        lList.add(item);

                    }

                    for(int i=0;i<lList.size();i++) {
                        filterList.clear();



                    }


                }


                dialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(reservation.this, "no connected internet", Toast.LENGTH_SHORT).show();}


        });
        Button reservation = findViewById(R.id.Reservation);
        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(reservation.this,visacard.class);
                startActivity(i);
            }
        });

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
