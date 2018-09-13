package com.example.arturschaefer.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.arturschaefer.bakingapp.R;
import com.example.arturschaefer.bakingapp.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{
    public static final String LOG_TAG = StepAdapter.class.getSimpleName();
    private ArrayList<Step> mStepArrayList;
    private StepListener mStepListener;
    private Context mContext;

    public interface StepListener{
        void onStepClick(Step step);
    }

    public StepAdapter(ArrayList<Step> mStepArrayList, StepListener mStepListener) {
        this.mStepArrayList = mStepArrayList;
        this.mStepListener = mStepListener;
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_steps, parent, false);
        return new StepAdapter.StepViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, int position) {

        Step itemStep = mStepArrayList.get(position);
        holder.shortTextView.setText(String.valueOf(position)
                + " - "
                + itemStep.getmShortDescription());
        String colorString = (position % 2 == 1) ? "#82b1ff" : "#e3f2fd";
        holder.itemView.setBackgroundColor(Color.parseColor(colorString));
    }

    @Override
    public int getItemCount() {
        return mStepArrayList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ll_step)
        LinearLayout stepLinearLayout;
        @BindView(R.id.tv_step_short_description)
        TextView shortTextView;

        public StepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            shortTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mStepListener.onStepClick(mStepArrayList.get(getAdapterPosition()));
            Log.i(LOG_TAG, "Click Listenter. Position Adapter: " + getAdapterPosition());
        }
    }
}
