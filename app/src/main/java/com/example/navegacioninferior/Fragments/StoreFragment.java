package com.example.navegacioninferior.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.navegacioninferior.Movie;
import com.example.navegacioninferior.MyApplication;
import com.example.navegacioninferior.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment {

    private static final String TAG = StoreFragment.class.getSimpleName();
    private static final String URL = "http://puntosingular.mx/json/movies2017.json";

    private RecyclerView recyclerView;
    private List<Movie> movieList;
    private StoreAdapter mAdapter;

    public StoreFragment() {
        // Required empty public constructor
    }

    public static StoreFragment newInstance(String param1, String param2){
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        movieList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), movieList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        fetchStoreItems();
        return rootView;
    }

    private void fetchStoreItems(){
        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    Toast.makeText(getActivity(), "Could't fetch the store items! Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
                List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
                }.getType());
                movieList.clear();
                movieList.addAll(items);

                //Refrescando el RecyclerView
                mAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Error: "+error.getMessage());
                Toast.makeText(getActivity(), "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge){
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); //Posición item
            int colum =  position % spanCount; //Columna item

            if (includeEdge) {
                outRect.left = spacing - colum * spacing / spanCount;
                outRect.right = (colum + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing; //Borde Superior (top)
                }

                outRect.bottom = spacing; //Item Inferior
            }else{
                outRect.left = colum * spacing / spanCount;
                outRect.right = spacing - (colum + 1) * spacing / spanCount;

                if (position >= spanCount){
                    outRect.top = spacing; //Item Superior
                }
            }
        }
    }

    //Convirtiendo dp a Pixel
    private int dpToPx(int dp){
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {

        private Context context;
        private List<Movie> movieList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price;
            public ImageView thumbnail;

            public MyViewHolder(View view){
                super(view);
                name = view.findViewById(R.id.title);
                price = view.findViewById(R.id.price);
                thumbnail = view.findViewById(R.id.thumbnail);
            }
        }

        public StoreAdapter(Context context, List<Movie> movieList){
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_item_row, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            final Movie movie = movieList.get(position);
            holder.name.setText(movie.getTitle());
            holder.price.setText(movie.getPrice());

            Glide.with(context).load(movie.getImage()).into(holder.thumbnail);
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

}
