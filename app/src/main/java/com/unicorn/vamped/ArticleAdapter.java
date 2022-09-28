package com.unicorn.vamped;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.content.ContentValues.TAG;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private Context context;
    private List<Article> myArticleList;
    private ListItemClickListener mOnClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cardView;
        ImageView artImage;
        TextView artName;
        TextView artContent;
        final private ListItemClickListener mOnClickListener;

        ViewHolder(View itemView, ListItemClickListener onClickListener) {
            super(itemView);
            cardView = (CardView) itemView;
            artName = itemView.findViewById(R.id.artname);
            artContent = itemView.findViewById(R.id.arttext);
            artImage = itemView.findViewById(R.id.artimg);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

    public ArticleAdapter(List<Article> articleList, ListItemClickListener onClickListener) {
        myArticleList = articleList;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = myArticleList.get(position);
        holder.artName.setText(article.getName());
        holder.artContent.setText(article.getContent());
        PicassoClient.downloadImage(context, article.getImageUrl(), holder.artImage);
    }

    @Override
    public int getItemCount() {
        return myArticleList.size();
    }

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }
}