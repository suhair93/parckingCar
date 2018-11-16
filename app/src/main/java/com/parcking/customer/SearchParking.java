package com.parcking.customer;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.employee.stiuation;
import com.parcking.models.Request;
import com.parcking.models.admin;
import com.parcking.models.parking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchParking extends AppCompatActivity {
    Calendar myCalendar;
    Spinner parkingName, city;
    ArrayAdapter<String> name;
    FirebaseDatabase database;
    DatabaseReference ref;

    List<admin> listAdmin =new ArrayList<>();
    Button search;
    ArrayAdapter<String> ArrayName;
    String selectedID = "", selectedName = "";
    private String[] nameOrg;
    private String[] idOrg;
    private String[] nameCity = new String[5];
    DatePickerDialog.OnDateSetListener date1;
    TextView date;
    ProgressDialog dialog;
    EditText start_date, end_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_parking);

        //***********//
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        //***********//

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.green));

        }

        //***********//
       city = findViewById(R.id.spinnerCity);
        parkingName = findViewById(R.id.spinnerParking);
        nameCity[0] = "Select City";
        nameCity[1] = "Riyadh";
        nameCity[2] = "Medina Monwara";
        nameCity[3] = "Jadah";
        nameCity[4] = "Damam";

        ArrayName = new ArrayAdapter<String>(SearchParking.this, R.layout.tv, nameCity);
        city.setAdapter(ArrayName);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                ((TextView) view).setTextColor(Color.BLACK);
                if (item != null) {
                    SpinnerParkingName();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search = findViewById(R.id.search);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        start_date = (EditText) findViewById(R.id.start_time);
        getTime(start_date);
        end_date = (EditText) findViewById(R.id.end_time);
        getTime(end_date);
        date = findViewById(R.id.date);
        myCalendar = Calendar.getInstance();
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date.setText(sdf.format(myCalendar.getTime()) + "");

        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        parkingName = findViewById(R.id.spinnerParking);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();


        SpinnerParkingName();


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                org();
            }
        });


    }

    public void updateDate(){
        Query fireQuery = ref.child("stiuation").orderByChild("idOrg").equalTo(selectedID);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(SearchParking.this, "Not found", Toast.LENGTH_SHORT).show();
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        parking s = snapshot.getValue(parking.class);
                         s.setDate(date.getText().toString());
                    }

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(SearchParking.this, "no connected internet", Toast.LENGTH_SHORT).show();}


        });

    }

    public void SpinnerParkingName (){

        Query fireQuery = ref.child("admin").orderByChild("city").equalTo(city.getSelectedItem().toString());
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    nameOrg = new String[1];
                    idOrg = new String[1];
                    nameOrg[0] = "not found ";
                    idOrg[0] = "not found ";
                    Toast.makeText(SearchParking.this, "Not found", Toast.LENGTH_SHORT).show();
                } else {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        admin admin = snapshot.getValue(admin.class);
                        listAdmin.add(admin);
                    }
                    nameOrg = new String[listAdmin.size()];
                    idOrg = new String[listAdmin.size()];
                    for (int i =0 ; i<listAdmin.size();i++){
                        nameOrg[i] = listAdmin.get(i).getName_org();
                        idOrg[i] = listAdmin.get(i).getId_org();
                    }
                }
                name = new ArrayAdapter<String>(SearchParking.this, R.layout.tv, nameOrg);
                parkingName.setAdapter(name);
                parkingName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Object item = parent.getItemAtPosition(position);
                      //  ((TextView) view).setTextColor(Color.BLACK);
                        if (item != null) {
                            selectedID = idOrg[position];
                            selectedName = nameOrg[position];

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(SearchParking.this, "no connected internet", Toast.LENGTH_SHORT).show();}


        });

    }

    public void org() {
        Query fireQuery = ref.child("Request").orderByChild("idOrg").equalTo(selectedID);
        fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ازا الحساب غير موجوديظهر مسج
                if (dataSnapshot.getValue() == null) {
                    Toast.makeText(SearchParking.this, "Not found", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                } else {


                    Request request = dataSnapshot.getValue(Request.class);
                    String price = request.getPrice();
                    String email = request.getEmailEmployee();


                    Intent i = new Intent(SearchParking.this, reservation.class);
                    i.putExtra("price", price);
                    i.putExtra("email", email);
                    i.putExtra("start_time", start_date.getText().toString());
                    i.putExtra("end_time", end_date.getText().toString());
                    startActivity(i);

                }


                dialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(SearchParking.this, "no connected internet", Toast.LENGTH_SHORT).show();
            }


        });


    }









    private void updateLabel() {

        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()) + "");

    }

    public void getTime(final EditText editTextDate) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final TimePickerDialog TimePickerDialog = new TimePickerDialog(SearchParking.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
              //   date.set(Calendar.MINUTE, 00);
                String myFormat = "HH";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editTextDate.setText(sdf.format(date.getTime()));

            }
        }, currentDate.get(Calendar.HOUR_OF_DAY), 00, true);


        //   datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                TimePickerDialog.show();
            }
        });
    }

    public void getdate(final EditText editTextDate) {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(SearchParking.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);
                new TimePickerDialog(SearchParking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        String myFormat = "dd-mm-yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        editTextDate.setText(sdf.format(date.getTime()));
                        // editTextDate.setText(new SimpleDateFormat("dd-MMM-yyyy h:mm").format(date.getTime()));
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        //   datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                datePickerDialog.show();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
