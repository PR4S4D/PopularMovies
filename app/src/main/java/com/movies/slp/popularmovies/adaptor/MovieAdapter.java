package com.movies.slp.popularmovies.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movies.slp.popularmovies.utilities.Movie;
import com.movies.slp.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList = Collections.emptyList();
    final private ListItemClickListener onClickListener;

    public MovieAdapter(List<Movie> movieList, ListItemClickListener onClickListener) {
        this.movieList = movieList;
        this.onClickListener = onClickListener;
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int movieGrid = R.layout.movie_grid;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(movieGrid, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        String posterUrl = movieList.get(position).getPosterPath();
        Picasso.with(holder.moviePoster.getContext()).load(posterUrl).placeholder(R.drawable.loading).error(R.drawable.loading).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        if(null == movieList)
            return 0;
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView moviePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.poster);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}
