package com.example.sohbetapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.sohbetapp.Claslar.RandomName;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Fragmentİki extends Fragment {
    private View view;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button button;
    private EditText editTextAd,editTextTarih,editTextHakkinda;
    private ImageView imageView,imageViewArkdaslarim;
    private String imageUrl;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Fragment fragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_iki,container,false);
        tanimla();
        bilgiGetir();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guncelle();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeriAc();
            }
        });

        imageViewArkdaslarim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fragment=new FragmentBenimArkadaslarim();
               ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,fragment).commit();
            }
        });
        return view;
    }

    public void tanimla(){
        database=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        reference=database.getReference().child("Users").child(user.getUid());
        button=view.findViewById(R.id.buttonProfilGuncelle);
        editTextAd=view.findViewById(R.id.editAdSoyadProfil);
        editTextTarih=view.findViewById(R.id.editTarihProfil);
        editTextHakkinda=view.findViewById(R.id.edithakkindaProfil);
        imageView=view.findViewById(R.id.imageViewProfil);
        imageViewArkdaslarim=view.findViewById(R.id.imageViewMyFriend);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
    }

    public void bilgiGetir(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users=snapshot.getValue(Users.class);
                editTextAd.setText(users.getAdSoyad());
                editTextHakkinda.setText(users.getHakkinda());
                editTextTarih.setText(users.getTarih());
                imageUrl=users.getProfilFoto();
                if (!users.getProfilFoto().equals("null")){
                    Picasso.get().load(users.getProfilFoto()).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void guncelle(){
        String ad=editTextAd.getText().toString();
        String tarih=editTextTarih.getText().toString();
        String hakkinda=editTextHakkinda.getText().toString();
        Map kullaniciBilgieri=new HashMap();
        kullaniciBilgieri.put("adSoyad",ad);
        kullaniciBilgieri.put("tarih",tarih);
        kullaniciBilgieri.put("hakkinda",hakkinda);
        if (imageUrl.equals("null")){
            kullaniciBilgieri.put("profilFoto","null");
        }else{
            kullaniciBilgieri.put("profilFoto",imageUrl);
        }
        reference.setValue(kullaniciBilgieri).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    getFragmentManager().beginTransaction().replace(R.id.fragmentHome,new Fragmentİki()).commit();
                    Toast.makeText(getContext(),"Basarili",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(),"Hata",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void galeriAc(){
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==5  && resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            StorageReference put=storageReference.child("usersPhoto").child(RandomName.getRandomString() +".jpg");
            put.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                            firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String downloadUrl = uri.toString();
                                    String ad = editTextAd.getText().toString();
                                    String tarih = editTextTarih.getText().toString();
                                    String hakkinda = editTextHakkinda.getText().toString();
                                    Map kullaniciBilgieri = new HashMap();
                                    kullaniciBilgieri.put("adSoyad", ad);
                                    kullaniciBilgieri.put("tarih", tarih);
                                    kullaniciBilgieri.put("hakkinda", hakkinda);
                                    kullaniciBilgieri.put("profilFoto",downloadUrl);
                                    reference.setValue(kullaniciBilgieri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                getFragmentManager().beginTransaction().replace(R.id.fragmentHome, new Fragmentİki()).commit();
                                                Toast.makeText(getContext(), "Basarili", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), "Hata", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                        }
                    });
        }
    }
}

