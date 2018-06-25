package fr.artefact.private_chat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.Models.User;
import fr.artefact.private_chat.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>  {

    private java.util.List<User> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout mTextView;

        private ViewHolder(LinearLayout v) {
            super(v);
            mTextView = v;
        }

        void setData(User user) {
            TextView userName = mTextView.findViewById(R.id.user_name);
            userName.setText(user.getName());
        }
    }

    public UserAdapter(List<User> users) {
        dataSet = users;
    }

    @Override
    @NonNull
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_item, parent, false);
        return new UserAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {

        User item = dataSet.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    public void addUsers(List<User> users) {
        dataSet = users;
    }

}