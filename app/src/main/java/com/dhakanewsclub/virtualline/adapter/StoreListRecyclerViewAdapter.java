package com.dhakanewsclub.virtualline.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhakanewsclub.virtualline.R;
import com.dhakanewsclub.virtualline.place_line.PlaceLineActivity;
import com.dhakanewsclub.virtualline.add_place.SavePlaceInformationActivity;
import com.dhakanewsclub.virtualline.models.retrofit.PlaceInfo;
import com.dhakanewsclub.virtualline.my_place_list.PlaceListData;

import java.util.ArrayList;

public class StoreListRecyclerViewAdapter extends RecyclerView.Adapter<StoreListRecyclerViewAdapter.ViewHolder> {
    final String DIBAGING_TAG="DIBAGING_TAG";
    ArrayList<PlaceInfo> storeList;
    public StoreListRecyclerViewAdapter(ArrayList<PlaceInfo> storeList){
        this.storeList=storeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.store_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PlaceInfo placeInformation = storeList.get(position);
        holder.mStoreName.setText(placeInformation.getPlaceName());
        holder.mStoreType.setText(placeInformation.getPlaceType().getPlaceTypeName());
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+placeInformation.getPlaceName(),Toast.LENGTH_LONG).show();
                Intent line = new Intent(view.getContext(), PlaceLineActivity.class);
                PlaceListData.selectedPlace=placeInformation;
                Log.d(DIBAGING_TAG,"place info static :"+PlaceListData.selectedPlace.getPlaceLine().getLineStatus());
                line.putExtra("placeId",placeInformation.getPlaceId().toString());
                view.getContext().startActivity(line);

            }
        });

        holder.mEditStoreInfoImageView.setOnClickListener(view -> {
            Intent editInfo = new Intent(view.getContext(), SavePlaceInformationActivity.class);
            view.getContext().startActivity(editInfo);
        });

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mStoreName, mStoreType;
        public LinearLayout mLinearLayout;
        public ImageView mEditStoreInfoImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mStoreName =itemView.findViewById(R.id.store_name);
            mStoreType =itemView.findViewById(R.id.store_type);
            mLinearLayout =itemView.findViewById(R.id.linearLayout);
            mEditStoreInfoImageView=itemView.findViewById(R.id.store_info_edit_imageView);

        }
    }
}
