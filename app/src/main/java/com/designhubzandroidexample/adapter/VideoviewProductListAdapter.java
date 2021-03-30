package com.designhubzandroidexample.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.designhubz.androidsdk.helper.Product;
import com.designhubzandroidexample.R;

import java.util.List;

public abstract class VideoviewProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Activity activity;
    List<Product> productList;

    public VideoviewProductListAdapter(Activity activity, List<Product> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View productListLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_videoview, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(productListLayout);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ProductViewHolder) holder).tvName.setText(productList.get(position).getName());
        ((ProductViewHolder) holder).tvPrice.setText("\u20B9 "+productList.get(position).getPrice());
        ((ProductViewHolder) holder).tvDesc.setText(""+productList.get(position).getDesc());
        ((ProductViewHolder) holder).tvOrgPrice.setText("\u20B9 "+productList.get(position).getOrgPrice());
        ((ProductViewHolder) holder).tvOrgPrice.setPaintFlags(((ProductViewHolder) holder).tvOrgPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    public int getItemCount() {
        if (productList != null)
            return productList.size();
        else
            return 0;
    }


    public abstract void onClickItem(int adapterPosition);

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice,tvOrgPrice,tvDesc;
        ImageView ivBookmark;
        Button btAddtobag;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvOrgPrice = itemView.findViewById(R.id.tvOrgPrice);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivBookmark = itemView.findViewById(R.id.ivBookmark);
            btAddtobag = itemView.findViewById(R.id.btAddtobag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem(getAdapterPosition());
                }
            });
        }
    }
}
