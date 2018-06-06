package fr.artefact.private_chat;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;

class UserAdapter extends RecyclerView.Adapter<UserViewHolder>  {
    public java.util.List<User> dataset;

    // Contructeur
    public UserAdapter(java.util.List<User> users) {
        dataset = users;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        android.content.Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        TextView view = inflater.inflate(R.layout.fragment_user_item, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTexView(dataset.get(position));

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataset.size();
    }
}