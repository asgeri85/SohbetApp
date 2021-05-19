package com.example.sohbetapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetapp.Adapter.HomeCardAdapter;
import com.example.sohbetapp.Models.Users;
import com.example.sohbetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentHome extends Fragment{
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private HomeCardAdapter adapter;
    private RecyclerView rv;
    private View view;
    private ArrayList<String>key;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home,container,false);
        tanimla();
        adamGetir();

        return view;
    }
    public void tanimla(){
        key=new ArrayList<>();
       database=FirebaseDatabase.getInstance();
       reference=database.getReference();
       auth=FirebaseAuth.getInstance();
       user=auth.getCurrentUser();
       rv=view.findViewById(R.id.rvHome);
       rv.setHasFixedSize(true);
       rv.setLayoutManager(new LinearLayoutManager(getContext()));
       adapter=new HomeCardAdapter(key,getContext());
       rv.setAdapter(adapter);
    }

    public void adamGetir(){
        reference.child("Users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               reference.child("Users").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshotsnapshot) {
                       Users users=dataSnapshotsnapshot.getValue(Users.class);
                       if (!users.getAdSoyad().equals("null") && !dataSnapshotsnapshot.getKey().equals(user.getUid())){
                           if (key.indexOf(dataSnapshotsnapshot.getKey())==-1){
                               key.add(dataSnapshotsnapshot.getKey());
                           }
                           adapter.notifyDataSetChanged();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
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
