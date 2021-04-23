package com.example.v4u.bottom;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
public class Search_Fragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EditText edtSearch;
    private OkHttpClient client;
    private ProductListAdapter productListAdapter;
    private RecyclerView recyclerView;
    private List<SingleProduct> productList = new ArrayList<>();
    private SingleProductOnItemClickListener listener;
    private DatabaseHandler databaseHandler;

    public Search_Fragment() {
        // Required empty public constructor
    }

    public static Search_Fragment newInstance(String param1, String param2) {
        Search_Fragment fragment = new Search_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient();
        databaseHandler = new DatabaseHandler(getContext());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search_, container, false);

        edtSearch = (EditText) root.findViewById(R.id.edtSearch);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productList.clear();
                search(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return root;
    }

    void search(CharSequence charSequence) {
        RequestBody formBody = new FormEncodingBuilder()
                .add("query", charSequence.toString())
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/search")
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
                    Log.d("#####", "Array Size : "+jsonArray.length());
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
                        try {
                            singleProduct.setPrice(Integer.parseInt(jsonobject.getString("price")));
                        } catch(Exception e) {
                            singleProduct.setPrice(Integer.parseInt("0"));
                        }
                        singleProduct.setQuantity(jsonobject.getString("quantity"));
                        productList.add(singleProduct);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //productListAdapter.notifyDataSetChanged();
                Log.d("#####", ""+productList.size());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        productListAdapter.notifyDataSetChanged();
                        //Toast.makeText(getActivity(), ""+res, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}