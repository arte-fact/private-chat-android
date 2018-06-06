package fr.artefact.private_chat;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextView;
    public UserViewHolder(TextView v) {
        super(v);
        mTextView = v;
    }
}