package com.calak.jemmy.movie.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.calak.jemmy.movie.R;
import com.calak.jemmy.movie.model.AppVar;
import com.calak.jemmy.movie.model.BaseActivity;
import com.calak.jemmy.movie.model.Movies;
import com.calak.jemmy.movie.network.ConnectionStatus;
import com.squareup.picasso.Picasso;

import static com.calak.jemmy.movie.network.ConnectionStatus.IS_NETWORK_AVAILABLE;

public class DetailMovieActivity extends BaseActivity {

    AlertDialogManager alert = new AlertDialogManager();
    private static final String TAG = "DetailMovieActivity";
    private Movies movies;
    public TextView id;
    private TextView title;
    private TextView overview;
    private TextView vote_average;
    private TextView release_date;
    private TextView popularity;
    private TextView vote_count;
    private ImageView gambar_movies;
    private TextView name;
    private ImageView mark_as_favourite;
    private ImageView delete;
    private String JSON_STRING;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#333333")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Detail Movie</font>"));
        movies = (Movies) getIntent().getSerializableExtra("movie");
        popularity = (TextView) findViewById(R.id.popularity);
        id = (TextView) findViewById(R.id.id);
        title = (TextView) findViewById(R.id.title);
        vote_count = (TextView) findViewById(R.id.vote_count);
        release_date = (TextView) findViewById(R.id.release_date);
        vote_average = (TextView) findViewById(R.id.vote_average);
        overview = (TextView) findViewById(R.id.overview);
    /*    title.setText(movies.getTitle());*/
        String[] dateParts = movies.getRelease_date().split("-");
        String day = dateParts[0];
        release_date.setText(day);
        int param_id = (int) movies.getId();
        String idnya = String.valueOf(param_id);
        id.setText(idnya);
        title.setText(movies.getTitle());
       /* release_date.setText(movies.getRelease_date());
       */
        overview.setText(movies.getOverview());
        int param_count = (int) movies.getVote_count();
        String convertcount = String.valueOf(param_count);
        vote_count.setText(convertcount);
        double param_popularity = (double) movies.getPopularity();
        String convertpopularity = String.valueOf(param_popularity);
        popularity.setText(convertpopularity);
        float param = (float) movies.getVote_average();
        String convertparam = String.valueOf(param);
        vote_average.setText(convertparam);
        gambar_movies = (ImageView) findViewById(R.id.gambar_movie);
        mark_as_favourite = (ImageView) findViewById(R.id.mark_as_favourite);
        mark_as_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movies.setFavorite("true");
                getDB().updateMovie(movies);
                Toast.makeText(DetailMovieActivity.this, "Done ," + movies.getTitle().toString() + " now your favorite.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        delete = (ImageView) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailMovieActivity.this);
                alertDialogBuilder.setMessage("Are You Sure To Delete This Record?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                getDB().deleteMovie(movies);
                                Toast.makeText(DetailMovieActivity.this, "Done ," + movies.getTitle().toString() + " Movie Deleted", Toast.LENGTH_SHORT).show();
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
        });
        Picasso.with(this).load(AppVar.BASE_IMAGE + movies.getPoster_path()).into(gambar_movies);

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
                    Snackbar.make(findViewById(R.id.detailMovie), "You are online now.", Snackbar.LENGTH_LONG).show();
                }else{
                    Snackbar.make(findViewById(R.id.detailMovie), "You are in offline mode.", Snackbar.LENGTH_LONG).show();
                }
            }
        }, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_right, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exit) {
            exit();
        }else if(id==android.R.id.home){
            finish();
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
                        alert.showAlertDialog(DetailMovieActivity.this, "Closing program....", "Please Wait...", false);
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
