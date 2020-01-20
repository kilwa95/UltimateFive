package com.example.ultimatefive;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;


public class FriendsFragment extends Fragment {

    private View freindsView;
    private RecyclerView friendsRecyclerView;

    private DatabaseReference ContactRefernece,userRefernece;
    private FirebaseAuth auth;
    private String currentsUserId;



    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        freindsView =  inflater.inflate(R.layout.fragment_friends, container, false);

        friendsRecyclerView = freindsView.findViewById(R.id.friends_list);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // base de donnes
        auth = FirebaseAuth.getInstance();
        currentsUserId = auth.getCurrentUser().getUid();
       ContactRefernece = FirebaseDatabase.getInstance().getReference().child("contacts").child(currentsUserId);
       userRefernece = FirebaseDatabase.getInstance().getReference().child("Users");

        return freindsView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ContactRefernece,User.class)
                .build();
        FirebaseRecyclerAdapter<User, freindViewholder> adapter =
               new FirebaseRecyclerAdapter<User, freindViewholder>(options) {
                   @Override
                   protected void onBindViewHolder(@NonNull final freindViewholder holder, final int position, @NonNull final User model)
                   {
                       String user_id = getRef(position).getKey();

                       userRefernece.child(user_id).addValueEventListener(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                           {
                               if (dataSnapshot.hasChild("image"))
                               {
                                   String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                                   String retriveville= dataSnapshot.child("ville").getValue().toString();
                                   String retriveUserImage= dataSnapshot.child("image").getValue().toString();

                                   holder.userName.setText(retrivePrenom);
                                   holder.userVille.setText(retriveville);
                                   Picasso.get().load(retriveUserImage).placeholder(R.drawable.profile_image).into(holder.profileImage);

                               }

                               else
                               {
                                   String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                                   String retriveville= dataSnapshot.child("ville").getValue().toString();

                                   holder.userName.setText(retrivePrenom);
                                   holder.userVille.setText(retriveville);
                               }

                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError databaseError) {

                           }
                       });

                   }

                   @NonNull
                   @Override
                   public freindViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout,viewGroup,false);
                       freindViewholder viewholder = new freindViewholder(view);
                       return viewholder;
                   }
               };

        friendsRecyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class freindViewholder extends RecyclerView.ViewHolder
    {

        TextView userName,userVille;
        CircleImageView profileImage;

        public freindViewholder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userVille = itemView.findViewById(R.id.user_profile_ville);
            profileImage = itemView.findViewById(R.id.users_profile_image);

        }
    }

}
