package com.example.v4u.bottom;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v4u.Login;
import com.example.v4u.R;
import com.example.v4u.adapters.CartProductAdapter;
import com.example.v4u.db.DatabaseHandler;
import com.example.v4u.listener.CartProductOnItemClickListener;
import com.example.v4u.model.Product;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.List;

public class Cart_Fragment extends Fragment {
    private DatabaseHandler databaseHandler;
    private List<Product> productList;
    private RecyclerView recyclerView;
    private CartProductAdapter cartProductAdapter;
    private CartProductOnItemClickListener listener;
    private TextView totalPrice;
    private Button checkoutBtn;
    private int total = 0;
    private AlertDialog.Builder builder;
    private SharedPreferences preferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public Cart_Fragment() {
        // Required empty public constructor
    }

    public static Cart_Fragment newInstance(String param1, String param2) {
        Cart_Fragment fragment = new Cart_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHandler = new DatabaseHandler(getActivity());
        productList = databaseHandler.getAllProduct();
        Log.d("#####", "Size "+productList.size());

        builder = new AlertDialog.Builder(getActivity());

        preferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

        Checkout.preload(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cart_, container, false);

        totalPrice = (TextView) root.findViewById(R.id.totalPrice);
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);
        checkoutBtn = (Button) root.findViewById(R.id.checkoutBtn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        builder.setTitle("Please Login to Continue");
        builder.setMessage("");
        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });
        builder.create();

        listener = new CartProductOnItemClickListener() {
            @Override
            public void onRemove(int position) {
                Toast.makeText(getActivity(), "Item Removed : "+productList.get(position).getName(), Toast.LENGTH_SHORT).show();
                databaseHandler.removeProduct(productList.get(position));
                productList.remove(position);
                cartProductAdapter.notifyDataSetChanged();
                refreshPrice();
            }

            @Override
            public void onAdd(int position) {
                databaseHandler.addQuantity(productList.get(position));
                productList.get(position).setQuantity(productList.get(position).getQuantity()+1);
                cartProductAdapter.notifyItemChanged(position);
                refreshPrice();
            }

            @Override
            public void onMinus(int position) {
                databaseHandler.minusQuantity(productList.get(position));
                productList.get(position).setQuantity(productList.get(position).getQuantity()-1);
                cartProductAdapter.notifyItemChanged(position);
                refreshPrice();
            }
        };

        cartProductAdapter = new CartProductAdapter(productList, listener, getContext());
        recyclerView.setAdapter(cartProductAdapter);

        refreshPrice();

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(preferences.getBoolean("isLogined", false) == true)
                {
                    startPayment();
                } else {
                    builder.show();
                }
            }
        });

        return root;
    }
    public void refreshPrice() {
        total = 0;
        for(Product product: productList)
        {
            total += (product.getPrice()*product.getQuantity());
        }
        totalPrice.setText("Total: "+total);
    }
    public void startPayment() {
        final Activity activity = getActivity();

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "ZapZoo");
            options.put("description", "Product Order");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf(total*100));
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }
}