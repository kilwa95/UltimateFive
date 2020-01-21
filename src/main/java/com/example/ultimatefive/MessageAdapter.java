package com.example.ultimatefive;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * created by Abdulhalim Khaled on 2020-01-20.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
{
    private List<Message> userMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference usersref;

    public MessageAdapter(List<Message> userMessageList)
    {
        this.userMessageList = userMessageList;
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView sendermessagetext, recerverMessageText;
        public CircleImageView ImageProfileUser;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            sendermessagetext = itemView.findViewById(R.id.sender_message_text);
            recerverMessageText = itemView.findViewById(R.id.recever_message_text);
            ImageProfileUser = itemView.findViewById(R.id.message_profile_image);

        }

    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message_layout,null,true);
        mAuth = FirebaseAuth.getInstance();
        return new MessageViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position)
    {
            String MessageSenderId = mAuth.getCurrentUser().getUid();
            Message message = userMessageList.get(position);
            String fromUserId = message.getFrom();
            String fromMessageType = message.getType();

            usersref = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
            usersref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.hasChild("image"))
                    {
                        String recerImageProfile = dataSnapshot.child("image").getValue().toString();

                        Picasso.get().load(recerImageProfile).placeholder(R.drawable.profile_image).into(holder.ImageProfileUser);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            if (fromMessageType.equals("text"))
            {
                holder.recerverMessageText.setVisibility(View.INVISIBLE);
                holder.ImageProfileUser.setVisibility(View.INVISIBLE);
                holder.sendermessagetext.setVisibility(View.INVISIBLE);

            }

            if (fromUserId.equals(MessageSenderId))

            {
                holder.sendermessagetext.setVisibility(View.VISIBLE);
                holder.sendermessagetext.setBackgroundResource(R.drawable.sender_message_layout);
                holder.sendermessagetext.setTextColor(Color.BLACK);
                holder.sendermessagetext.setText(message.getMessage());
            }

            else
            {
                holder.ImageProfileUser.setVisibility(View.VISIBLE);
                holder.recerverMessageText.setVisibility(View.VISIBLE);

                holder.recerverMessageText.setBackgroundResource(R.drawable.recever_message_layout);
                holder.recerverMessageText.setTextColor(Color.BLACK);
                holder.recerverMessageText.setText(message.getMessage());

            }
    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }
}







