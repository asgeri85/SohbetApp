package com.example.sohbetapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sohbetapp.Fragment.FragmentBildirim;
import com.example.sohbetapp.Fragment.FragmentHome;
import com.example.sohbetapp.Fragment.Fragmentİki;
import com.example.sohbetapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    private Fragment tempFragment;
    private Toolbar toolbar;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.bootomAnaSayfa);
        toolbar=findViewById(R.id.toolbarHome);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        toolbar.setTitle("Sohbet App");
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentHome,new FragmentHome()).commit();

        if (user==null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else{
            reference.child("Users").child(user.getUid()).child("durum").setValue(true);
        }


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.action_home){
                    tempFragment=new FragmentHome();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,tempFragment).commit();
                }
                if (item.getItemId()==R.id.action_iki){
                    tempFragment=new Fragmentİki();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,tempFragment).commit();
                }
                if (item.getItemId()==R.id.action_uc){
                    //tempFragment=new FragmentUc();
                    reference.child("Users").child(user.getUid()).child("durum").setValue(false);
                    mAuth.signOut();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bildirim:
               getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHome,new FragmentBildirim()).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.child("Users").child(user.getUid()).child("durum").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.child("Users").child(user.getUid()).child("durum").setValue(true);
    }
}