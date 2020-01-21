package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ResulteActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    RecyclerView recyclerView;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resulte);

        //firewalle
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        this.configeTollbar();

        bottomNavigationView = findViewById(R.id.BoottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.recherche);



        recyclerView = findViewById(R.id.cyleVie);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.recherche:
                        return true;

                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(), MessageActivity.class));
                        overridePendingTransition(0, 0);
                        return true;


                }

                return false;


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser==null) {
            sendUserToLogin();
        }
    }

    private void sendUserToLogin()
    {
        Intent intent = new Intent(ResulteActivity.this,LoginActivity.class);
        startActivity(intent);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_firewalle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_Logout:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(0,0);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    private void configeTollbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

}
