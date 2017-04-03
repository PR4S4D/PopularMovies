package com.movies.slp.popularmovies.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.movies.slp.popularmovies.R;
import com.movies.slp.popularmovies.data.MovieReview;

import java.util.Collections;
import java.util.List;

public class ReviewAdaptor extends RecyclerView.Adapter<ReviewAdaptor.ViewHolder> {
    List<MovieReview> reviews = Collections.EMPTY_LIST;
    public ReviewAdaptor(List<MovieReview> reviews) {
        this.reviews = reviews;
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int reviews = R.layout.reviews;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(reviews, viewGroup, false);
        return new ReviewAdaptor.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        public ViewHolder(View itemView) {
            super(itemView);
            author = (TextView)itemView.findViewById(R.id.author);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
