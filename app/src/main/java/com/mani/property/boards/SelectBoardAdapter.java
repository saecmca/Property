package com.mani.property.boards;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.home.PropertyModel;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectBoardAdapter extends RecyclerView.Adapter<SelectBoardAdapter.MyHolder> {
     private String BoardId="";
    Context mcontent;
    ArrayList<PropertyModel> arrayList;


    public SelectBoardAdapter(Context context, ArrayList<PropertyModel> datelist,String BoardId) {
       this.arrayList = datelist;
        this.mcontent = context;
        this.BoardId=BoardId;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_boarddetail, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        try {

            final PropertyModel positionMovie = arrayList.get(position);

                holder.tvAmt.setText("$" + positionMovie.getAmount());
                holder.tvBed.setText(positionMovie.getBedrooms() + " Bed(s)");
                holder.tvBoth.setText(positionMovie.getBathrooms() + " Bath(s)");
                holder.tvArea.setText(positionMovie.getSquareaft() + " Sqfts");
                holder.tvAddr.setText(positionMovie.getStreet() + ", " + positionMovie.getCity());
                if (positionMovie.getImages().getUrl() != null)
                    Picasso.with(mcontent).load(positionMovie.getImages().getUrl().get(0)).into(holder.ivImageUrl);
                else holder.ivImageUrl.setImageResource(R.drawable.bluepattern);
            holder.tvCross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Dialogbox.isNetworkStatusAvialable(mcontent))
                        webserviceDelete(position, positionMovie.getZpid());
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void webserviceDelete(final int position, String zpid) {

        try {
            UserRequest userRequest=new UserRequest();
            DelBoardReq delBoardReq=new DelBoardReq();
            delBoardReq.setProperty_id(zpid);
            delBoardReq.setId(BoardId);
            userRequest.setUserId("15");
            userRequest.setBoard(delBoardReq);
            Dialogbox.showDialog(mcontent, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<BoardResp> getcancelresponse = apiInterface.delBoardProperty(userRequest);
            getcancelresponse.enqueue(new Callback<BoardResp>() {
                @Override
                public void onResponse(Call<BoardResp> call, Response<BoardResp> response) {
                    BoardResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        arrayList.remove(position);
                        if(arrayList.size()>0)
                            BoardDetail.tvNoBoard.setVisibility(View.GONE);
                        else BoardDetail.tvNoBoard.setVisibility(View.VISIBLE);
                        notifyDataSetChanged();
                    } else {
                        Dialogbox.alerts(mcontent, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<BoardResp> call, Throwable t) {
                    Dialogbox.dismissDialog();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView  tvBed, tvBoth, tvAddr, tvAmt, tvArea,tvCross;
        private ImageView ivImageUrl;

        public MyHolder(View itemView) {
            super(itemView);
            tvCross = (TextView) itemView.findViewById(R.id.tvCross);
            tvBed = (TextView) itemView.findViewById(R.id.tvbed);
            tvBoth = (TextView) itemView.findViewById(R.id.tvBoth);
            tvAddr = (TextView) itemView.findViewById(R.id.tvAddr);
            tvAmt = (TextView) itemView.findViewById(R.id.tvAmt);
            tvArea = (TextView) itemView.findViewById(R.id.tvArea);
            ivImageUrl = (ImageView) itemView.findViewById(R.id.iv_Image);


        }
    }

}