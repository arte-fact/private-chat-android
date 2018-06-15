package fr.artefact.private_chat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.R;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>  {

    private List<Conversation> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

        public void setData(Conversation conversation) {
            mTextView.setText(conversation.getName());
        }
    }

    public ConversationAdapter(List<Conversation> conversations) {
        dataSet = conversations;
    }

    @Override
    @NonNull
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new ConversationAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationAdapter.ViewHolder holder, int position) {

        Conversation item = dataSet.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void addItems(List<Conversation> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void addItem(Conversation message) {
        this.dataSet.add(message);
        notifyDataSetChanged();
    }
}