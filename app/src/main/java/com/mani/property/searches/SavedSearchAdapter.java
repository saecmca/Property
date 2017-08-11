package com.mani.property.searches;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.Localstorage;
import com.mani.property.favourite.FavResp;
import com.mani.property.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedSearchAdapter extends RecyclerView.Adapter<SavedSearchAdapter.MyHolder> {

    Context mcontent;
    ArrayList<SearchModel> arrayList;


    public SavedSearchAdapter(Context context, ArrayList<SearchModel> arrayList) {
        this.arrayList = arrayList;
        this.mcontent = context;

    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_saved, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {

        try {

            final SearchModel positionMovie = arrayList.get(position);
            holder.tvCity.setText(positionMovie.getSearch());
            holder.tvClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Dialogbox.isNetworkStatusAvialable(mcontent))
                        webserviceDelete(position, positionMovie.getSave_search_id());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void webserviceDelete(final int position, String searchId) {

        try {

            Dialogbox.showDialog(mcontent, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<FavResp> getcancelresponse = apiInterface.deletesearch(new DeleteSearch(Localstorage.getSavedUserId(mcontent), searchId));
            getcancelresponse.enqueue(new Callback<FavResp>() {
                @Override
                public void onResponse(Call<FavResp> call, Response<FavResp> response) {
                    FavResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        arrayList.remove(position);
                        notifyDataSetChanged();
                    } else {
                        Dialogbox.alerts(mcontent, model.getStatus().getDescription(), "2");
                    }
                    Dialogbox.dismissDialog();
                }

                @Override
                public void onFailure(Call<FavResp> call, Throwable t) {
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
        private TextView tvCity, tvClose;

        public MyHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            tvClose = (TextView) itemView.findViewById(R.id.tvClose);

        }
    }

}