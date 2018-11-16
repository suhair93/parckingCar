package com.parcking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.models.user;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Signup extends AppCompatActivity {
    ProgressDialog dialog;
    Button customer,parcking,signup;
    LinearLayout idOrg,nameOrg;
    ArrayAdapter<String> ArrayName;
    private String[] nameCity;
    FirebaseDatabase database;
    DatabaseReference ref;
    EditText email,password,return_password,phone,name,id_org,name_org;
    AppCompatSpinner city;
    String type_user ="customer",idorg = "",nameorg ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this,Start.class);
                startActivity(i);
            }
        });

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        idOrg = findViewById(R.id.layout_id);
        nameOrg = findViewById(R.id.layout_name);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        return_password = findViewById(R.id.erpassword);
        city = findViewById(R.id.city);
        id_org = findViewById(R.id.id_org);
        name_org = findViewById(R.id.user_org);
        phone = findViewById(R.id.phone);



        parcking = findViewById(R.id.parking);
        customer = findViewById(R.id.customer);

        parcking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               type_user ="admin";
                idOrg.setVisibility(View.VISIBLE);
                nameOrg.setVisibility(View.VISIBLE);
                customer.setTextColor(getResources().getColor(R.color.white));
                parcking.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });


        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               type_user = "customer";
                idOrg.setVisibility(View.GONE);
                nameOrg.setVisibility(View.GONE);
                parcking.setTextColor(getResources().getColor(R.color.white));
                customer.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        });
        nameCity = new String[5];
        nameCity[0] = "Select City";
        nameCity[1] = "Riyadh";
        nameCity[2] = "Medina Monwara";
        nameCity[3] = "Jadah";
        nameCity[4] = "Damam";

        ArrayName = new ArrayAdapter<String>(getBaseContext(), R.layout.tv, nameCity);
        city.setAdapter(ArrayName);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                ((TextView) view).setTextColor(Color.BLACK);
                if (item != null) {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        signup = findViewById(R.id.singup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// شروط عندما لا يدخل المستخدم الايميل او الباسورد
                if (TextUtils.isEmpty(email.getText().toString())) {
                    // الرسالة التى تظهر للمستخدم
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!isEmailValid(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter correct email address!", Toast.LENGTH_SHORT).show();


                }else


                if(!password.getText().toString().equals(return_password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password is not identical", Toast.LENGTH_SHORT).show();

                }else
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Enter phone!", Toast.LENGTH_SHORT).show();

                }else
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();

                }
              else
                if(city.getSelectedItemPosition()== 0){
                    Toast.makeText(getApplicationContext(), "Enter city!", Toast.LENGTH_SHORT).show();

                }
                else if(type_user.equals("admin")){
                    if(TextUtils.isEmpty(id_org.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Enter id parking!", Toast.LENGTH_SHORT).show();

                    }else
                    if(TextUtils.isEmpty(name_org.getText().toString())){
                        Toast.makeText(getApplicationContext(), "Enter name parking!", Toast.LENGTH_SHORT).show();

                    }

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

                            idorg = id_org.getText().toString();
                            nameorg = name_org.getText().toString();
                            // اوبجكت من نوع يوزر لتخزين بيانات الادمن الجديد
                            user user = new user();
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());
                            user.setTypeUser(type_user);
                            user.setCity(city.getSelectedItem().toString());
                            user.setId_org(idorg);
                            user.setName_org(nameorg);
                            user.setName(name.getText().toString());
                            user.setPhone(phone.getText().toString());
                            // حفظه ك اوبجكت في جدول اليوزر بالفيربيس
                            ref.child("user").push().setValue(user);
                            if(type_user.equals("admin")){
                                ref.child("admin").push().setValue(user);
                            }else if(type_user.equals("customer")){
                                ref.child("customer").push().setValue(user);
                            }
                            // رسالة عند الانتهاء
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            // الانتقال اللي تسجيل الدخول
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        } else {
                            // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "This account already exists", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();
                        // رساله خطأ
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


    }


    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
