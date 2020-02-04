package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class FicheActivity extends AppCompatActivity {


    private TextView villeMatcherView,typeMatcheView,dateMatchView,prixMatchView,dureeMatchView,heureTextView;
    private ImageView  imageMatchview;
    private Toolbar mToolbar;



    private Button participerbuttom;
    private String getuserId,currentState,currentUserId;
    private FirebaseAuth auth;
    private DatabaseReference Userreference,Matchereference,participerreference;





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);


        mToolbar = findViewById(R.id.toolbar_fiche);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Fiche matche");

        Userreference = FirebaseDatabase.getInstance().getReference().child("Users");
        Matchereference = FirebaseDatabase.getInstance().getReference().child("Matches");
        participerreference= FirebaseDatabase.getInstance().getReference().child("participation");




        auth = FirebaseAuth.getInstance();
        getuserId = getIntent().getExtras().get("userId").toString();
        currentUserId = auth.getCurrentUser().getUid();

        villeMatcherView= findViewById(R.id.fiche_ville_matche);
        typeMatcheView= findViewById(R.id.fiche_type_matche);
        dateMatchView= findViewById(R.id.fiche_date_matche);
        prixMatchView= findViewById(R.id.fiche_prix_matche);
        dureeMatchView= findViewById(R.id.fiche_duree_matche);
        imageMatchview = findViewById(R.id.fiche_image_matche);
        heureTextView = findViewById(R.id.fiche_heure_matche);

        participerbuttom = findViewById(R.id.participer_btn);


        currentState = "new";


        getInformationUser();

    }

    private void getInformationUser()
    {
        Matchereference.child(getuserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("image"))
                {
                    String date = dataSnapshot.child("date").getValue().toString();
                    String heure = dataSnapshot.child("heure").getValue().toString();
                    String duree=  dataSnapshot.child("duree").getValue().toString();
                    String type =  dataSnapshot.child("type").getValue().toString();
                    String prix =  dataSnapshot.child("prix").getValue().toString();
                    String ville =  dataSnapshot.child("ville").getValue().toString();
                    String image= dataSnapshot.child("image").getValue().toString();


                    dateMatchView.setText(date);
                    heureTextView.setText(heure);
                    dureeMatchView.setText(duree);
                    typeMatcheView.setText(type);
                    prixMatchView.setText(prix);
                    villeMatcherView.setText(ville);
                    Picasso.get().load(image).placeholder(R.drawable.matcher).into(imageMatchview);

                    ManageRequest();


                }

                else if (dataSnapshot.exists())
                {
                    String date = dataSnapshot.child("date").getValue().toString();
                    String heure = dataSnapshot.child("heure").getValue().toString();
                    String duree = dataSnapshot.child("duree").getValue().toString();
                    String type = dataSnapshot.child("type").getValue().toString();
                    String prix = dataSnapshot.child("prix").getValue().toString();
                    String ville = dataSnapshot.child("ville").getValue().toString();


                    dateMatchView.setText(date);
                    heureTextView.setText(heure);
                    dureeMatchView.setText(duree);
                    typeMatcheView.setText(type);
                    prixMatchView.setText(prix);
                    villeMatcherView.setText(ville);

                    ManageRequest();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void ManageRequest()
    {
        participerreference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild(getuserId))
                {
                    String request_type = dataSnapshot.child(getuserId).child("request_type").getValue().toString();

                    if (request_type.equals("sent"))
                    {
                        currentState = "request_sent";
                        participerbuttom.setText("Cancel participation Request");
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
        if (!currentUserId.equals(getuserId))
        {
            participerbuttom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    participerbuttom.setEnabled(false);

                    if (currentState.equals("new"))
                    {
                        participeMatche();
                    }

                    if (currentState.equals("request_sent"))
                    {
                        canalParticpiation();
                    }
                }
            });
        }

        else
        {
           participerbuttom.setVisibility(View.INVISIBLE);
        }
    }

    private void canalParticpiation()
    {
        participerreference.child(currentUserId).child(getuserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            participerreference.child(getuserId).child(currentUserId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                participerbuttom.setEnabled(true);
                                                currentState = "new";
                                                participerbuttom.setText("participation");

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void participeMatche()
    {
        participerreference.child(currentUserId).child(getuserId).child("request_type")
                .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                participerreference.child(getuserId).child(currentUserId).child("request_type")
                        .setValue("receved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            participerbuttom.setEnabled(true);
                            currentState = "request_sent";
                            participerbuttom.setText("Cancel participation Request");
                        }
                    }
                });
            }
        });
    }
}
