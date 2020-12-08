package com.dhakanewsclub.virtualline.adapter;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceType;

import java.util.ArrayList;
import java.util.List;

public class PlaceTypeOptionRecyclerViewAdapter extends RecyclerView.Adapter<PlaceTypeOptionRecyclerViewAdapter.ViewHolder> {

    List<PlaceType> mPlaceTypes;
    public static OnItemClickListener sOnItemClickListener;
    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public PlaceTypeOptionRecyclerViewAdapter(List<PlaceType> placeTypes, OnItemClickListener onItemClickListener) {
        if(placeTypes==null){
            this.mPlaceTypes=new ArrayList<PlaceType>();
        }
        else {
            this.mPlaceTypes = placeTypes;
        }
        this.sOnItemClickListener=onItemClickListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.place_type_recycler_view_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PlaceType placeType=mPlaceTypes.get(position);
        holder.mTextViewPlaceTypeName.setText(placeType.getPlaceTypeName());
        if(placeType.getPlaceTypeName().equals("Pharmacy")){
            holder.mImageViewIcon.setImageResource(R.drawable.place_type_option_pharmacy);
        }
        else if(placeType.getPlaceTypeName().equals("Super Shop")) {
            holder.mImageViewIcon.setImageResource(R.drawable.place_type_option_grocery_store);
        }

    }

    @Override
    public int getItemCount() {
        return mPlaceTypes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageViewIcon;
        public TextView mTextViewPlaceTypeName;
        public LinearLayout mLinearLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageViewIcon=itemView.findViewById(R.id.place_icon_image_view);
            mTextViewPlaceTypeName=itemView.findViewById(R.id.place_type_name_text_view);
            mLinearLayout=itemView.findViewById(R.id.place_type_option_recycler_view_item);
            mLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position= PlaceTypeOptionRecyclerViewAdapter.ViewHolder.super.getAdapterPosition();
                    sOnItemClickListener.onItemClick(view,position);
                }
            });
        }

    }

}
