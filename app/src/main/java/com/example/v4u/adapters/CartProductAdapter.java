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
import com.example.v4u.model.Product;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.MyViewHolder> {

    private List<Product> productList;
    private CartProductOnItemClickListener listener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView item_name, item_price, item_quantity, removeBtn;
        private ImageButton minusBtn, addBtn;
        private ImageView imv;

        public MyViewHolder(@NonNull View itemView, final CartProductOnItemClickListener listener) {
            super(itemView);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            item_price = (TextView) itemView.findViewById(R.id.item_price);
            item_quantity = (TextView) itemView.findViewById(R.id.item_quantity);
            removeBtn = (TextView) itemView.findViewById(R.id.removeBtn);
            minusBtn = (ImageButton) itemView.findViewById(R.id.minusBtn);
            addBtn = (ImageButton) itemView.findViewById(R.id.addBtn);
            imv = (ImageView) itemView.findViewById(R.id.imv);

            removeBtn.setOnClickListener(this);
            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onMinus(getAdapterPosition());
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onAdd(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            listener.onRemove(getAdapterPosition());
        }
    }

    public CartProductAdapter(List<Product> productList, CartProductOnItemClickListener listener, Context context) {
        this.productList = productList;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public CartProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartitem_row, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartProductAdapter.MyViewHolder holder, int position) {
        holder.item_name.setText(productList.get(position).getName());
        holder.item_price.setText("₹ "+productList.get(position).getPrice());
        holder.item_quantity.setText(""+productList.get(position).getQuantity());
        Glide.with(context).load(productList.get(position).getImage_url()).placeholder(R.drawable.cart1).into(holder.imv);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
