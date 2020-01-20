package com.example.ultimatefive;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsActivity extends AppCompatActivity {

    private String messageReceveId, messageReceveName, messageReceveImage,messageSenderId;
    private TextView userName, userLastseen;
    private CircleImageView userImage;

    private Toolbar chatTollbar;

    private ImageButton SendMessageButtom;
    private EditText MessageInputTexte;

    private FirebaseAuth mAuth;
    private DatabaseReference Rootref;

    private final List<Message> messageslist = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private RecyclerView messageRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);


        mAuth = FirebaseAuth.getInstance();
        messageSenderId = mAuth.getCurrentUser().getUid();
       Rootref = FirebaseDatabase.getInstance().getReference();

        messageReceveId = getIntent().getExtras().get("visit_user_id").toString();
        messageReceveName = getIntent().getExtras().get("visit_user_name").toString();
        messageReceveImage = getIntent().getExtras().get("visit_image").toString();


        // toolbar with image profile et userName

        chatTollbar = findViewById(R.id.chats_friends_toolbar);
        setSupportActionBar(chatTollbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionbarView = layoutInflater.inflate(R.layout.custom_chat_bar, null);
        actionBar.setCustomView(actionbarView);

        userImage = findViewById(R.id.custom_profile_image);
        userName = findViewById(R.id.custom_profile_Name);
        userLastseen = findViewById(R.id.custom_profile_last_seen);

        SendMessageButtom = findViewById(R.id.send_message_to_joeur);
        MessageInputTexte = findViewById(R.id.input_message);


        messageAdapter = new MessageAdapter(messageslist);
        messageRecyclerView = findViewById(R.id.message_joeurs);
        linearLayoutManager = new LinearLayoutManager(this);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        messageRecyclerView.setAdapter(messageAdapter);



        userName.setText(messageReceveName);
        Picasso.get().load(messageReceveImage).placeholder(R.drawable.profile_image).into(userImage);


        SendMessageButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Rootref.child("Messages").child(messageSenderId).child(messageReceveId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Message message = dataSnapshot.getValue(Message.class);
                messageslist.add(message);
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage() {
        String messagetext = MessageInputTexte.getText().toString();

        if (TextUtils.isEmpty(messagetext))
        {
            Toast.makeText(ChatsActivity.this, "please write message first...", Toast.LENGTH_SHORT).show();

        }

        else
        {
            String messageSenderRef = "Messages/" + messageSenderId + "/" + messageReceveId;
            String messageRecevedRef = "Messages/" + messageReceveId + "/" + messageSenderId;

            DatabaseReference userMessageKey = Rootref.child("Messages").child(messageSenderId).child(messageReceveId).push();
            String messagePuchId = userMessageKey.getKey();

            Map messgetextBody = new HashMap();
            messgetextBody.put("message",messagetext);
            messgetextBody.put("type","text");
            messgetextBody.put("from",messageSenderId);


            Map messgetextBodydet = new HashMap();
            messgetextBodydet.put(messageSenderRef + "/" + messagePuchId,messgetextBody);
            messgetextBodydet.put(messageRecevedRef + "/" + messagePuchId,messgetextBody);

            Rootref.updateChildren(messgetextBodydet).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if (task.isSuccessful())
                    {

                    }
                }
            });


        }
    }

}
