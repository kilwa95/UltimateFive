package com.example.ultimatefive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private Toolbar toolbat;
    private ImageButton sendMessageButtom;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextemessage;

    private FirebaseAuth mAuth;
    private String currentgroupName,currentUserId,currentUserName,currentDate,currentTime;
    private DatabaseReference Userreference,Groupreference,groupMessageKeyrefernec;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentgroupName = getIntent().getExtras().get("groupName").toString();
        Toast.makeText(GroupChatActivity.this,currentgroupName,Toast.LENGTH_SHORT).show();


        // id de user et base de donnes
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        Userreference = FirebaseDatabase.getInstance().getReference().child("Users");
        Groupreference = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentgroupName);

        toolbat = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(toolbat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(currentgroupName);

        sendMessageButtom = findViewById(R.id.send_message_buttom);
        userMessageInput = findViewById(R.id.input_group_message);
        displayTextemessage = findViewById(R.id.group_chat_bar_text_display);
        mScrollView = findViewById(R.id.scroll_view);


        GetUserinfo();

        sendMessageButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageInfoInDataBase();
                userMessageInput.setText("");
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Groupreference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())
                {
                    affichermessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                if (dataSnapshot.exists())
                {
                    affichermessages(dataSnapshot);
                }
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



    private void GetUserinfo()
    {
        Userreference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    currentUserName = dataSnapshot.child("prenom").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    private void saveMessageInfoInDataBase()
    {
        String message = userMessageInput.getText().toString();
        String messageKey = Groupreference.push().getKey();

        if (TextUtils.isEmpty(message))
        {
            Toast.makeText(GroupChatActivity.this,"please write message first...",Toast.LENGTH_SHORT).show();

        }
        else
        {
            Calendar calendarDate =  Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM,dd,yyyy");
            currentDate = currentDateFormat.format(calendarDate.getTime());


            Calendar calendarTime =  Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calendarTime.getTime());


            HashMap<String,Object> groupMessageKey = new HashMap<>();
            Groupreference.updateChildren(groupMessageKey);
            groupMessageKeyrefernec = Groupreference.child(messageKey);

            HashMap<String,Object> messageinfomap = new HashMap<>();
            messageinfomap.put("prenom",currentUserName);
            messageinfomap.put("message",message);
            messageinfomap.put("date",currentDate);
            messageinfomap.put("time",currentTime);

            groupMessageKeyrefernec.updateChildren(messageinfomap);






        }
    }

    private void affichermessages(DataSnapshot dataSnapshot)
    {
        Iterator iterator = dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatData = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatPrenom = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();

            displayTextemessage.append(chatPrenom + ":\n" + chatMessage + ":\n" + chatTime +"        "+ chatData + ":\n\n\n");
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }
    }
}
