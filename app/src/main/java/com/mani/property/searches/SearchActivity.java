package com.mani.property.searches;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.mani.property.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.ivFil)
    ImageView ivFil;
    @BindView(R.id.radoSale)
    RadioButton radoSale;
    @BindView(R.id.rdoRent)
    RadioButton rdoRent;
    @BindView(R.id.rclSearchProperty)
    RecyclerView rclSearchProperty;
    boolean checked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
         checked = radoSale.isChecked();

    }

    @OnClick({R.id.ivBack, R.id.radoSale, R.id.rdoRent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.radoSale:
                if(checked)
                    radoSale.setChecked(false);
                else radoSale.setChecked(true);
                break;
            case R.id.rdoRent:
                break;
        }
    }
}
