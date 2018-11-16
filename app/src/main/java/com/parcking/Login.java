package com.parcking;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.admin.MainAdmin;
import com.parcking.customer.MainCustomer;
import com.parcking.employee.MainEmployee;
import com.parcking.models.Keys;
import com.parcking.models.user;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {
      Button loginBtn ;
      EditText   inputUsername;
    EditText inputPassword;;
    ImageButton mCbShowPwd;
    String name = "", password = "";
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    List<user> userlist;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        userlist= new ArrayList<>();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);

        inputUsername = findViewById(R.id.email);

        inputPassword = (EditText) findViewById(R.id.password);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Start.class);
                startActivity(i);
            }
        });

        mCbShowPwd = (ImageButton) findViewById(R.id.show_pass);
        mCbShowPwd.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        inputPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;

                }
                return true;
            }
        });



        loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = inputUsername.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.show();
                //authenticate user
                // نفس الداله المستخدمه بتسجيل حساب جديد

                Query fireQuery = ref.child("user").orderByChild("email").equalTo(email);
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ازا الحساب غير موجوديظهر مسج
                        if (dataSnapshot.getValue() == null) {
                            Toast.makeText(Login.this, "Not found", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                        } else {
                            List<user> searchList = new ArrayList<user>();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                user user = snapshot.getValue(user.class);
                                searchList.add(user);

                            }
                            // لوب ليقوم بالبحث عن الحساب
                            for(int i=0;i<searchList.size();i++){
                                // ازا الايميل والباسورد صحيحة
                                if(searchList.get(i).getEmail().equals(email) && searchList.get(i).getPassword().equals(password)) {

                                    //الاوبجكت هذا خاص بنقل البيانات من كلاس لكلاس اخر
                                    SharedPreferences.Editor editor = getSharedPreferences("parking", MODE_PRIVATE).edit();
                                    // شرط ازا كان نوع المستخدم ادمن
                                    if (searchList.get(i).getTypeUser().equals(Keys.KEY_ADMIN)) {

                                        editor.putString(Keys.KEY_ADMIN, email);
                                        editor.putString(Keys.KEY_ORGNIZATION_ID, searchList.get(i).getId_org());
                                        editor.putString(Keys.KEY_ORGNIZATION_NAME, searchList.get(i).getName_org());
                                        editor.apply();
                                        dialog.dismiss();
                                        // الانتقال لواجهة الرئيسية اللادمن
                                        Intent intent = new Intent(Login.this, MainAdmin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    // ازا نوع المتسخدم طالب
                                    if (searchList.get(i).getTypeUser().equals(Keys.KEY_EMPLOYEE) ) {


                                        // خزن الايميل هنا
                                        editor.putString(Keys.KEY_EMPLOYEE, email);
                                        editor.putString(Keys.KEY_NAME, searchList.get(i).getName());
                                        editor.putString(Keys.KEY_ORGNIZATION_ID, searchList.get(i).getId_org());
                                        editor.putString(Keys.KEY_ORGNIZATION_NAME, searchList.get(i).getName_org());
                                        editor.apply();
                                        dialog.dismiss();
                                        // الانتقال لواجة الالرئيسية للطالب
                                        Intent intent = new Intent(Login.this, MainEmployee.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();

                                    }
                                    // ازا كان مشرف
                                    if (searchList.get(i).getTypeUser().equals(Keys.KEY_CUSTOMER) ) {
                                        editor.putString(Keys.KEY_CUSTOMER, email);
                                        editor.apply();
                                        dialog.dismiss();
                                        Intent intent = new Intent(Login.this, MainCustomer.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                    // غير ذلك ازا كان خطأ بكلمة المرور او اسم المستخدم
                                }else{
                                    dialog.dismiss();
                                    Toast.makeText(Login.this, "invalid user name or password", Toast.LENGTH_SHORT).show();}

                            }


                            dialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();
                        Toast.makeText(Login.this, "no connected internet", Toast.LENGTH_SHORT).show();}


                });






            }





        });




    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
