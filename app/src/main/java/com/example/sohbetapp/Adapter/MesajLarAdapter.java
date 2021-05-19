package com.example.sohbetapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sohbetapp.Models.MesajModel;
import com.example.sohbetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

public class MesajLarAdapter extends RecyclerView.Adapter<MesajLarAdapter.MesajlarAdapterTutucu> {
    private List<String>keys;
    private List<MesajModel>mesajModelList;
    private Context mContext;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private boolean state;
    private int  view_gonderen=1;
    private int view_alan=2;

    public MesajLarAdapter(List<String> keys, List<MesajModel> mesajModelList, Context mContext) {
        this.keys = keys;
        this.mesajModelList = mesajModelList;
        this.mContext = mContext;
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        state=false;
    }

    @NonNull
    @Override
    public MesajlarAdapterTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==view_gonderen){
            view= LayoutInflater.from(mContext).inflate(R.layout.mesaj_gonderen,parent,false);
            return new MesajlarAdapterTutucu(view);
        }else{
            view= LayoutInflater.from(mContext).inflate(R.layout.mesa_gelen,parent,false);
            return new MesajlarAdapterTutucu(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MesajlarAdapterTutucu holder, int position) {
        holder.textView.setText(mesajModelList.get(position).getMesajText());
    }

    @Override
    public int getItemCount() {
        return mesajModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mesajModelList.get(position).getFrom().equals(user.getUid())){
            state=true;
            return view_gonderen;
        }else{
            state=false;
            return view_alan;
        }
    }

    public class MesajlarAdapterTutucu extends RecyclerView.ViewHolder{
        private TextView textView;
        public MesajlarAdapterTutucu(@NonNull View itemView) {
            super(itemView);
            if (state==true){
                textView=itemView.findViewById(R.id.textViewGonderenMesaj);
            }else{
                textView=itemView.findViewById(R.id.textViewGonderenMesaj);
            }
        }
    }
}
