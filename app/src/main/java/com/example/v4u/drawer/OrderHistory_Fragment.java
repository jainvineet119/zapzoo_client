package com.example.v4u.drawer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.v4u.OrderDetail;
import com.example.v4u.R;
import com.example.v4u.adapters.OrdersHistoryAdapter;
import com.example.v4u.listener.ShopOnItemClickListener;
import com.example.v4u.model.Order;
import com.example.v4u.model.Product;
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
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistory_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistory_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Order> orderList;
    private RecyclerView recyclerView;
    private OkHttpClient client;
    private SharedPreferences preferences;
    private OrdersHistoryAdapter historyAdapter;
    private ShopOnItemClickListener listener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderHistory_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistory_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistory_Fragment newInstance(String param1, String param2) {
        OrderHistory_Fragment fragment = new OrderHistory_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_order_history_, container, false);

        preferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);

        client = new OkHttpClient();

        RequestBody formBody = new FormEncodingBuilder()
                .add("username", preferences.getString("username", ""))
                .build();

        Request request = new Request.Builder()
                .url("https://zapzoo.devsourabh.repl.co/fetchOrder")
                .post(formBody)
                .build();

        listener = new ShopOnItemClickListener() {
            @Override
            public void onSelect(int position) {
                //Toast.makeText(getActivity(), "Working "+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), OrderDetail.class);
                intent.putExtra("orderid", ""+orderList.get(position).getTimestamp().getTime());
                intent.putExtra("products", (Serializable) orderList.get(position).getProductList());
                startActivity(intent);
            }
        };

        orderList = new ArrayList<>();
        historyAdapter = new OrdersHistoryAdapter(getActivity(), orderList, listener);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(historyAdapter);

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
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                Log.d("#####", ""+res);

                try {
                    JSONArray jsonArray = new JSONArray(res);
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Order order = new Order();
                        order.setId(jsonObject.getString("_id"));
                        order.setTimestamp(new Timestamp(jsonObject.getLong("timestamp")));
                        order.setShop_name(jsonObject.getString("shop_id"));

                        JSONArray productsArray = jsonObject.getJSONArray("products");
                        List<Product> products = new ArrayList<>();

                        for(int j=0; j<productsArray.length(); j++)
                        {
                            JSONObject productObject = productsArray.getJSONObject(j);
                            Product product = new Product();
                            product.setId(productObject.getString("id"));
                            product.setName(productObject.getString("name"));
                            product.setImage_url(productObject.getString("image_url"));
                            product.setPrice(productObject.getInt("price"));
                            product.setQuantity(productObject.getInt("quantity"));

                            products.add(product);
                        }

                        order.setProductList(products);
                        orderList.add(order);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        historyAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        
        return root;
    }
}