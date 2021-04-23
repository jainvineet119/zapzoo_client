package com.example.v4u.category.beveragecat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.v4u.Login;
import com.example.v4u.ProductDetailsActivity;
import com.example.v4u.R;
import com.example.v4u.adapters.ProductListAdapter;
import com.example.v4u.db.DatabaseHandler;
import com.example.v4u.listener.SingleProductOnItemClickListener;
import com.example.v4u.model.Product;
import com.example.v4u.model.SingleProduct;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link tea_coffee#newInstance} factory method to
 * create an instance of this fragment.
 */
public class tea_coffee extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "category";
    private static final String ARG_PARAM2 = "sub_category";

    // TODO: Rename and change types of parameters
    private String category;
    private String sub_category;
    private CardView p1, p2;
    private OkHttpClient client;
    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;
    private List<SingleProduct> productList = new ArrayList<>();
    private SingleProductOnItemClickListener listener;
    private DatabaseHandler databaseHandler;
    private ProgressBar progressBar;
    private SharedPreferences preferences;

    public tea_coffee() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment tea_coffee.
     */
    // TODO: Rename and change types and number of parameters
    public static tea_coffee newInstance(String param1, String param2) {
        tea_coffee fragment = new tea_coffee();
        Bundle args = new Bundle();
        args.putString("category", param1);
        args.putString("sub_category", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        databaseHandler = new DatabaseHandler(getContext());
        productList.clear();
        if (getArguments() != null) {
            category = getArguments().getString(ARG_PARAM1);
            sub_category = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_tea_coffee, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        listener = new SingleProductOnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
                intent.putExtra("item_id", productList.get(position).getId());
                intent.putExtra("item_name", productList.get(position).getName());
                intent.putExtra("item_price", productList.get(position).getPrice());
                intent.putExtra("item_details", productList.get(position).getDetails());
                intent.putExtra("image_url", productList.get(position).getImage_url());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(int position) {
                if (databaseHandler.addToCart(new Product(productList.get(position).getId(), productList.get(position).getName(), productList.get(position).getImage_url(), productList.get(position).getPrice(), 1))) {
                    Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Already Exist", Toast.LENGTH_SHORT).show();
                }
            }
        };

        productList.clear();
        productListAdapter = new ProductListAdapter(productList, listener, getContext());
        recyclerView.setAdapter(productListAdapter);

        RequestBody formBody = new FormEncodingBuilder()
                .add("category", category)
                .add("sub-category", sub_category)
                .add("shop_id", preferences.getString("shop_id", ""))
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/getProduct")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String res = response.body().string();
                try {
                    JSONArray jsonArray = new JSONArray(res);
                    Log.d("#####", "Array Size : "+sub_category+" "+jsonArray.length());
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        SingleProduct singleProduct = new SingleProduct();
                        singleProduct.setId(jsonobject.getString("_id"));
                        singleProduct.setName(jsonobject.getString("name"));
                        singleProduct.setDetails(jsonobject.getString("details"));
                        singleProduct.setCategory(jsonobject.getString("category"));
                        singleProduct.setSub_category(jsonobject.getString("sub-category"));
                        singleProduct.setImage_url(jsonobject.getString("images"));
                        singleProduct.setPrice(Integer.parseInt(jsonobject.getString("price")));
                        singleProduct.setQuantity(jsonobject.getString("quantity"));
                        Log.d("#####", ""+sub_category+" "+singleProduct.getName());
                        productList.add(singleProduct);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //productListAdapter.notifyDataSetChanged();
                //Log.d("#####", ""+productList.size());
                if(getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(productListAdapter != null) {
                                productListAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                            }
                            //Toast.makeText(getActivity(), ""+res, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return root;
    }
}