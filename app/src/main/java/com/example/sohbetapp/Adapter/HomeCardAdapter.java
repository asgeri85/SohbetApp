package com.example.sohbetapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sohbetapp.Fragment.FragmentDigerKullanici;
import com.example.sohbetapp.Models.Users;
import com.example.sohbetapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.HomeAdpterTutucu> {
    private List<String>userKeyList;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private Fragment fragment;

    public HomeCardAdapter(List<String> userKeyList, Context mContext) {
        this.userKeyList = userKeyList;
        this.mContext = mContext;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
    }

    @NonNull
    @Override
    public HomeAdpterTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.home_caardview,parent,false);
        return new HomeAdpterTutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdpterTutucu holder, int position) {
        reference.child("Users").child(userKeyList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotsnapshot) {
               Users users=dataSnapshotsnapshot.getValue(Users.class);
               holder.textView.setText(users.getAdSoyad());
               Picasso.get().load(users.getProfilFoto()).into(holder.imageView);
               if (users.isDurum()==true){
                   holder.imageViewDurum.setImageResource(R.drawable.ic_baseline_circle_green);
               }else{
                   holder.imageViewDurum.setImageResource(R.drawable.ic_baseline_circle_24);
               }
               holder.cardView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Bundle bundle=new Bundle();
                       bundle.putString("key",dataSnapshotsnapshot.getKey());
                       fragment=new FragmentDigerKullanici();
                       fragment.setArguments(bundle);
                       ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,fragment).commit();
                   }
               });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class HomeAdpterTutucu extends RecyclerView.ViewHolder{
        private CardView cardView;
        private TextView textView;
        private ImageView imageView,imageViewDurum;
        public HomeAdpterTutucu(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.crdHome);
            textView=itemView.findViewById(R.id.textViewCard);
            imageView=itemView.findViewById(R.id.imageViewCard);
            imageViewDurum=itemView.findViewById(R.id.homeDurumonline);
        }
    }
}
