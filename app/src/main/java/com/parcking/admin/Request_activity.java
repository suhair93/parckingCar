package com.parcking.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.adapter.AllResAdapter;
import com.parcking.adapter.EmployeeAdapter;
import com.parcking.adapter.RequestAdapter;
import com.parcking.employee.add_parking;
import com.parcking.models.AllResrvation;
import com.parcking.models.Keys;
import com.parcking.models.Request;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Request_activity extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference ref;
    SharedPreferences prefs;
    String idorg="",nameorg="",emai1="",name="";
    ProgressDialog dialog ;
    RequestAdapter nAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        if (Build.VERSION.SDK_INT >= 21) {
            Window window =  getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

            dialog = new ProgressDialog(this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("waiting ....");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);


        }
        prefs =  getSharedPreferences("parking", MODE_PRIVATE);
        idorg = prefs.getString(Keys.KEY_ORGNIZATION_ID, "");
        nameorg = prefs.getString(Keys.KEY_ORGNIZATION_NAME, "");
        emai1 = prefs.getString(Keys.KEY_ADMIN, "");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        final List<Request> lList = new ArrayList<Request>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);


        dialog.show();
        Query fireQuery = ref.child("Request").orderByChild("idOrg").equalTo(idorg);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا غير موجود قم بتخزينه
                if (dataSnapshot.getValue() != null) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request request = snapshot.getValue(Request.class);
                      lList.add(request);
                     nAdapter.notifyDataSetChanged();
                      dialog.dismiss();
                    }


                } else {
                    // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                    Toast.makeText(getApplicationContext(), "no requests", Toast.LENGTH_LONG).show();
                    dialog.dismiss();



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // رساله خطأ
                Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();
                dialog.dismiss();

            }
        });


        nAdapter = new RequestAdapter(this, lList);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
