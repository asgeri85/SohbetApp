package com.example.sohbetapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.sohbetapp.Adapter.MesajLarAdapter;
import com.example.sohbetapp.Models.MesajModel;
import com.example.sohbetapp.Models.Users;
import com.example.sohbetapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.hdodenhof.circleimageview.CircleImageView;

public class MesajlarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RecyclerView rv;
    private EditText editText;
    private String ad,otherİd,userİd;
    private CircleImageView imageView;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<MesajModel>mesajModelList;
    private List<String>keyList;
    private MesajLarAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajlar);
        toolbar=findViewById(R.id.toolbarMEsajlar);
        fab=findViewById(R.id.floatingActionButton);
        rv=findViewById(R.id.recyclerViewMesaj);
        editText=findViewById(R.id.editMesaj);
        imageView=findViewById(R.id.profile_image);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        ad= ((String) getIntent().getSerializableExtra("ad"));
        otherİd= (String) getIntent().getSerializableExtra("id");
        userİd=user.getUid();
        toolbar.setTitle(ad);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bilgiGetir(otherİd);
        mesajModelList=new ArrayList<>();
        keyList=new ArrayList<>();
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new MesajLarAdapter(keyList,mesajModelList,getApplicationContext());
        rv.setAdapter(adapter);
        mesajGetir(userİd,otherİd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=editText.getText().toString();
                DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date today= Calendar.getInstance().getTime();
                final String date=dateFormat.format(today);
                mesajGonder(userİd,otherİd,"text",text,false,date);
            }
        });

    }

    public void bilgiGetir(String otherİd){
        reference.child("Users").child(otherİd).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users kullanici=snapshot.getValue(Users.class);
                Picasso.get().load(kullanici.getProfilFoto()).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void mesajGonder(String userİd,String otherİd,String textType,String mesaj,boolean okudnu,String date){
        String mesajİd=reference.child("Mesajlar").child(userİd).child(otherİd).push().getKey();
        Map mesajMap=new HashMap();
        mesajMap.put("textType",textType);
        mesajMap.put("Date",date);
        mesajMap.put("OkunduMu",okudnu);
        mesajMap.put("mesajText",mesaj);
        mesajMap.put("from",userİd);

        reference.child("Mesajlar").child(userİd).child(otherİd).child(mesajİd).setValue(mesajMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherİd).child(userİd).child(mesajİd).setValue(mesajMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        editText.setText("");
                    }
                });
            }
        });
    }

    public void mesajGetir(String userİd,String otherİd){
        reference.child("Mesajlar").child(userİd).child(otherİd).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MesajModel model=snapshot.getValue(MesajModel.class);
                mesajModelList.add(model);
                keyList.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
                rv.scrollToPosition(mesajModelList.size()-1);
                reference.child("Users").child(userİd).child("durum").setValue(true);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}