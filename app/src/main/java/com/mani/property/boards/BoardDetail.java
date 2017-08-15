package com.mani.property.boards;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.mani.property.R;
import com.mani.property.common.Dialogbox;
import com.mani.property.home.PropertyModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Mani on 15-08-2017.
 */

public class BoardDetail extends AppCompatActivity {
    @BindView(R.id.rclBoard)
    RecyclerView rclBoardDtls;
    @BindView(R.id.toolTitle)
    TextView toolTitle;
    ArrayList<PropertyModel> arrBoardDtls = new ArrayList<>();
    public static TextView tvNoBoard;
    private String strBoardId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_screen);
        tvNoBoard = (TextView) findViewById(R.id.tvNoBoard);
        ButterKnife.bind(this);
        try {
            Intent intent = getIntent();

            int position = intent.getIntExtra(("BoardDtls"), -1);
            strBoardId = intent.getStringExtra("BoardID");
            arrBoardDtls = BoardScreen.arrBoards.get(position).getProperties();
            toolTitle.setText(arrBoardDtls.get(0).getCity());
            rclBoardDtls.setHasFixedSize(true);
            rclBoardDtls.setLayoutManager(new LinearLayoutManager(this));
            if (Dialogbox.isNetworkStatusAvialable(this)) {
                SelectBoardAdapter selectBoardAdapter = new SelectBoardAdapter(BoardDetail.this, arrBoardDtls, strBoardId);
                rclBoardDtls.setAdapter(selectBoardAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.ivMenu)
    public void onViewClicked() {
        finish();
    }
}
