package com.example.sohbetapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sohbetapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity {
    private Button buttonkayitOl;
    private EditText editTextmail,editTextSifer;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        buttonkayitOl=findViewById(R.id.buttonKayitOl);
        editTextmail=findViewById(R.id.editmailKayitOl);
        editTextSifer=findViewById(R.id.editSifreKayitOl);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        buttonkayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=editTextmail.getText().toString();
                String sifre=editTextSifer.getText().toString();
                if (!mail.isEmpty() && !sifre.isEmpty()){
                    kayitol(mail,sifre);
                }else{
                    Toast.makeText(getApplicationContext(),"Alanlar Bos olamaz",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void kayitol(String mail,String sifre){
        auth.createUserWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    reference=database.getReference().child("Users").child(auth.getUid());
                    Map kullaniciBilgieri=new HashMap();
                    kullaniciBilgieri.put("adSoyad","null");
                    kullaniciBilgieri.put("profilFoto","null");
                    kullaniciBilgieri.put("tarih","null");
                    kullaniciBilgieri.put("hakkinda","null");
                    kullaniciBilgieri.put("durum",true);
                    reference.setValue(kullaniciBilgieri);
                    Toast.makeText(getApplicationContext(),"Basarili",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(KayitOlActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Hata",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}