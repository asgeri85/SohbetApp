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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText editTextMail,editTextSifre;
    private Button buttonGiris,buttonKayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextMail=findViewById(R.id.editGrismail);
        editTextSifre=findViewById(R.id.editGirisSifre);
        buttonGiris=findViewById(R.id.buttonGiris);
        buttonKayit=findViewById(R.id.buttonKayitOlGiris);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=editTextMail.getText().toString();
                String sifre=editTextSifre.getText().toString();
                if (!mail.isEmpty() && !sifre.isEmpty()){
                    girisYap(mail,sifre);
                }else{
                    Toast.makeText(getApplicationContext(),"Tüm alanları doldurunuz",Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, KayitOlActivity.class));
            }
        });
    }

    public void girisYap(String mail,String sifre){
        auth.signInWithEmailAndPassword(mail,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Giris Basarili",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Hatali Bilgiler",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}