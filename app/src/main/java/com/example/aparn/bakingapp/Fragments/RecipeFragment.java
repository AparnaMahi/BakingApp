package com.example.aparn.bakingapp.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aparn.bakingapp.Data.RecipeAdapter;
import com.example.aparn.bakingapp.MainActivity;
import com.example.aparn.bakingapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {

    @BindView(R.id.recipeList_rv)
    RecyclerView cardRecyclerView;
    private RecyclerView.Adapter cardAdapter;
    private RecyclerView.LayoutManager cardLayoutManager;
    public static final String TAG = "BakingApp";

    public RecipeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.receipe_list, container, false);
        ButterKnife.bind(this, view);
        cardRecyclerView.setHasFixedSize(true);

        if (MainActivity.isTablet) {
            cardLayoutManager = new GridLayoutManager(getContext(), 3,
                    LinearLayoutManager.VERTICAL, false);
            cardRecyclerView.setLayoutManager(cardLayoutManager);
        } else {
            cardLayoutManager = new LinearLayoutManager(getContext());
            cardRecyclerView.setLayoutManager(cardLayoutManager);
        }

        if (!isOnline(getContext())) {
            Snackbar.make(container, "No Network Connection", Snackbar.LENGTH_LONG).show();
            return null;
        }else {
            new GetRecipes_FrmNetwork().execute();
            return view;
        }
    }

     public URL buildUrl() {
        Uri uri = Uri.parse(MainActivity.json_URL);
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
    // from stackoverflow
    private static boolean isOnline(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connMgr != null ? connMgr.getActiveNetworkInfo() : null;
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class GetRecipes_FrmNetwork extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = buildUrl();
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(10000);
                try {
                    InputStream in = urlConnection.getInputStream();
                    Scanner scanner = new Scanner(in);
                    scanner.useDelimiter("\\A");
                    boolean hasInput = scanner.hasNext();
                    if (hasInput) {
                        return scanner.next();
                    } else {
                        scanner.close();
                        return null;
                    }
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            cardAdapter = new RecipeAdapter(getContext(), jsonString);
            cardRecyclerView.setAdapter(cardAdapter);
        }
    }
}
