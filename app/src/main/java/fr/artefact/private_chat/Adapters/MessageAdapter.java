package fr.artefact.private_chat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.Models.Message;
import fr.artefact.private_chat.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>  {

    private java.util.List<Message> dataSet;
    public RecyclerView mRecyclerView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTextView;

        private ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

        void setData(Message message) {
            mTextView.setText(message.getText());
        }
    }

    public MessageAdapter(final List<Message> messages) {
        dataSet = messages;
        notifyDataSetChanged();
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    public void addItem(Message message) {
        this.dataSet.add(message);
        notifyDataSetChanged();
        this.mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
    }
}