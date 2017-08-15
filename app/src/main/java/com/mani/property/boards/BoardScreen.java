package com.mani.property.boards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.common.ItemClickSupport;
import com.mani.property.userdetails.UserRequest;
import com.mani.property.webservice.RestClient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardScreen extends AppCompatActivity {

    @BindView(R.id.rclBoard)
    RecyclerView rclBoard;
   public  static ArrayList<BoardModel>arrBoards=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_screen);
        ButterKnife.bind(this);
        rclBoard.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        rclBoard.setLayoutManager(layoutManager);
        if(Dialogbox.isNetworkStatusAvialable(this))
            webserviceBoard();
    }

    private void webserviceBoard() {
        try {
            UserRequest userReq=new UserRequest();
            userReq.setUserId("15");
            Dialogbox.showDialog(BoardScreen.this, "Loading...");
            RestClient.APIInterface apiInterface = RestClient.getapiclient();
            Call<BoardResp> getcancelresponse = apiInterface.getBoard(userReq);
            getcancelresponse.enqueue(new Callback<BoardResp>() {
                @Override
                public void onResponse(Call<BoardResp> call, Response<BoardResp> response) {
                    final BoardResp model = response.body();
                    if (model != null && model.getStatus() != null && model.getStatus().getId().equalsIgnoreCase("1")) {
                        //arrView= model.getImages();
                        arrBoards=model.getBoards();
                        BoardAdapter boardAdapter=new BoardAdapter(BoardScreen.this,arrBoards);
                        rclBoard.setAdapter(boardAdapter);
                        ItemClickSupport.addTo(rclBoard).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                            @Override
                            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                                // position ,
                               /* ArrayList<PropertyModel> myList = new ArrayList<>();
                                myList=model.getBoards().get(position).getProperties();*/
                                Intent main=new Intent(BoardScreen.this,BoardDetail.class);
                                main.putExtra("BoardDtls",position);
                                main.putExtra("BoardID",arrBoards.get(position).getBoard_id());
                                startActivity(main);

                             /*   SelectBoardAdapter selectBoardAdapter=new SelectBoardAdapter(BoardScreen.this,model.getBoards().get(position).getProperties());
                                rclBoard.setAdapter(selectBoardAdapter);*/
                            }
                        });

                    } else {
                        Dialogbox.alerts(BoardScreen.this, model.getStatus().getDescription(), "2");
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

    @OnClick(R.id.ivMenu)
    public void onViewClicked() {
        finish();
    }
}
