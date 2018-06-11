package fr.artefact.private_chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {

    public java.util.List<User> dataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        private ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }

        public void setData(User user) {
            Log.d("name", user.getName());
            mTextView.setText(user.getName());

        }
    }

    public UserAdapter(java.util.List<User> users) {
        dataset = users;
    }

    @Override
    @NonNull
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserAdapter.ViewHolder holder, int position) {

        User item = dataset.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}