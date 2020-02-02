package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ResulteActivity extends AppCompatActivity {


    BottomNavigationView bottomNavigationView;
    private RecyclerView matcherecyclerView;

    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference matchRefrence;
    private DatabaseReference userRefernece;

    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resulte);






        //firewalle
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        currentUserId = mAuth.getCurrentUser().getUid();




        // base de donne
        matchRefrence = FirebaseDatabase.getInstance().getReference().child("Matches");
        userRefernece = FirebaseDatabase.getInstance().getReference().child("Users");





        this.configeTollbar();

        bottomNavigationView = findViewById(R.id.BoottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.recherche);



        matcherecyclerView = findViewById(R.id.cyleVie);
        matcherecyclerView.setLayoutManager(new LinearLayoutManager(this));



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


        FirebaseRecyclerOptions<Matche> options = new FirebaseRecyclerOptions.Builder<Matche>()
                    .setQuery(matchRefrence,Matche.class)
                    .build();


            FirebaseRecyclerAdapter<Matche,ResultatViewholder> adapter =
                    new FirebaseRecyclerAdapter<Matche, ResultatViewholder>(options)  {
                        @NonNull
                        @Override
                        public ResultatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
                        {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matche_display_layout,parent,false);
                            ResultatViewholder viewholder = new ResultatViewholder(view);
                            return viewholder;
                        }

                        @Override
                        protected void onBindViewHolder(@NonNull final ResultatViewholder holder, final int position, @NonNull Matche model)
                        {

                            holder.typeMatcheView.setText(model.getType());
                            holder.dateMatchView.setText(model.getDate());
                            holder.prixMatchView.setText(model.getPrix());
                            holder.dureeMatchView.setText(model.getDuree());
                            holder.villeUserView.setText(model.getVille());
                            holder.descriptionView.setText(model.getDescription());
                            Picasso.get().load(model.getImage()).placeholder(R.drawable.matcher).into(holder.imageMatchview);




                            String users_id = getRef(position).getKey();

                            userRefernece.child(users_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    if (dataSnapshot.hasChild("image"))
                                    {
                                        String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                                        String retriveUserImage= dataSnapshot.child("image").getValue().toString();

                                        holder.nomUserView.setText(retrivePrenom);
                                         Picasso.get().load(retriveUserImage).placeholder(R.drawable.profile_image).into(holder.imageUserView);

                                    }

                                    else
                                    {
                                        String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();

                                        holder.nomUserView.setText(retrivePrenom);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    String user_id = getRef(position).getKey();
                                    Intent intent = new Intent(ResulteActivity.this,FicheActivity.class);
                                    intent.putExtra("userId",user_id);
                                    startActivity(intent);

                                }
                            });




                        }
                    };

            matcherecyclerView.setAdapter(adapter);
            adapter.startListening();


    }

    public static class ResultatViewholder extends RecyclerView.ViewHolder
    {
        TextView nomUserView,villeUserView,typeMatcheView,dateMatchView,prixMatchView,dureeMatchView,descriptionView;
        CircleImageView imageUserView;
        ImageView imageMatchview;

        public ResultatViewholder(@NonNull View itemView)
        {
            super(itemView);
            nomUserView= itemView.findViewById(R.id.user_name);
            villeUserView= itemView.findViewById(R.id.ville_matche);
            typeMatcheView= itemView.findViewById(R.id.type_matche);
            dateMatchView= itemView.findViewById(R.id.date_matche);
            prixMatchView= itemView.findViewById(R.id.prix_matche);
            dureeMatchView= itemView.findViewById(R.id.duree_matche);
            descriptionView= itemView.findViewById(R.id.user_description_matche);

            imageUserView = itemView.findViewById(R.id.imageView_userProfile);
            imageMatchview = itemView.findViewById(R.id.image_matche);
        }


    }

    private void MatcheSearche(String searche)
    {

        FirebaseRecyclerOptions<Matche> options;


        String query = searche.toLowerCase();
        Query firebaseSerachQuery = matchRefrence.orderByChild("ville").startAt(query).endAt(query + "\uf8ff");

        Log.i("sql","resultat" + firebaseSerachQuery);
        options  = new FirebaseRecyclerOptions.Builder<Matche>().setQuery(firebaseSerachQuery,Matche.class).build();




        FirebaseRecyclerAdapter<Matche,ResultatViewholder> adapter =
        new FirebaseRecyclerAdapter<Matche, ResultatViewholder>(options) {
            @NonNull
            @Override
            public ResultatViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.matche_display_layout,parent,false);
                ResultatViewholder viewholder = new ResultatViewholder(view);
                return viewholder;
            }

            @Override
            protected void onBindViewHolder(@NonNull final ResultatViewholder holder, final int position, @NonNull Matche model)
            {
                holder.typeMatcheView.setText(model.getType());
                holder.dateMatchView.setText(model.getDate());
                holder.prixMatchView.setText(model.getPrix());
                holder.dureeMatchView.setText(model.getDuree());
                holder.villeUserView.setText(model.getVille());
                holder.descriptionView.setText(model.getDescription());
                Picasso.get().load(model.getImage()).placeholder(R.drawable.matcher).into(holder.imageMatchview);




                String users_id = getRef(position).getKey();

                userRefernece.child(users_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.hasChild("image"))
                        {
                            String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                            String retriveUserImage= dataSnapshot.child("image").getValue().toString();

                            holder.nomUserView.setText(retrivePrenom);
                            Picasso.get().load(retriveUserImage).placeholder(R.drawable.profile_image).into(holder.imageUserView);

                        }

                        else
                        {
                            String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();

                            holder.nomUserView.setText(retrivePrenom);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String user_id = getRef(position).getKey();
                        Intent intent = new Intent(ResulteActivity.this,FicheActivity.class);
                        intent.putExtra("userId",user_id);
                        startActivity(intent);

                    }
                });
            }

        };
        matcherecyclerView.setAdapter(adapter);
        adapter.startListening();




    }



    private void sendUserToLogin()
    {
        Intent intent = new Intent(ResulteActivity.this,LoginActivity.class);
        startActivity(intent);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_matche, menu);
        MenuItem item = menu.findItem(R.id.menu_serache);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                 MatcheSearche(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                MatcheSearche(newText);
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_Logout:
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                overridePendingTransition(0,0);
                return true;

            case R.id.menu_serache:
                return true;

        }


        return super.onOptionsItemSelected(item);

    }





    private void configeTollbar() {
        mToolbar = findViewById(R.id.toolbar_matche);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("cherche une matche");


    }

}





