package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Activity.Save_info;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Shadow on 9/22/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final Map<Ride, User> ridesData;
    private final OnRecycleViewItemClicked listener;
    private  Activity activity;

    // private String[] mDataset;

    ArrayList<Ride> rides;

    public MyAdapter(Activity activity, ArrayList<Ride> rides , Map<Ride, User> ridesData, OnRecycleViewItemClicked listener) {
        this.ridesData=ridesData;
        this.rides = rides;
    this.listener = listener;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            if (rides != null) {
                String name = ridesData.get(rides.get(position)).getUsername();
//                Toast.makeText(activity, "" + name, Toast.LENGTH_LONG).show();
                holder.textView_displayName.setText(name);
                String date_time = AppConstant.DateConvertion.getDate(rides.get(position).getDateTime());
                holder.textView_time.setText(date_time.split(" ")[1]);
                holder.textView_date.setText(date_time.split(" ")[0]);

            }
        }catch (NullPointerException e) {
        }





    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rides.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        TextView textView_displayName;
        TextView textView_time;
        TextView textView_date;
        private ImageView  result_img,date,time;
        public ViewHolder(View v) {
            super(v);
            textView_displayName = (TextView) v.findViewById(R.id.textView_displayName);
            textView_time = (TextView) v.findViewById(R.id.textView_time);
            textView_date = (TextView) v.findViewById(R.id.textView_date);
            result_img = (ImageView) v.findViewById(R.id.result_img);
            date = (ImageView) v.findViewById(R.id.date);
            time = (ImageView) v.findViewById(R.id.time);

            Display display = activity.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();
            result_img.getLayoutParams().height = (int) (height * 0.09);
            result_img.getLayoutParams().width = (int) (width * 0.09);

            date.getLayoutParams().height = (int) (height * 0.04);
            date.getLayoutParams().width = (int) (width * 0.05);

            time.getLayoutParams().height = (int) (height * 0.04);
            time.getLayoutParams().width = (int) (width * 0.05);

            v.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onItemClicked(rides.get(position), ridesData.get(rides.get(position)));
        }
    }

    public interface OnRecycleViewItemClicked{
        public void onItemClicked(Ride selected_ride, User target_user);
    }
}