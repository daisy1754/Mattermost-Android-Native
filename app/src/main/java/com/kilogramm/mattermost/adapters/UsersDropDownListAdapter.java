package com.kilogramm.mattermost.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.kilogramm.mattermost.MattermostPreference;
import com.kilogramm.mattermost.R;
import com.kilogramm.mattermost.databinding.ItemUserDropDownListBinding;
import com.kilogramm.mattermost.model.entity.user.User;
import com.squareup.picasso.Picasso;

import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by ngers on 16.09.16.
 */

public class UsersDropDownListAdapter extends RealmRecyclerViewAdapter<User, UsersDropDownListAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    public UsersDropDownListAdapter(@NonNull Context context,
                                    OnItemClickListener onItemClickListener) {
        super(context,null,true);
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemUserDropDownListBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_user_drop_down_list,
                parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = getData().get(position);
        if (user.getId() != null) {
            holder.binding.avatar.setTag(user);
            holder.binding.userNikname.setText("@" + user.getUsername());
            holder.binding.userName.setText(String.format(" %s %s",
                    user.getFirstName() != null ? user.getFirstName() : "",
                    user.getLastName() != null ? user.getLastName() : ""));

            Picasso.with(holder.binding.getRoot().getContext())
                    .load(getImageUrl(user))
                    .resize(60, 60)
                    .error(holder.binding.getRoot().getContext()
                            .getResources()
                            .getDrawable(R.drawable.ic_person_grey_24dp))
                    .placeholder(holder.binding.getRoot().getContext()
                            .getResources()
                            .getDrawable(R.drawable.ic_person_grey_24dp))
                    .into(holder.binding.avatar);

            holder.binding.getRoot().setOnClickListener(view ->
                    onItemClickListener.onItemClick(user.getUsername()));
        }
    }

    public String getImageUrl(User user) {
        return "https://"
                + MattermostPreference.getInstance().getBaseUrl()
                + "/api/v3/users/"
                + user.getId()
                + "/image";
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        private ItemUserDropDownListBinding binding;

        public ViewHolder(ItemUserDropDownListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String name);
    }

}