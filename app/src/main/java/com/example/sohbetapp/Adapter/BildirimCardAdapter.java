package com.example.sohbetapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sohbetapp.Models.Users;
import com.example.sohbetapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BildirimCardAdapter extends RecyclerView.Adapter<BildirimCardAdapter.BildirimTutuxu> {
    private List<String>keys;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userİd;
    public BildirimCardAdapter(List<String> keys, Context mContext) {
        this.keys = keys;
        this.mContext = mContext;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userİd=user.getUid();
    }

    @NonNull
    @Override
    public BildirimTutuxu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.bildirimler_card,parent,false);
        return new BildirimTutuxu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BildirimTutuxu holder, int position) {
        reference.child("Users").child(keys.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kullanici=snapshot.getValue(Users.class);
                holder.textView.setText(kullanici.getAdSoyad());
                Picasso.get().load(kullanici.getProfilFoto()).into(holder.imageViewProf);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.imageViewRedd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reddEt(userİd,keys.get(position));
            }
        });

        holder.imageViewKabul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kabulEt(userİd,keys.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class BildirimTutuxu extends RecyclerView.ViewHolder {
        private ImageView imageViewProf,imageViewKabul,imageViewRedd;
        private TextView textView;
        public BildirimTutuxu(@NonNull View itemView) {
            super(itemView);
            imageViewKabul=itemView.findViewById(R.id.imageViewBildirimKabul);
            imageViewProf=itemView.findViewById(R.id.imageViewBildirimProf);
            imageViewRedd=itemView.findViewById(R.id.imageViewBildirimRedd);
            textView=itemView.findViewById(R.id.textViewBildirimCardAd);
        }
    }
    public void reddEt(String userİd,String otherİd){
        reference.child("Arkadas_istek").child(userİd).child(otherİd).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadas_istek").child(otherİd).child(userİd).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext,"İstek Geri Çevrildi",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void kabulEt(String userİd,String otjerİd){
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date today= Calendar.getInstance().getTime();
        final String date=dateFormat.format(today);
        reference.child("Arkadaslar").child(userİd).child(otjerİd).child("Tarih").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    reference.child("Arkadaslar").child(otjerİd).child(userİd).child("Tarih").setValue(date).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(mContext,"Arkadaşlık isteği kabul edildi",Toast.LENGTH_SHORT).show();
                                reference.child("Arkadas_istek").child(userİd).child(otjerİd).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        reference.child("Arkadas_istek").child(otjerİd).child(userİd).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
