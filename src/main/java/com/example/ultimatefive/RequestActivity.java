package com.example.ultimatefive;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RequestActivity extends AppCompatActivity {

    private RecyclerView RequestListRecyclerView;
    private DatabaseReference ChatRquestRefernece,userRefernece,ContactRefernece;
    private FirebaseAuth auth;
    private String currentUserId;
    private Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        // toolbar
        mToolbar = findViewById(R.id.Request_friends_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Mes Invitations");

        auth = FirebaseAuth.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

        ChatRquestRefernece = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        userRefernece = FirebaseDatabase.getInstance().getReference().child("Users");
        ContactRefernece = FirebaseDatabase.getInstance().getReference().child("contacts");




        RequestListRecyclerView = findViewById(R.id.chat_request_list);
        RequestListRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(ChatRquestRefernece.child(currentUserId),User.class)
                .build();

        FirebaseRecyclerAdapter<User,requestViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, requestViewHolder>(options) {
                    @NonNull
                    @Override
                    public requestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout,viewGroup,false);
                        requestViewHolder viewholder = new requestViewHolder(view);
                        return viewholder;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final requestViewHolder holder, int position, @NonNull final User model)
                    {
                        holder.itemView.findViewById(R.id.request_accepte_btn).setVisibility(View.VISIBLE);
                        holder.itemView.findViewById(R.id.request_cancel_btn).setVisibility(View.VISIBLE);

                        final String list_user_id = getRef(position).getKey();

                        DatabaseReference requestTyperefrence = getRef(position).child("request_type").getRef();

                        requestTyperefrence.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    String type = dataSnapshot.getValue().toString();

                                    if (type.equals("receved"))
                                    {
                                        userRefernece.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                            {
                                                if ( dataSnapshot.hasChild("image"))
                                                {
                                                    final String retriveUserImage= dataSnapshot.child("image").getValue().toString();
                                                    Picasso.get().load(retriveUserImage).placeholder(R.drawable.profile_image).into(holder.profileImage);
                                                }


                                                    final String retrivePrenom= dataSnapshot.child("prenom").getValue().toString();
                                                    final String retriveville= dataSnapshot.child("ville").getValue().toString();

                                                    holder.userName.setText(retrivePrenom);
                                                    holder.userVille.setText(retriveville);


                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v)
                                                    {
                                                        CharSequence options[] = new CharSequence[]
                                                                {
                                                                    "Accepte",
                                                                    "Cancel"
                                                                };
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(RequestActivity.this);
                                                        builder.setTitle(retrivePrenom + "Chat request");
                                                        builder.setItems(options, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
                                                                if (which==0)
                                                                {
                                                                    ContactRefernece.child(currentUserId).child(list_user_id).child("Contacts")
                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                        {
                                                                                if (task.isSuccessful())
                                                                                {
                                                                                    ContactRefernece.child(list_user_id).child(currentUserId).child("Contacts")
                                                                                            .setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task)
                                                                                        {
                                                                                            if (task.isSuccessful())
                                                                                            {
                                                                                                ChatRquestRefernece.child(currentUserId).child(list_user_id)
                                                                                                        .removeValue()
                                                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                            @Override
                                                                                                            public void onComplete(@NonNull Task<Void> task)
                                                                                                            {
                                                                                                                if (task.isSuccessful())
                                                                                                                {
                                                                                                                    ChatRquestRefernece.child(list_user_id).child(currentUserId)
                                                                                                                            .removeValue()
                                                                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                                                                {
                                                                                                                                    if (task.isSuccessful())
                                                                                                                                    {
                                                                                                                                        Toast.makeText(RequestActivity.this, "new Friend saved", Toast.LENGTH_SHORT).show();
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            });
                                                                                                                }
                                                                                                            }
                                                                                                        });
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                }
                                                                        }
                                                                    });
                                                                }

                                                                if (which==1)
                                                                {
                                                                    ChatRquestRefernece.child(currentUserId).child(list_user_id)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task)
                                                                                {
                                                                                    if (task.isSuccessful())
                                                                                    {
                                                                                        ChatRquestRefernece.child(list_user_id).child(currentUserId)
                                                                                                .removeValue()
                                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onComplete(@NonNull Task<Void> task)
                                                                                                    {
                                                                                                        if (task.isSuccessful())
                                                                                                        {
                                                                                                            Toast.makeText(RequestActivity.this, "Freind deleted", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                });
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });

                                                        builder.show();
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }


                                  



                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                };
        RequestListRecyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    public static class requestViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName,userVille;
        CircleImageView profileImage;
        Button Acceptebuttom,Cancelbuttom;

        public requestViewHolder(@NonNull View itemView)
        {
            super(itemView);
            userName = itemView.findViewById(R.id.user_profile_name);
            userVille = itemView.findViewById(R.id.user_profile_ville);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            Acceptebuttom = itemView.findViewById(R.id.request_accepte_btn);
           Cancelbuttom = itemView.findViewById(R.id.request_cancel_btn);



        }
    }
}
