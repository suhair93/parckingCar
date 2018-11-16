package com.parcking.employee;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.parcking.R;

import com.parcking.adapter.ParkAdapter;
import com.parcking.models.Employee;
import com.parcking.models.Keys;
import com.parcking.models.ParkingItem;
import com.parcking.models.parking;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import mehdi.sakout.dynamicbox.DynamicBox;

public class stiuation extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference ref;
    StorageReference storageReference;
    RecyclerView recyclerView;
    public static final String STORAGE_PATH_UPLOADS = "uploads_car/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    //uri to store file
    private Uri filePath;
    List<parking> parkingList =new ArrayList<>();
    List<ParkingItem> itemList =new ArrayList<>();
    EditText  situation;
    ImageButton img;
    Button add ;

    ProgressDialog dialog;
    com.parcking.adapter.ParkAdapter ParkAdapter;
    SharedPreferences prefs;
    String idorg="",nameorg="",emai="",name="";
    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stiuation);

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);


        prefs =  getSharedPreferences("parking", MODE_PRIVATE);
        idorg = prefs.getString(Keys.KEY_ORGNIZATION_ID, "");
        nameorg = prefs.getString(Keys.KEY_ORGNIZATION_NAME, "");
        emai = prefs.getString(Keys.KEY_EMPLOYEE, "");
        name = prefs.getString(Keys.KEY_NAME, "");



        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        ParkAdapter = new ParkAdapter(this, parkingList);
        recyclerView.setAdapter(ParkAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //box = new DynamicBox(add_parking.this,recyclerView);
        // box.showLoadingLayout();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        ref.child("stiuation").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                parkingList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    parking parking = snapshot.getValue(parking.class);
                    if(parking.getEmailEmployee().equals(emai)){
                        parkingList.add(parking);
                        ParkAdapter.notifyDataSetChanged();}
                }

                Collections.reverse(parkingList);
               // box.hideAll();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        situation = findViewById(R.id.situation);

        img = findViewById(R.id.situation_img);

        add = findViewById(R.id.add);

        getPIC();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();

            }
        });






    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //      car.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR =  getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(stiuation.this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            final StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog


                            parking car = new parking();
                            car.setName(situation.getText().toString());
                            car.setImg(taskSnapshot.getDownloadUrl().toString());
                            car.setEmailEmployee(emai);
                            car.setIdOrg(idorg);
                            for (int i =0 ; i<= 24 ; i++){
                                itemList.add(new ParkingItem(i,false,""));
                            }
                           Calendar myCalendar = Calendar.getInstance();
                            String myFormat = "dd/MM/yyyy";
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                            car.setDate(sdf.format(myCalendar.getTime()) + "");

                            parkingList.add(car);
                            situation.setText("");
                            ParkAdapter.notifyDataSetChanged();

                              ref.child("stiuation").push().setValue(car);
                            Toast.makeText(getBaseContext(), "image Uploaded ", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getBaseContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            //   Toast.makeText(getBaseContext(),"Done",Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }
    private void getPIC() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getBaseContext().getPackageName()));
            startActivity(intent);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getBaseContext(),"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getBaseContext() ,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}
