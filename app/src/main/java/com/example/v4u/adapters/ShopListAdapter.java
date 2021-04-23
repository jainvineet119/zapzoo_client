package com.example.v4u.adapters;

import android.content.Context;
import android.service.autofill.TextValueSanitizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.v4u.R;
import com.example.v4u.listener.CartProductOnItemClickListener;
import com.example.v4u.listener.ShopOnItemClickListener;
import com.example.v4u.listener.SingleProductOnItemClickListener;
import com.example.v4u.model.Product;
import com.example.v4u.model.Shop;
import com.example.v4u.model.SingleProduct;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.MyViewHolder> {

    private List<Shop> shopList;
    private ShopOnItemClickListener listener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView shop_name;
        private ImageView shop_image;
        private TextView shop_detail;

        public MyViewHolder(@NonNull View itemView, final ShopOnItemClickListener listener) {
            super(itemView);
            shop_name = itemView.findViewById(R.id.shop_name);
            shop_image = itemView.findViewById(R.id.shop_image);
            shop_detail = itemView.findViewById(R.id.shop_detail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onSelect(getAdapterPosition());
        }
    }

    public ShopListAdapter(List<Shop> shopList, Context context, ShopOnItemClickListener listener) {
        this.shopList = shopList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shops_list_row, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListAdapter.MyViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.shop_name.setText(""+shop.getName());
        holder.shop_detail.setText("480 Items | "+shop.getDistance()+" KM");
        Glide.with(context).load(shop.getImage_url()).placeholder(R.drawable.cart1).into(holder.shop_image);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}


