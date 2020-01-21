package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private DatabaseReference MatcheRef;
    private String cuuentUserId;

    private TextView dateTextView,heuretextView,dureetextview,typeTextview,prixTextview,villetextView;
    private ImageView imageMatcheview;
    private Button creeMatchbuttom;



    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        bottomNavigationView=findViewById(R.id.BoottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.add);

        //firewalle
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        MatcheRef = FirebaseDatabase.getInstance().getReference();
        cuuentUserId = mAuth.getCurrentUser().getUid();

        //toolbar
        toolbar =  findViewById(R.id.toolbar_add_matche);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // forumliare de matche
        dateTextView =  findViewById(R.id.date_text_view);
        heuretextView =  findViewById(R.id.heure_text_view);
        dureetextview=  findViewById(R.id.duree_text_view);
        typeTextview=  findViewById(R.id.type_text_view);
        prixTextview =  findViewById(R.id.prix_text_view);
        villetextView = findViewById(R.id.ville_text_view);
        creeMatchbuttom = findViewById(R.id.cree_matche);

        creeMatchbuttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                creeMatche();
            }
        });




        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {




                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.recherche:
                        startActivity(new Intent(getApplicationContext(),ResulteActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.add:
                        return true;


                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), profileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.message:
                        startActivity(new Intent(getApplicationContext(),MessageActivity.class));
                        overridePendingTransition(0,0);
                        return true;


                }

                return false;



            }
        });


    }

    private void creeMatche()
    {
        String date = dateTextView.getText().toString();
        String heure = heuretextView.getText().toString();
        String duree= dureetextview.getText().toString();
        String type = typeTextview.getText().toString();
        String prix = prixTextview.getText().toString();
        String ville = villetextView.getText().toString();

        String messageKey = MatcheRef.push().getKey();










        if (TextUtils.isEmpty(date)&&
                TextUtils.isEmpty(heure)&&
                TextUtils.isEmpty(duree)&&
                TextUtils.isEmpty(type)&&
                TextUtils.isEmpty(ville))
        {
            Toast.makeText(AddActivity.this,"veuiller remplir toute les champs...",Toast.LENGTH_SHORT).show();

        }

        else
        {

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("date", date);
            hashMap.put("heure", heure);
            hashMap.put("duree",duree);
            hashMap.put("type", type);
            hashMap.put("prix",prix);
            hashMap.put("ville",ville);

            MatcheRef.child("Matches").child(messageKey).child(cuuentUserId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    if(task.isSuccessful())
                    {


                        Toast.makeText(AddActivity.this, "matche saved ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddActivity.this,ResulteActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                    else
                    {
                        Toast.makeText(AddActivity.this, "matche no saved  ", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }




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
        Intent intent = new Intent(AddActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_firewalle,menu);
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
}
