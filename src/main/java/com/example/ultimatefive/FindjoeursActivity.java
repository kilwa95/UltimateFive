package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FindjoeursActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView FindFriendsRecyclerView;
    private DatabaseReference userRefernece;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findjoeurs);


        // base de donnes

        userRefernece = FirebaseDatabase.getInstance().getReference().child("Users");

        FindFriendsRecyclerView = findViewById(R.id.find_freinds_RecyclerView);
        FindFriendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // toolbar
        mToolbar = findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find joeurs");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(userRefernece,User.class)
                .build();

        FirebaseRecyclerAdapter<User,FindfreindsViewholder> adapter =
                new FirebaseRecyclerAdapter<User, FindfreindsViewholder>(options) {
                    @NonNull
                    @Override
                    public FindfreindsViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout,viewGroup,false);
                        FindfreindsViewholder viewholder = new FindfreindsViewholder(view);
                        return viewholder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull FindfreindsViewholder holder, final int position, @NonNull User model)
                    {
                        holder.userName.setText(model.getNom());
                        holder.userVille.setText(model.getVille());
                        Picasso.get().load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String user_id = getRef(position).getKey();
                                Intent intent = new Intent(FindjoeursActivity.this,ProfilejoueurActivity.class);
                                intent.putExtra("userId",user_id);
                                startActivity(intent);
                            }
                        });

                    }
                };

        FindFriendsRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class FindfreindsViewholder extends RecyclerView.ViewHolder
    {

        TextView userName,userVille;
        CircleImageView profileImage;

        public FindfreindsViewholder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userVille = itemView.findViewById(R.id.user_profile_ville);
            profileImage = itemView.findViewById(R.id.users_profile_image);

        }
    }
}
