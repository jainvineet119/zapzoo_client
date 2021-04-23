package com.example.v4u.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.v4u.R;
import com.example.v4u.listener.ShopOnItemClickListener;
import com.example.v4u.model.Order;
import com.example.v4u.model.Product;
import com.example.v4u.model.Shop;

import java.util.Date;
import java.util.List;

public class OrdersHistoryAdapter extends RecyclerView.Adapter<OrdersHistoryAdapter.MyViewHolder> {

    private List<Order> orderList;
    private Context context;
    private ShopOnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView datetime, orderid, quantity, viewDetail;

        public MyViewHolder(@NonNull View itemView, ShopOnItemClickListener listener) {
            super(itemView);
            datetime = itemView.findViewById(R.id.datetime);
            orderid = itemView.findViewById(R.id.orderid);
            quantity = itemView.findViewById(R.id.quantity);
            viewDetail = itemView.findViewById(R.id.viewDetail);

            viewDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onSelect(getAdapterPosition());
        }
    }

    public OrdersHistoryAdapter(Context context, List<Order> orderList, ShopOnItemClickListener listener) {
        this.orderList = orderList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderhistory_row, parent, false);

        return new MyViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersHistoryAdapter.MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.datetime.setText(""+order.getTimestamp().toString());
        holder.orderid.setText("Order ID - #"+order.getTimestamp().getTime());
        holder.quantity.setText(""+order.getProductList().size()+" Items | "+calcTotal(order.getProductList())+"Rs");
    }

    private int calcTotal(List<Product> productList) {
        int sum = 0;
        for(int i=0; i<productList.size(); i++)
        {
            sum += productList.get(i).getPrice();
        }
        return sum;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}