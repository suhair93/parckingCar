package com.parcking.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.parcking.R;
import com.parcking.models.Employee;
import com.parcking.models.Keys;
import com.parcking.models.Request;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class RequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Request> list ;
    private Context context;
    FirebaseDatabase database;
    DatabaseReference ref;
    public RequestAdapter(Context context, List<Request> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Holder newsHolder = (Holder) holder;
        final Request request = list.get(position);
        newsHolder.name.setText(request.getNameEmployee());
       newsHolder.accept.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               database = FirebaseDatabase.getInstance();
               ref = database.getReference();
               Query fireQuery = ref.child("Request").orderByChild("emailEmployee").equalTo(request.emailEmployee);
               fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       // ازا غير موجود قم بتخزينه
                       if (dataSnapshot.getValue() != null) {


                           for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                               Request request = snapshot.getValue(Request.class);
                               request.setStatus(true);
                               snapshot.getRef().setValue(request);
                               Toast.makeText(context, "accept ", Toast.LENGTH_LONG).show();
                           }


                       } else {
                           // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة




                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                       // رساله خطأ
                       Toast.makeText(context, "no connection internet ", Toast.LENGTH_LONG).show();

                   }
               });
           }
       });

       newsHolder.reject.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               database = FirebaseDatabase.getInstance();
               ref = database.getReference();
               Query fireQuery = ref.child("Request").orderByChild("emailEmployee").equalTo(request.emailEmployee);
               fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {
                       // ازا غير موجود قم بتخزينه
                       if (dataSnapshot.getValue() != null) {


                           for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                               Request request = snapshot.getValue(Request.class);
                               request.setStatus(false);
                               snapshot.getRef().setValue(request);
                               Toast.makeText(context, "reject ", Toast.LENGTH_LONG).show();
                           }


                       } else {
                           // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة




                       }
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                       // رساله خطأ
                       Toast.makeText(context, "no connection internet ", Toast.LENGTH_LONG).show();

                   }
               });
           }
       });

       newsHolder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
               LayoutInflater inflater = (LayoutInflater)  context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

               View views = inflater.inflate(R.layout.activity_add_parking, null);
               alertDialog.setView(views);
               final AlertDialog dialog = alertDialog.create();

               dialog.show();

               database = FirebaseDatabase.getInstance();
               ref = database.getReference();

               final EditText  info = views.findViewById(R.id.info);
               final EditText price= views.findViewById(R.id.price);
               final EditText days = views.findViewById(R.id.day);
               final EditText time = views.findViewById(R.id.time);
               final Button save = views.findViewById(R.id.save);
               save.setVisibility(View.GONE);

               Query fireQuery = ref.child("Request").orderByChild("emailEmployee").equalTo(request.emailEmployee);
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
                       Toast.makeText(context, "no connection internet ", Toast.LENGTH_LONG).show();

                   }






               });

              // dialog.dismiss();

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
        TextView name ;
        Button reject,accept;
        CardView cardView;
        public Holder(View itemView) {
            super(itemView);

            name =   itemView.findViewById(R.id.name);
             reject = itemView.findViewById(R.id.reject);
             accept = itemView.findViewById(R.id.accept);
           cardView =  itemView.findViewById(R.id.cardview);


        }

    }
}






