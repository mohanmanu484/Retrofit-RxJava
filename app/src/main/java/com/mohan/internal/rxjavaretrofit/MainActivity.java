package com.mohan.internal.rxjavaretrofit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mohan.internal.rxjavaretrofit.pojo.Movie;
import com.mohan.internal.rxjavaretrofit.pojo.MoviesResponse;
import com.mohan.internal.rxjavaretrofit.retrofit.NetworkCall;
import com.mohan.internal.rxjavaretrofit.retrofit.RestClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    MovieAdapter movieAdapter;
    ListView movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = (ListView) findViewById(R.id.lvMovies);
        movieAdapter = new MovieAdapter(new ArrayList<Movie>(0));
        movieList.setAdapter(movieAdapter);
    }

    public void clearMovies(View view) {
        movieAdapter.swapMovies(new ArrayList<Movie>(0));
    }

    public void fetchMovies(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        Retrofit retrofit = RestClient.getClient();
        NetworkCall networkCall = retrofit.create(NetworkCall.class);
        Observable<MoviesResponse> observable = networkCall.getMovies(getString(R.string.your_key));
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MoviesResponse>() {
                    @Override
                    public void onCompleted() {
                        if (progressDialog != null) progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (progressDialog != null) progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MoviesResponse moviesResponse) {
                        List<Movie> movieList = moviesResponse.getResults();
                        movieAdapter.swapMovies(movieList);
                    }
                });

    }

    public class MovieAdapter extends BaseAdapter {

        List<Movie> movieArrayList;

        public MovieAdapter(List<Movie> movieArrayList) {
            this.movieArrayList = movieArrayList;
        }

        public void swapMovies(List<Movie> movieArrayList) {
            this.movieArrayList = movieArrayList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return movieArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return movieArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return movieArrayList.get(i).getId();
        }

        @Override
        public View getView(int i, View converterView, ViewGroup viewGroup) {
            View view = converterView;
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(android.R.layout.simple_list_item_1, viewGroup
                        , false);
            }
            TextView movieName = (TextView) view.findViewById(android.R.id.text1);
            movieName.setText(movieArrayList.get(i).getTitle());
            return view;
        }
    }
}
