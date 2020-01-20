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
    MyAdapter myAdapter;

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

        myAdapter = new MyAdapter(this, getMyList());
        recyclerView.setAdapter(myAdapter);


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


    private ArrayList<Model> getMyList() {
        ArrayList<Model> models = new ArrayList<>();

        Model m = new Model();
        m.setNomOrganisateur("Khaled");
        m.setImage(R.drawable.paris);
        m.setAdresse("35 quai de grenelle");
        m.setDate("12/2/2020");
        m.setHoraire("13:18");
        m.setTypeTerraine("grande terraine");
        m.setLieu("paris");
        models.add(m);


        Model m2 = new Model();
        m2.setNomOrganisateur("pierre");
        m2.setImage(R.drawable.lyon);
        m2.setAdresse("35 quai de grenelle");
        m2.setDate("12/2/2020");
        m2.setHoraire("13:18");
        m2.setTypeTerraine("grande terraine");
        m2.setLieu("lyon");
        models.add(m2);


        Model m3 = new Model();
        m3.setNomOrganisateur("bastien");
        m3.setImage(R.drawable.madrid);
        m3.setAdresse("35 quai de grenelle");
        m3.setDate("12/2/2020");
        m3.setHoraire("13:18");
        m3.setTypeTerraine("grande terraine");
        m3.setLieu("lyon");
        models.add(m3);


        Model m4 = new Model();
        m4.setNomOrganisateur("Martien");
        m4.setImage(R.drawable.rome);
        m4.setAdresse("35 quai de grenelle");
        m4.setDate("12/2/2020");
        m4.setHoraire("13:18");
        m4.setTypeTerraine("grande terraine");
        m4.setLieu("lyon");
        models.add(m4);

        return models;
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
