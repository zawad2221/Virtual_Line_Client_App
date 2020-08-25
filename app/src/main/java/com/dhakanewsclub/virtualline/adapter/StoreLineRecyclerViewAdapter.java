package com.dhakanewsclub.virtualline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.models.retrofit.CheckedUser;

import java.util.ArrayList;

public class StoreLineRecyclerViewAdapter extends RecyclerView.Adapter<StoreLineRecyclerViewAdapter.ViewHolder> {
    ArrayList<CheckedUser> checkedUsers;
    private static OnItemClickListener onItemClickListener;
    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public StoreLineRecyclerViewAdapter(ArrayList<CheckedUser> checkedUsers,OnItemClickListener onItemClickListener) {
        this.checkedUsers = checkedUsers;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.store_line_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CheckedUser user= checkedUsers.get(position);
        holder.mUserNameTextView.setText(user.getUser().getName());
        //on click from adapter
//        holder.mRemoveUserImageView.setOnClickListener(view-> {
//            Toast.makeText(view.getContext(), checkedUsers.get(position).getUser().getName()+" is removed",Toast.LENGTH_LONG).show();
//
//
//        });


    }



    @Override
    public int getItemCount() {
        return checkedUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mUserNameTextView;
        public ImageView mRemoveUserImageView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mUserNameTextView=itemView.findViewById(R.id.name_textView);
            mRemoveUserImageView=(ImageView) itemView.findViewById(R.id.removeUserButton);
            mRemoveUserImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=ViewHolder.super.getAdapterPosition();
                    onItemClickListener.onItemClick(view,position);
                }
            });
        }
    }
}
