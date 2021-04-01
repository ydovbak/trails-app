package com.dovbak.trails;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dovbak.trails.models.TrailModel;

import java.util.List;

public class MyTrailRecyclerViewAdapter extends RecyclerView.Adapter<MyTrailRecyclerViewAdapter.ViewHolder> {

    private final List<TrailModel> mValues;
    private MyTrailAdapterClickListener clickListener;

    public MyTrailRecyclerViewAdapter(List<TrailModel> items, MyTrailAdapterClickListener clickListener) {
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trail_item, parent, false);
        return new ViewHolder(view, this.clickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TrailModel trail = mValues.get(position);
        holder.trail = mValues.get(position);
        holder.nameView.setText(mValues.get(position).getName());
        holder.descView.setText(mValues.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        public final TextView nameView;
        public final TextView descView;
        public TrailModel trail;

        public ViewHolder(View view, MyTrailAdapterClickListener clickListener) {
            super(view);
            this.view = view;
            nameView = (TextView) view.findViewById(R.id.item_number);
            descView = (TextView) view.findViewById(R.id.content);
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener != null) {
                        clickListener.onTrailClick(trail);
                    }
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + descView.getText() + "'";
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface MyTrailAdapterClickListener {
        void onTrailClick(TrailModel trail);
    }
}