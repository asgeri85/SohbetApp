package com.example.sohbetapp.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetapp.Adapter.BildirimCardAdapter;
import com.example.sohbetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentBildirim extends Fragment {
    private View view;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private String userİd;
    private RecyclerView rv;
    private ArrayList<String>keyList;
    private BildirimCardAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.bildirim_fragment,container,false);
        tanimla();
        istekGetir(userİd);
        return view;
    }

    public void tanimla(){
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Arkadas_istek");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userİd=user.getUid();
        rv=view.findViewById(R.id.rvBildirim);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        keyList=new ArrayList<>();
        adapter=new BildirimCardAdapter(keyList,getContext());
        rv.setAdapter(adapter);
    }

    public void istekGetir(String userİd){
        reference.child(userİd).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String kontrol=snapshot.child("tip").getValue().toString();
                    if (kontrol.equals("alan")){
                        if (keyList.indexOf(snapshot.getKey())==-1){
                            keyList.add(snapshot.getKey());
                        }
                        adapter.notifyDataSetChanged();
                    }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                keyList.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
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
