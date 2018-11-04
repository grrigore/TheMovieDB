package com.grrigore.themoviedb.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grrigore.themoviedb.R;
import com.grrigore.themoviedb.data.CrewRoom;

import java.util.List;

public class CrewAdapter extends RecyclerView.Adapter<CrewAdapter.CrewViewHolder> {

    private List<CrewRoom> crewList;

    public CrewAdapter(List<CrewRoom> crewList) {
        this.crewList = crewList;
    }

    @NonNull
    @Override
    public CrewAdapter.CrewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_crew, viewGroup, false);
        return new CrewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewAdapter.CrewViewHolder crewViewHolder, int i) {
        CrewRoom crew = crewList.get(i);
        crewViewHolder.crewNameTextView.setText(crew.getName());
        crewViewHolder.crewTextView.setText(crew.getDepartment());
    }

    @Override
    public int getItemCount() {
        if (crewList == null) {
            return 0;
        } else {
            return crewList.size();
        }
    }

    public class CrewViewHolder extends RecyclerView.ViewHolder {
        private TextView crewNameTextView;
        private TextView crewTextView;


        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            crewNameTextView = itemView.findViewById(R.id.textview_moviedetail_crewname);
            crewTextView = itemView.findViewById(R.id.textview_moviedetail_crewdepartment);
        }
    }

    public void setCrewList(List<CrewRoom> crewList) {
        this.crewList = crewList;
    }
}
