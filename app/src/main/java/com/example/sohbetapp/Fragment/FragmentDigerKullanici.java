package com.example.sohbetapp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sohbetapp.Activity.MainActivity;
import com.example.sohbetapp.Activity.MesajlarActivity;
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

public class FragmentDigerKullanici extends Fragment {
    private TextView textViewAd,textViewtARİH,textViewHakkinda,textViewArkadas,textViewBegeni;
    private View view;
    private String otherId,userId;
    private FirebaseDatabase database;
    private DatabaseReference reference,refArkadas;
    private ImageView imageViewFoto,imageViewArkadas,imageViewlike,imageViewMesaj;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String kontrol="",begeni="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.diger_kullanici_prof,container,false);
        tanimla();
        bilgiGetir();

        imageViewArkadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (kontrol.equals("alan")){
                    arkadasİptalEt(otherId,userId);
                }else if (kontrol.equals("arkadas")){
                    arkdaslikBitir(userId,otherId);
                }else{
                    arkadasEkle(otherId,userId);
                }
            }
        });

        imageViewlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (begeni.equals("")){
                    begen(userId,otherId);
                }else{
                    begenmeİptal(userId,otherId);
                }
            }
        });

        imageViewMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MesajlarActivity.class);
                intent.putExtra("ad",textViewAd.getText().toString());
                intent.putExtra("id",otherId);
                startActivity(intent);
            }
        });

        begeniSayi(otherId);
        arkadasGetir(otherId);
        return view;
    }

    public void tanimla(){
        otherId=getArguments().getString("key");
        textViewAd=view.findViewById(R.id.textViewDigerAd);
        textViewtARİH=view.findViewById(R.id.textViewDigerDogum);
        textViewHakkinda=view.findViewById(R.id.textViewDigerHakkinda);
        imageViewFoto=view.findViewById(R.id.imageViewDigerProfil);
        imageViewArkadas=view.findViewById(R.id.imageViewArkadsEkle);
        imageViewlike=view.findViewById(R.id.imageViewLike);
        textViewArkadas=view.findViewById(R.id.textViewDigerTakipci);
        textViewBegeni=view.findViewById(R.id.textViewDigerBegeni);
        imageViewMesaj=view.findViewById(R.id.imageViewMesajGonder);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();
        refArkadas=database.getReference("Arkadas_istek");
        refArkadas.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userId)){
                    kontrol=snapshot.child(userId).child("tip").getValue().toString();
                    imageViewArkadas.setImageResource(R.drawable.ic_baseline_person_remove_24);
                }else{
                    imageViewArkadas.setImageResource(R.drawable.ic_baseline_person_add_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(userId)){
                    kontrol="arkadas";
                    imageViewArkadas.setImageResource(R.drawable.ic_baseline_cancel_24);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.hasChild(userId)){
                  begeni=snapshot.child(userId).child("islem").getValue().toString();
                  imageViewlike.setImageResource(R.drawable.ic_baseline_favorite_24);
              }else{
                  imageViewlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void bilgiGetir(){
        reference.child("Users").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                textViewAd.setText(users.getAdSoyad());
                textViewHakkinda.setText("Hakkında: "+users.getHakkinda());
                textViewtARİH.setText("Doğum tarihi: "+users.getTarih());
                Picasso.get().load(users.getProfilFoto()).into(imageViewFoto);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void arkadasEkle(String other,String user){
        refArkadas.child(user).child(other).child("tip").setValue("gonderen").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    refArkadas.child(other).child(user).child("tip").setValue("alan").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                kontrol="alan";
                                imageViewArkadas.setImageResource(R.drawable.ic_baseline_person_remove_24);
                                Toast.makeText(getContext(),"Arkadaşlık istegi gonderildi",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(),"Hata meydana geldi",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void arkadasİptalEt(String otherId,String userId){
        refArkadas.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                refArkadas.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol="";
                        imageViewArkadas.setImageResource(R.drawable.ic_baseline_person_add_24);
                        Toast.makeText(getContext(),"Arkadaşlık isteği iptal edildi",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void arkdaslikBitir(String userId,String otherId){
        reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        imageViewArkadas.setImageResource(R.drawable.ic_baseline_person_add_24);
                        Toast.makeText(getContext(),"Arkadaşlık isteği iptal edildi",Toast.LENGTH_SHORT).show();
                        arkadasGetir(otherId);
                    }
                });
            }
        });
    }

    public void begen(String userId,String otherId){
        reference.child("Begeniler").child(otherId).child(userId).child("islem").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    begeni="begendi";
                    imageViewlike.setImageResource(R.drawable.ic_baseline_favorite_24);
                    Toast.makeText(getContext(),"Begenildi",Toast.LENGTH_SHORT).show();
                    begeniSayi(otherId);
                }
            }
        });
    }

    public void begenmeİptal(String userId,String otherId){
        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    begeni ="";
                    imageViewlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    Toast.makeText(getContext(),"Beğeni geri çekildi",Toast.LENGTH_SHORT).show();
                    begeniSayi(otherId);
                }
            }
        });

    }

    public void begeniSayi(String otherId){
        reference.child("Begeniler").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             textViewBegeni.setText(snapshot.getChildrenCount()+" Beğeni");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void arkadasGetir(String otherId){
        reference.child("Arkadaslar").child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textViewArkadas.setText(snapshot.getChildrenCount()+"  Arkadaş");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
