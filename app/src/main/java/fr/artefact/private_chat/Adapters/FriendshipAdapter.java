package fr.artefact.private_chat.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import fr.artefact.private_chat.Models.Friendship;
import fr.artefact.private_chat.R;

public class FriendshipAdapter extends RecyclerView.Adapter<FriendshipAdapter.ViewHolder>  {

    private List<Friendship> dataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout mLayout;

        private ViewHolder(LinearLayout v) {
            super(v);
            mLayout = v;
        }

        void setData(Friendship friendship) {
            TextView userName = mLayout.findViewById(R.id.friend_name);
            userName.setText(friendship.getName());
        }
    }

    public FriendshipAdapter(List<Friendship> friendship) {
        dataSet = friendship;
    }

    @Override
    @NonNull
    public FriendshipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend_item, parent, false);
        return new FriendshipAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendshipAdapter.ViewHolder holder, int position) {

        Friendship item = dataSet.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        }
        return dataSet.size();
    }

    public void addItems(List<Friendship> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public void addItem(Friendship friendship) {
        this.dataSet.add(friendship);
        notifyDataSetChanged();
    }
}