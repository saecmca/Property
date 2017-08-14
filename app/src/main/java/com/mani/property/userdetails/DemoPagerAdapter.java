package com.mani.property.userdetails;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mani.property.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DemoPagerAdapter extends PagerAdapter {
    SparseArray<View> views = new SparseArray<>();

    List<String> listofPersons;
    LayoutInflater inflater;
    Context context;

    public DemoPagerAdapter(Context context, List<String> listofPersons) {
        this.context = context;
        this.listofPersons = listofPersons;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listofPersons.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        // TODO Auto-generated method stub
        return view == ((View) obj);

    }


    public Object instantiateItem(ViewGroup container, final int position)

    {

        ImageView imgpae;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.viewpager_item, container, false);
        imgpae = (ImageView) itemView.findViewById(R.id.view_image);


        try {
            // txtCompn.setText((position + a_1) + "/" + StuFirstNM.length);


            if (!TextUtils.isEmpty(listofPersons.get(position).toString())) {
                //   Log.w("test", row_pos.getMovieurl()+"-name="+row_pos.getMovieName());
                Picasso.with(context).load(listofPersons.get(position)).into(imgpae);
            } else {
                imgpae.setBackgroundResource(R.drawable.loginbg);
            }


            ((ViewPager) container).addView(itemView);
            views.put(position, itemView);

        } catch (Exception e)

        {
            e.printStackTrace();
        }

        return itemView;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;

        ((ViewPager) container).removeView(view);
        views.remove(position);
        view = null;

    }


}

