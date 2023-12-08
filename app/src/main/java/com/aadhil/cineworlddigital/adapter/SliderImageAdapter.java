package com.aadhil.cineworlddigital.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.aadhil.cineworlddigital.R;
import com.aadhil.cineworlddigital.model.SliderImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SliderImageAdapter extends PagerAdapter {
    final private AppCompatActivity activity;
    final private ArrayList<SliderImage> images;

    public SliderImageAdapter(AppCompatActivity activity, ArrayList<SliderImage> images) {
        this.activity = activity;
        this.images = images;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.image_slider, container, false);
        ImageView imageView = view.findViewById(R.id.imageView4);

        Picasso.get().load(images.get(position).getImageUri()).into(imageView);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
