package com.example.ultimatefive;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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


public class ChatFragment extends Fragment {

    private View chatView;
    private RecyclerView chatList;

    private DatabaseReference ChatRefernece,userRefernece;
    private FirebaseAuth auth;
    private String currentUserId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatView =  inflater.inflate(R.layout.fragment_chat, container, false);


        // base de donnes
        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();
        ChatRefernece= FirebaseDatabase.getInstance().getReference().child("contacts").child(currentUserId);
        userRefernece = FirebaseDatabase.getInstance().getReference().child("Users");


        chatList = chatView.findViewById(R.id.chat_list);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        return chatView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
               .setQuery(ChatRefernece,User.class)
               .build();
        FirebaseRecyclerAdapter<User, ChatViewholder> adapter =
                new FirebaseRecyclerAdapter<User, ChatViewholder>(options) {
                    @NonNull
                    @Override
                    public ChatViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout,viewGroup,false);
                        ChatViewholder viewholder = new ChatViewholder(view);
                        return viewholder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final ChatViewholder holder, int position, @NonNull User model)
                    {
                        final String usersId = getRef(position).getKey();
                        final String[] retriveUserImage = {"default"};

                        userRefernece.child(usersId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if(dataSnapshot.exists())
                                {
                                    if (dataSnapshot.hasChild("image"))
                                    {
                                        retriveUserImage[0] = dataSnapshot.child("image").getValue().toString();
                                        Picasso.get().load(retriveUserImage[0]).placeholder(R.drawable.profile_image).into(holder.profileImage);

                                    }

                                    final String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                                    final String retriveville= dataSnapshot.child("ville").getValue().toString();

                                    holder.userName.setText(retrivePrenom);
                                    holder.userVille.setText(retriveville);

                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v)
                                        {
                                            Intent chatIntent = new Intent(getContext(),ChatsActivity.class);
                                            chatIntent.putExtra("visit_user_id",usersId);
                                            chatIntent.putExtra("visit_user_name",retrivePrenom);
                                            chatIntent.putExtra("visit_image", retriveUserImage[0]);

                                            startActivity(chatIntent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError)
                            {

                            }
                        });
                    }
                };

       chatList.setAdapter(adapter);
        adapter.startListening();

    }


    public static class ChatViewholder extends RecyclerView.ViewHolder
    {

        TextView userName,userVille;
        CircleImageView profileImage;

        public ChatViewholder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userVille = itemView.findViewById(R.id.user_profile_ville);
            profileImage = itemView.findViewById(R.id.users_profile_image);

        }
    }
}
