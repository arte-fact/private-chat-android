package fr.artefact.private_chat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.Models.Conversation;
import fr.artefact.private_chat.R;

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ViewHolder>  {

    private List<Conversation> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout mTextView;

        private ViewHolder(LinearLayout v) {
            super(v);
            mTextView = v;
        }

        void setData(Conversation conversation) {
            TextView userName = mTextView.findViewById(R.id.user_name);
            userName.setText(conversation.getName());
        }
    }

    public ConversationAdapter(List<Conversation> conversations) {
        dataSet = conversations;
    }

    @Override
    @NonNull
    public ConversationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
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
        if (dataSet == null) {
            return 0;
        }
        return dataSet.size();
    }

    public void addItems(List<Conversation> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void addItem(Conversation conversation) {
        this.dataSet.add(conversation);
        notifyDataSetChanged();
    }
}