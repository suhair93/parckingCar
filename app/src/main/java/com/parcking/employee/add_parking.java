package com.parcking.employee;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.models.Keys;
import com.parcking.models.Request;

import mehdi.sakout.dynamicbox.DynamicBox;

public class add_parking extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    EditText days,time,price,info ;
    Button  save;

    SharedPreferences prefs;
    String idorg="",nameorg="",emai1="",name="";
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);


        prefs =  getSharedPreferences("parking", MODE_PRIVATE);
        idorg = prefs.getString(Keys.KEY_ORGNIZATION_ID, "");
        nameorg = prefs.getString(Keys.KEY_ORGNIZATION_NAME, "");
        emai1 = prefs.getString(Keys.KEY_EMPLOYEE, "");
        name = prefs.getString(Keys.KEY_NAME, "");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        progressDialog = new ProgressDialog(add_parking.this);

        info = findViewById(R.id.info);
        price= findViewById(R.id.price);
        days = findViewById(R.id.day);
        time = findViewById(R.id.time);



        save = findViewById(R.id.save);

        Query fireQuery = ref.child("Request").orderByChild("emailEmployee").equalTo(emai1);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا غير موجود قم بتخزينه
                if (dataSnapshot.getValue() != null) {


                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Request request = snapshot.getValue(Request.class);
                        info.setText(request.getInformation());
                        price.setText(request.getPrice());
                        days.setText(request.getDays());
                        time.setText(request.getTime());
                    }


                } else {
                    // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // رساله خطأ
                Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query fireQuery = ref.child("Request").orderByChild("emailEmployee").equalTo(emai1);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ازا غير موجود قم بتخزينه
                        if (dataSnapshot.getValue() != null) {


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Request data = snapshot.getValue(Request.class);
                                data.setDays(days.getText().toString());
                                data.setTime(time.getText().toString());
                                data.setPrice(price.getText().toString());
                                data.setInformation(info.getText().toString());
                                data.isStatus(false);
                                data.setEmailEmployee(emai1);
                                data.setNameEmployee(name);
                                data.setNameOrg(nameorg);
                                data.setIdOrg(idorg);
                                snapshot.getRef().setValue(data);
                                Toast.makeText(getApplicationContext(), "done ", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة

                            Request data = new Request();
                            data.setDays(days.getText().toString());
                            data.setTime(time.getText().toString());
                            data.setPrice(price.getText().toString());
                            data.setInformation(info.getText().toString());
                            data.isStatus(false);
                            data.setEmailEmployee(emai1);
                            data.setNameEmployee(name);
                            data.setNameOrg(nameorg);
                            data.setIdOrg(idorg);
                            ref.child("Request").push().setValue(data);
                            Toast.makeText(getBaseContext(), " Done", Toast.LENGTH_LONG).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        // رساله خطأ
                        Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


    }





}
