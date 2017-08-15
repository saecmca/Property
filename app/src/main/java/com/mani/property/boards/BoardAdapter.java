package com.mani.property.boards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.property.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.MyHolder> {

    Context mcontent;
    ArrayList<BoardModel> arrayList;


    public BoardAdapter(Context context, ArrayList<BoardModel> datelist) {
        arrayList = datelist;
        this.mcontent = context;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        try {

            BoardModel positionMovie = arrayList.get(position);

            Log.w("asd", "onBindViewHolder: "+positionMovie.getProperties().size());
            holder.tvCount.setText(""+positionMovie.getProperties().size());
            if(positionMovie.getProperties().get(position).getImages().getUrl()!=null) {
                Picasso.with(mcontent).load(positionMovie.getProperties().get(position).getImages().getUrl().get(0)).into(holder.ivGrid);
                Picasso.with(mcontent).load(positionMovie.getProperties().get(position).getImages().getUrl().get(1)).into(holder.ivGrid1);
            }
             holder.tvTitle.setText(positionMovie.getProperties().get(0).getCity());
            //else holder.iv_Favi.setImageResource(R.drawable.icon_favorite_trans);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvCount, tvTitle, tvBoth, tvAddr, tvAmt, tvArea;
        private ImageView ivImageUrl, ivGrid,ivGrid1;

        public MyHolder(View itemView) {
            super(itemView);
            tvCount=(TextView)itemView.findViewById(R.id.tvCount);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            ivGrid = (ImageView) itemView.findViewById(R.id.ivGrid);
            ivGrid1 = (ImageView) itemView.findViewById(R.id.ivGrid1);

        }
    }

}