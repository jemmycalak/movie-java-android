package com.calak.jemmy.movie.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.calak.jemmy.movie.R;
import com.calak.jemmy.movie.network.ConnectionStatus;
import com.calak.jemmy.movie.network.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.calak.jemmy.movie.model.AppVar;
import com.calak.jemmy.movie.model.BaseActivity;

import com.calak.jemmy.movie.adapter.ListAdapter;
import com.calak.jemmy.movie.adapter.viewHolder.MovieViewHolder;
import com.calak.jemmy.movie.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import static com.calak.jemmy.movie.network.ConnectionStatus.IS_NETWORK_AVAILABLE;

public class MovieActivity extends BaseActivity {
    private AlertDialogManager alert = new AlertDialogManager();
    private static final String TAG = "MovieActivity";
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private ArrayList<Movies> list = new ArrayList<>();
    private FrameLayout frameLayout;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initRecycler();
        initAdapterMovies();
//        getDB().clearMovie();
        showDialog("Loading...");
        getDataMovie();
        checkConnection();
    }

    private void checkConnection() {
        IntentFilter intentFilter = new IntentFilter(ConnectionStatus.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //IS_NETWORK_AVAILABLE must be import from ConnectionStatus.class
                boolean isNetworkConnected = intent.getBooleanExtra(IS_NETWORK_AVAILABLE, false);
                Log.d("statusConnection", String.valueOf(isNetworkConnected));
                if(isNetworkConnected==true){
                    getDataMovie(AppVar.URL_MOVIE_550);
                    hideSnackbar();
                    Snackbar.make(frameLayout, "You are online now.", Snackbar.LENGTH_SHORT).show();
                }else{
                    listAdapter.swapData(getDB().getAllListMovies());
                    snowSnackBar();
                    hideDialog();
                }
            }
        }, intentFilter);
    }

    private void initToolbar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Movies</font>"));
    }

    private void getDataMovie() {
        if (isInternetConnectionAvailable()) {
            getDataMovie(AppVar.URL_MOVIE_550);
            hideSnackbar();
        } else {
            listAdapter.swapData(getDB().getAllListMovies());
            snowSnackBar();
            hideDialog();
        }
    }

    private void snowSnackBar() {
        snackbar = Snackbar.make(frameLayout, "You are in offline mode.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(android.R.color.holo_red_dark));
        snackbar.show();
    }

    public void hideSnackbar(){
        if(!(snackbar==null)){
            snackbar.dismiss();
        }
    }

    private void initRecycler() {
        frameLayout = (FrameLayout)findViewById(R.id.frame);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMovie);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 1));
    }
    private void initAdapterMovies() {
        listAdapter = new ListAdapter<Movies
                , MovieViewHolder>
                (R.layout.item_movie
                        , MovieViewHolder.class
                        , Movies.class
                        , list) {
            @Override
            protected void bindView(MovieViewHolder holder, final Movies model, int position) {
                Picasso.with(getApplicationContext())
                        .load(AppVar.BASE_IMAGE+model.getPoster_path())
                        .into(holder.gambar_movie);
                Log.d("Reading: ", "Reading all movies..");
                List<Movies> movies = getDB().getAllMovies();
                for (Movies mv : movies) {
                    String log = "Id: " + mv.getId() + " ,Title: " + mv.getTitle() +  " ,Image: " + mv.getPoster_path() + " ,favorite: " + mv.getFavorite();
                    Log.d("movie : ", log);
                }
                holder.getItem().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(MovieActivity.this,DetailMovieActivity.class);
                        in.putExtra("movie",model);
                        startActivity(in);
                    }
                });
            }
        };
        recyclerView.setAdapter(listAdapter);
    }
    public void getDataMovie(String url) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Movies</font>"));
        StringRequest request = new StringRequest(Request.Method.GET
                , url
                , new Response.Listener<String>() { //response api
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                try {
                    JSONObject parent = new JSONObject(response);
                    JSONArray results = parent.getJSONArray("results");
                    list = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject sourceParam = results.getJSONObject(i);
                        Movies datajson = new Movies();
                        datajson.setId(sourceParam.getInt("id"));
                        datajson.setOriginal_language(sourceParam.getString("original_language"));
                        datajson.setTitle(sourceParam.getString("title"));
                        datajson.setOverview(sourceParam.getString("overview"));
                        datajson.setPopularity(sourceParam.getDouble("popularity"));
                        datajson.setPoster_path(sourceParam.getString("poster_path"));
                        datajson.setRelease_date(sourceParam.getString("release_date"));
                        datajson.setVote_average(sourceParam.getDouble("vote_average"));
                        datajson.setVote_count(sourceParam.getInt("vote_count"));
                        if (getDB().getCountMovies() != results.length()) {
                            getDB().addMovie(datajson);
                        }
                        list.add(datajson);
                    }

                    listAdapter.swapData(getDB().getAllListMovies());
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() { // error response
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                hideDialog();

            }
        });
//        AppMovie.getInstance().addToRequestQueue(request, TAG);
        VolleySingleton.getmInstance(this).addToRequestque(request);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getDataMovie();
        Log.d( TAG,"Clear data sqlite ,dan insert data dari api");
    } @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        exit();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.exit) {
            exit();
        }
        return super.onOptionsItemSelected(item);
    }

    private void exit() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Exit Movie App?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        alert.showAlertDialog(MovieActivity.this, "Closing program....", "Please Wait...", false);
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
