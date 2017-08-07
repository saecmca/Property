package com.mani.property.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.property.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListMapAdapter extends RecyclerView.Adapter<ListMapAdapter.MyHolder> {

    Context mcontent;
    ArrayList<PropertyModel> arrayList;


    public ListMapAdapter(Context context, ArrayList<PropertyModel> datelist) {
        arrayList = datelist;
        this.mcontent = context;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listproperty, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        try {
            PropertyModel positionMovie = arrayList.get(position);
            holder.tvType.setText("  " + positionMovie.getPosting_type());
            if (positionMovie.getPosting_type().contains("FOR RENT"))
                holder.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_rent, 0, 0, 0);
            else if (positionMovie.getPosting_type().contains("FOR SALE"))
                holder.tvType.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle_sale, 0, 0, 0);
            holder.tvAmt.setText("$" + positionMovie.getAmount());
            holder.tvBed.setText(positionMovie.getBedrooms() + " Bed(s)");
            holder.tvBoth.setText(positionMovie.getBathrooms() + " Bath(s)");
            holder.tvArea.setText(positionMovie.getSquareaft() + " Sqfts");
            holder.tvAddr.setText(positionMovie.getStreet() + ", " + positionMovie.getCity());
            if (positionMovie.getImages().getUrl() != null)
                Picasso.with(mcontent).load(positionMovie.getImages().getUrl().get(0)).into(holder.ivImageUrl);
            else holder.ivImageUrl.setImageResource(R.drawable.loginbg);

            if (positionMovie.isFavorite())
                holder.iv_Favi.setImageResource(R.drawable.icon_favorite_red);
            else holder.iv_Favi.setImageResource(R.drawable.icon_favorite_trans);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvType, tvBed, tvBoth, tvAddr, tvAmt, tvArea;
        private ImageView ivImageUrl, iv_Favi;

        public MyHolder(View itemView) {
            super(itemView);
            tvType = (TextView) itemView.findViewById(R.id.tv_type);
            tvBed = (TextView) itemView.findViewById(R.id.tvbed);
            tvBoth = (TextView) itemView.findViewById(R.id.tvBoth);
            tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
            tvAmt = (TextView) itemView.findViewById(R.id.tvAmt);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);
            ivImageUrl = (ImageView) itemView.findViewById(R.id.iv_Image);
            iv_Favi = (ImageView) itemView.findViewById(R.id.iv_Favi);

        }
    }

}