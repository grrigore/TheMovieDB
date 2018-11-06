package com.grrigore.themoviedb.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.grrigore.themoviedb.R;
import com.grrigore.themoviedb.data.CastRoom;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<CastRoom> castList;

    public CastAdapter(List<CastRoom> castList) {
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastAdapter.CastViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cast, viewGroup, false);
        return new CastAdapter.CastViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.CastViewHolder castViewHolder, int i) {
        CastRoom cast = castList.get(i);
        if (cast.getProfile() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(cast.getProfile(), 0, cast.getProfile().length);
            castViewHolder.castImageView.setImageBitmap(bmp);
        } else {
            castViewHolder.castImageView.setImageResource(R.drawable.ic_broken_image_black_50dp);
        }
    }

    @Override
    public int getItemCount() {
        if (castList == null) {
            return 0;
        } else {
            return castList.size();
        }
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        private ImageView castImageView;


        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            castImageView = itemView.findViewById(R.id.imageview_moviedetail_cast);
        }
    }
}