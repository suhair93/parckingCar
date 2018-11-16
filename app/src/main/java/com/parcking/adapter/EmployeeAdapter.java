package com.parcking.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.models.Employee;

import java.util.List;


public class EmployeeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database;
    DatabaseReference ref;
    private List<Employee> list ;
    private Context context;
    public EmployeeAdapter(Context context, List<Employee> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_employee, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Holder newsHolder = (Holder) holder;
        final Employee employee = list.get(position);

        newsHolder.name.setText(employee.getName());
        newsHolder.email.setText(employee.getEmail());
        newsHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater)  context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.activity_add__employee, null);
                alertDialog.setView(views);
                final AlertDialog dialog = alertDialog.create();

                dialog.show();

                database = FirebaseDatabase.getInstance();
                ref = database.getReference();

                final EditText name=(EditText)views.findViewById(R.id.name);
                final EditText email=(EditText)views.findViewById(R.id.email);
                final EditText password=(EditText)views.findViewById(R.id.password);
                final EditText address=(EditText)views.findViewById(R.id.address);
                final EditText mobile=(EditText)views.findViewById(R.id.mobile);

                 name.setText(employee.getName().toString());
                 email.setText(employee.getEmail().toString());
                 password.setText(employee.getPassword().toString());
                 address.setText(employee.getCity().toString());
                 mobile.setText(employee.getPhone().toString());



                Button send=(Button)views.findViewById(R.id.save);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        employee.setName(name.getText().toString());
                        employee.setEmail(email.getText().toString());
                        employee.setPassword(password.getText().toString());
                        employee.setCity(address.getText().toString());
                        employee.setPhone(mobile.getText().toString());
                        final Query query1 = ref.child("employee").orderByChild("email").equalTo(employee.getEmail().toString());
                        final Query query2 = ref.child("user").orderByChild("email").equalTo(employee.getEmail().toString());
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    snapshot.getRef().setValue(employee);
                                    notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        query2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                    snapshot.getRef().setValue(employee);
                                    //   notifyDataSetChanged();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        dialog.dismiss();
                    }


                });




            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);

    }
    public class Holder extends RecyclerView.ViewHolder {
        TextView name,email ;
        Button edit;
        public Holder(View itemView) {
            super(itemView);

            name =   itemView.findViewById(R.id.name);
            email =  itemView.findViewById(R.id.email);
            edit =  itemView.findViewById(R.id.edit);


        }

    }
}






