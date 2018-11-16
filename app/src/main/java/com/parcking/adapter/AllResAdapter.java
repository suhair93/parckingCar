package com.parcking.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parcking.customer.SearchParking;
import com.parcking.R;
import com.parcking.models.AllResrvation;

import java.util.List;


public class AllResAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllResrvation> list ;
    private Context context;
    public AllResAdapter(Context context, List<AllResrvation> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_reservation, parent, false));

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Holder  Holder = (Holder) holder;
        AllResrvation parking = list.get(position);

         Holder.id_park.setText(parking.getId_park());
        Holder.from.setText(parking.getFrom());
        Holder.to.setText(parking.getTo());

        Holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SearchParking.class);
                context.startActivity(i);
            }
        });


        Holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

                View views = inflater.inflate(R.layout.dialoge_barcode, null);
                alertDialog.setView(views);

                ImageView imageView=(ImageView)views.findViewById(R.id.imageView);

                alertDialog.setNegativeButton(
                        context.getResources().getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

//                String text=""+model.getID() ;// Whatever you need to encode in the QR code
//                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
//                try {
//                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
//                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
//                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
//                    imageView.setImageBitmap(bitmap);
//                } catch (WriterException e) {
//                    e.printStackTrace();
//                }
                alertDialog.show();
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
        TextView id_park,from,to ;
        Button edit;
        CardView cardView;
        public Holder(View itemView) {
            super(itemView);

            id_park =   itemView.findViewById(R.id.parking);
            from =  itemView.findViewById(R.id.from);
            to =  itemView.findViewById(R.id.to);
            edit =  itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardview);


        }

    }
}






