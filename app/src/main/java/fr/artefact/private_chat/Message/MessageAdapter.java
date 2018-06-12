package fr.artefact.private_chat.Message;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>  {

    private java.util.List<Message> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

        public void setData(Message message) {
            mTextView.setText(message.getMessage());
        }
    }

    public MessageAdapter(List<Message> messages) {
        dataSet = messages;
    }

    @Override
    @NonNull
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new MessageAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Message item = dataSet.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}