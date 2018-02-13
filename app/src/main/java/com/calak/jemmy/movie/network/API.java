package com.calak.jemmy.movie.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.calak.jemmy.movie.activity.MovieActivity;
import com.calak.jemmy.movie.model.AppVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by JEMMY CALAK on 1/20/2018.
 */

public class API {
    AppVar appVar = new AppVar();
    ProgressDialog progressDialog;

    public void startLoading(Context context, String pesan){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Processing");
            progressDialog.setMessage(pesan);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
    public void stopLoading(Context context){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


//    public void getDataMovie(final Activity activity) {
//        startLoading(activity, "Please wait..");
//        final ArrayList<movie> dataMovie = new ArrayList<>();
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, appVar.BASE_URL + "popular?api_key=" + appVar.api_key, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("Response", String.valueOf(response));
//                try {
//                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                    JSONArray jsonArray = jsonObject.getJSONArray("results");
//                    for(int i=0; i<jsonArray.length(); i++){
//                        JSONObject json = jsonArray.getJSONObject(i);
//                        JSONArray jsonArray1 = json.getJSONArray("genre_ids");
//                        ArrayList<Integer> genreId = new ArrayList<>();
//                        for(int j=0; j<jsonArray1.length(); j++){
//                            genreId.add(jsonArray1.getInt(j));
//                        }
//
//                        movie model = new movie(
//                                json.getString("vote_count"), json.getString("id"), json.getString("video"), json.getString("vote_average"),
//                                json.getString("title"), json.getString("popularity"), json.getString("poster_path"), json.getString("original_language"),
//                                json.getString("original_title"), json.getString("backdrop_path"), json.getString("adult"), json.getString("overview"),
//                                json.getString("release_date"), genreId
//                        );
//                        dataMovie.add(model);
//                    }
//                    ((MovieActivity)activity).setData(dataMovie);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                stopLoading(activity);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                stopLoading(activity);
//                Log.d("VolleyError", error.getMessage());
//            }
//        });
//        VolleySingleton.getmInstance(activity).addToRequestque(jsonObjectRequest);
//    }
}
