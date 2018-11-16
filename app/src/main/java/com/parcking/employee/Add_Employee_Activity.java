package com.parcking.employee;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.parcking.adapter.EmployeeAdapter;
import com.parcking.models.Employee;
import com.parcking.models.Keys;
import com.parcking.models.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mehdi.sakout.dynamicbox.DynamicBox;

public class Add_Employee_Activity extends AppCompatActivity {
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    RecyclerView recyclerView;
     EmployeeAdapter eAdapter;
    DynamicBox box;
    String type_user ="employee",idorg = "",nameorg ="";
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

            prefs =  getSharedPreferences("parking", MODE_PRIVATE);
            idorg = prefs.getString(Keys.KEY_ORGNIZATION_ID, "");
        nameorg = prefs.getString(Keys.KEY_ORGNIZATION_NAME, "");

        final List<Employee> lList = new ArrayList<Employee>();


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        ref.child("employee").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Employee employee = snapshot.getValue(Employee.class);
                    if(employee.getId_org().equals(idorg)){
                        lList.add(employee);
                        eAdapter.notifyDataSetChanged();}
                }

                Collections.reverse(lList);
                box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(mLayoutManager);
        eAdapter = new EmployeeAdapter(this, lList);
        recyclerView.setAdapter(eAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        box = new DynamicBox(Add_Employee_Activity.this,recyclerView);
        box.showLoadingLayout();

        Button add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add_Employee_Activity.this);
                LayoutInflater inflater = (LayoutInflater)  getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View views = inflater.inflate(R.layout.activity_add__employee, null);
                alertDialog.setView(views);

                final AlertDialog alertD = alertDialog.create();
                 alertD.show();

                final EditText name=(EditText)views.findViewById(R.id.name);
                final EditText email=(EditText)views.findViewById(R.id.email);
                final EditText password=(EditText)views.findViewById(R.id.password);
                final EditText city=(EditText)views.findViewById(R.id.address);
                final EditText phone=(EditText)views.findViewById(R.id.mobile);
                final Button  save = views.findViewById(R.id.save);

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(email.getText().toString())) {
                            // الرسالة التى تظهر للمستخدم
                            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                            return;
                        }else

                        if (TextUtils.isEmpty(password.getText().toString())) {
                            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                            return;
                        }else


                        if(TextUtils.isEmpty(phone.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Enter phone!", Toast.LENGTH_SHORT).show();

                        }else
                        if(TextUtils.isEmpty(name.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();

                        }else
                        if(TextUtils.isEmpty(city.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Enter city!", Toast.LENGTH_SHORT).show();


                        }else
                            // ظهور علامة التحميل
                            dialog.show();
// هذه الدالة خاصه بالبحث في الفيربيس للتأكد من ان الايميل الذي تم ادخاله غير موجود بالداتا بيس
                        //user اسم الجدول الذي يتم تخزين فيه كل حسابات المسخدمين
                        //id هو العمود الي يتم تخزين فيه الايميل
                        // email وهو متغير الستلانج اللي خزنت فيه ليتم مقارنته بالاوبجكت الموجود
                        Query fireQuery = ref.child("user").orderByChild("email").equalTo(email.getText().toString());
                        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // ازا غير موجود قم بتخزينه
                                if (dataSnapshot.getValue() == null) {


                                    // اوبجكت من نوع يوزر لتخزين بيانات الادمن الجديد
                                    user user = new user();
                                    user.setEmail(email.getText().toString());
                                    user.setPassword(password.getText().toString());
                                    user.setTypeUser(type_user);
                                    user.setCity(city.getText().toString());
                                    user.setId_org(idorg);
                                    user.setName_org(nameorg);
                                    user.setName(name.getText().toString());
                                    user.setPhone(phone.getText().toString());
                                    // حفظه ك اوبجكت في جدول اليوزر بالفيربيس
                                    ref.child("user").push().setValue(user);
                                    if(type_user.equals("employee")){
                                        ref.child("employee").push().setValue(user);
                                    }
                                    // رسالة عند الانتهاء
                                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                                    eAdapter.notifyDataSetChanged();
                                    alertD.dismiss();
                                    dialog.dismiss();

                                } else {
                                    // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "This employee already exists", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                dialog.dismiss();
                                // رساله خطأ
                                Toast.makeText(getApplicationContext(), "no connection internet ", Toast.LENGTH_LONG).show();

                            }
                        });


                    }
                });




            }
        });
    }
}
