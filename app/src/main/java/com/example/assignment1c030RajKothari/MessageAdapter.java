package com.example.assignment1c030RajKothari;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;

    public MessageAdapter() {
        this.messageList = new ArrayList<>();
    }

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    private static final int TYPE_USER = 0;
    private static final int TYPE_BOT = 1;

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isUser() ? TYPE_USER : TYPE_BOT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_USER) {
            View view = inflater.inflate(R.layout.item_message_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).msgText.setText(message.getText());
            ((UserViewHolder) holder).userIcon.setImageResource(R.drawable.user); // user.jpg in drawable
        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).msgText.setText(message.getText());
            ((BotViewHolder) holder).botIcon.setImageResource(R.drawable.bot); // bot.jpg in drawable
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public void clearMessages() {
        messageList.clear();
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView msgText;
        ImageView userIcon;

        UserViewHolder(View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText);
            userIcon = itemView.findViewById(R.id.user_icon);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView msgText;
        ImageView botIcon;

        BotViewHolder(View itemView) {
            super(itemView);
            msgText = itemView.findViewById(R.id.msgText);
            botIcon = itemView.findViewById(R.id.bot_icon);
        }
    }
}
