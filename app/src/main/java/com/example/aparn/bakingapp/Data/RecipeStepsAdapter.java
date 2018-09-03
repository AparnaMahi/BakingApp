package com.example.aparn.bakingapp.Data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aparn.bakingapp.DetailActivity;
import com.example.aparn.bakingapp.Model.RecipeSteps;
import com.example.aparn.bakingapp.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsAdapterViewHolder> {

    private Context mContext;
    private String RecipeName;
    private ArrayList<RecipeSteps> RecipeStepList;

    public class RecipeStepsAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout RecipeStepLayout;
        public ImageView ThumbnailView;
        public TextView StepText;

        public RecipeStepsAdapterViewHolder(LinearLayout view) {
            super(view);
            RecipeStepLayout = view;
            ThumbnailView = new ImageView(mContext);
            StepText = new TextView(mContext);
            RecipeStepLayout.addView(ThumbnailView);
            RecipeStepLayout.addView(StepText);
        }
    }

    public RecipeStepsAdapter(Context context, String recipeName,ArrayList<RecipeSteps> recipeStepList) {
        mContext = context;
        RecipeName = recipeName;
        RecipeStepList = recipeStepList;
    }

    @NonNull
    @Override
    public RecipeStepsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout stepsLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_params.setMargins(20, 10, 20, 10);
        stepsLayout.setLayoutParams(layout_params);
        stepsLayout.setBackgroundResource(R.color.cardBGColor);
        RecipeStepsAdapterViewHolder res_recipeSteps = new RecipeStepsAdapterViewHolder(stepsLayout);
        return res_recipeSteps;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsAdapterViewHolder holder, final int position) {

        final RecipeSteps recipeStepAt = RecipeStepList.get(position);

        holder.RecipeStepLayout.setContentDescription(recipeStepAt.getShortDescription());

        LinearLayout.LayoutParams thumbnailParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        thumbnailParams.setMargins(20, 20, 20, 20);
        thumbnailParams.weight = 3f;
        thumbnailParams.gravity = Gravity.CENTER;
        holder.ThumbnailView.setLayoutParams(thumbnailParams);
        Picasso.get()
                .load(Uri.parse(recipeStepAt.getThumbnailUrl()))
                .placeholder(R.drawable.ic_cake_black_24dp)
                .error(R.drawable.ic_cake_black_24dp)
                .into(holder.ThumbnailView);

        LinearLayout.LayoutParams textParams =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.setMargins(20, 20, 20, 20);
        textParams.weight = 1f;
        textParams.gravity = Gravity.CENTER;
        holder.StepText.setLayoutParams(textParams);
        holder.StepText.setText(recipeStepAt.getShortDescription());
        holder.StepText.setTextAppearance(mContext, R.style.StepCardText);
        holder.StepText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        holder.RecipeStepLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity detailActivity = (DetailActivity) mContext;
                detailActivity.setCurrentPosition(position);
                detailActivity.openInstructionFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return RecipeStepList.size();
    }
}
