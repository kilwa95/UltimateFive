package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilejoueurActivity extends AppCompatActivity {

    private String getuserId,currentState,currentUserId;
    private CircleImageView userProfileImage;
    private TextView userprofileName,userProdileVille;
    private Button sendMessageRequestbuttom,declineMessageRequest;

    private DatabaseReference Userreference,ChatRequestRefernece,ContactrequestRefrence;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilejoueur);

        Userreference = FirebaseDatabase.getInstance().getReference().child("Users");
        ChatRequestRefernece = FirebaseDatabase.getInstance().getReference().child("Chat Request");
        ContactrequestRefrence = FirebaseDatabase.getInstance().getReference().child("contacts");

        auth = FirebaseAuth.getInstance();

        getuserId = getIntent().getExtras().get("userId").toString();
        currentUserId = auth.getCurrentUser().getUid();

        userProfileImage = findViewById(R.id.visit_profile_image);
        userprofileName = findViewById(R.id.visit_user_name);
        userProdileVille= findViewById(R.id.visit_user_Ville);
        sendMessageRequestbuttom = findViewById(R.id.send_message_request_buttom);
        declineMessageRequest = findViewById(R.id.decline_message_request_buttom);


        currentState = "new";

        getInformationUser();
    }


    private void getInformationUser()
    {
        Userreference.child(getuserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists()&& dataSnapshot.hasChild("nom")&& dataSnapshot.hasChild("image"))
                {
                    String UserName = dataSnapshot.child("nom").getValue().toString();
                    String Userville= dataSnapshot.child("ville").getValue().toString();
                    String Userimage= dataSnapshot.child("image").getValue().toString();


                    userprofileName.setText(UserName);
                    userProdileVille.setText(Userville);
                    Picasso.get().load(Userimage).placeholder(R.drawable.profile_image).into(userProfileImage);

                    ManageChatRequest();




                }

                else
                {
                    String UserName = dataSnapshot.child("nom").getValue().toString();
                    String Userville= dataSnapshot.child("ville").getValue().toString();

                    userprofileName.setText(UserName);
                    userProdileVille.setText(Userville);

                    ManageChatRequest();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequest()
    {

        ChatRequestRefernece.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.hasChild(getuserId))
                {
                    String request_type = dataSnapshot.child(getuserId).child("request_type").getValue().toString();

                    if (request_type.equals("sent"))
                    {
                        currentState = "request_sent";
                        sendMessageRequestbuttom.setText("Cancel Chat Request");
                    }

                    else if (request_type.equals("receved"))
                    {
                        currentState = "request_receved";
                        sendMessageRequestbuttom.setText("Accepte Chat Request");
                        declineMessageRequest.setVisibility(View.VISIBLE);

                        declineMessageRequest.setEnabled(true);
                        declineMessageRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                canalChatrequest();
                            }
                        });
                    }

                }


                else
                {
                    ContactrequestRefrence.child(currentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.hasChild(getuserId))
                            {
                                currentState = "freinds";
                                sendMessageRequestbuttom.setText("Remouve this contact");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        if (!currentUserId.equals(getuserId))
        {
            sendMessageRequestbuttom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendMessageRequestbuttom.setEnabled(false);

                    if (currentState.equals("new"))
                    {
                        sendChatRequest();
                    }

                    if (currentState.equals("request_sent"))
                    {
                        canalChatrequest();
                    }

                    if (currentState.equals("request_receved"))
                    {
                        AccepteChatRequest();
                    }

                    if (currentState.equals("freinds"))
                    {
                        remouveContact();
                    }
                }
            });
        }

        else
        {
            sendMessageRequestbuttom.setVisibility(View.INVISIBLE);
        }
    }

    private void remouveContact()
    {
        ContactrequestRefrence.child(currentUserId).child(getuserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ContactrequestRefrence.child(getuserId).child(currentUserId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                sendMessageRequestbuttom.setEnabled(true);
                                                currentState = "new";
                                                sendMessageRequestbuttom.setText("send message");

                                                declineMessageRequest.setVisibility(View.INVISIBLE);
                                                declineMessageRequest.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void AccepteChatRequest()
    {
        ContactrequestRefrence.child(currentUserId).child(getuserId)
                .child("Contacts").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    ContactrequestRefrence.child(getuserId).child(currentUserId)
                            .child("Contacts").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                ChatRequestRefernece.child(currentUserId).child(getuserId).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    ChatRequestRefernece.child(getuserId).child(currentUserId).removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task)
                                                                {
                                                                    sendMessageRequestbuttom.setEnabled(true);
                                                                    currentState = "freinds";
                                                                    sendMessageRequestbuttom.setText("Remouve this contact");


                                                                    declineMessageRequest.setVisibility(View.INVISIBLE);
                                                                    sendMessageRequestbuttom.setEnabled(false);

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

    private void canalChatrequest()
    {
        ChatRequestRefernece.child(currentUserId).child(getuserId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            ChatRequestRefernece.child(getuserId).child(currentUserId).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                sendMessageRequestbuttom.setEnabled(true);
                                                currentState = "new";
                                                sendMessageRequestbuttom.setText("send message");

                                                declineMessageRequest.setVisibility(View.INVISIBLE);
                                                declineMessageRequest.setEnabled(false);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void sendChatRequest()
    {
        ChatRequestRefernece.child(currentUserId).child(getuserId).child("request_type")
                .setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                ChatRequestRefernece.child(getuserId).child(currentUserId).child("request_type")
                        .setValue("receved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            sendMessageRequestbuttom.setEnabled(true);
                            currentState = "request_sent";
                            sendMessageRequestbuttom.setText("Cancel Chat Request");
                        }
                    }
                });
            }
        });
    }
}
