package com.example.sohbetapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetapp.Models.Users;
import com.example.sohbetapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BenimArkadasAdapter extends RecyclerView.Adapter<BenimArkadasAdapter.BenimArkdasAdapteTutucur>{
    private List<String>keys;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    public BenimArkadasAdapter(List<String> keys, Context mContext) {
        this.keys = keys;
        this.mContext = mContext;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }

    @NonNull
    @Override
    public BenimArkdasAdapteTutucur onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.benim_arkadas_cardview,parent,false);
        return new BenimArkdasAdapteTutucur(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BenimArkdasAdapteTutucur holder, int position) {
        reference.child("Users").child(keys.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kullanici=snapshot.getValue(Users.class);
                holder.textView.setText(kullanici.getAdSoyad());
                Picasso.get().load(kullanici.getProfilFoto()).into(holder.imageViewProf);
                if (kullanici.isDurum()==true){
                    holder.imageViewOnline.setImageResource(R.drawable.ic_baseline_circle_green);
                }else{
                    holder.imageViewOnline.setImageResource(R.drawable.ic_baseline_circle_24);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class BenimArkdasAdapteTutucur extends RecyclerView.ViewHolder{
        private TextView textView;
        private ImageView imageViewProf,imageViewOnline;
        private CardView cardView;
        public BenimArkdasAdapteTutucur(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textViewBenimCardAd);
            imageViewProf=itemView.findViewById(R.id.imageViewBenimArProf);
            imageViewOnline=itemView.findViewById(R.id.ArkadasDurumOnline);
        }
    }
}
