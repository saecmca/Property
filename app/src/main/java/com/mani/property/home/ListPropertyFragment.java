package com.mani.property.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iamhabib.easy_preference.EasyPreference;
import com.mani.property.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ListPropertyFragment extends Fragment {
    @BindView(R.id.rclProperty)
    RecyclerView rclProperty;
    Unbinder unbinder;
    // TODO: Rename and change types of parameters

    private ArrayList<PropertyModel> arrProperty=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        rclProperty.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rclProperty.setLayoutManager(mLayoutManager);
        PropertyModel object=EasyPreference.with(getActivity()).getObject("PROPERTYLIST", PropertyModel.class);

        ListMapAdapter listMapAdapter=new ListMapAdapter(getActivity(),arrProperty);
        rclProperty.setAdapter(listMapAdapter);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
