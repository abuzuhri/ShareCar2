package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Bahaa on 7/10/2015.
 */
public class MyRidesAdapter extends RecyclerView.Adapter<MyRidesAdapter.ViewHolder> {
    private User user;
    ArrayList<Ride> my_rides;
    OnRecycleViewItemClicked listener;
    Activity activity;

    public MyRidesAdapter(Activity activity, ArrayList<Ride> my_rides, User user, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.my_rides = my_rides;
        this.listener = listener;
        this.user = user;
    }

    @Override
    public MyRidesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        // ...
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(MyRidesAdapter.ViewHolder holder, int position) {
        if (my_rides != null) {
            String name = user.getUsername();
            holder.textView_displayName.setText(name);
            String date_time = AppConstant.DateConvertion.getDate(my_rides.get(position).getDateTime());
            holder.textView_time.setText(date_time);
            Picasso.with(activity).load(Uri.parse(user.getImage())).into(holder.result_img);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return my_rides.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        TextView textView_displayName;
        TextView textView_time;
        private ImageView  date, time;
        private CircleImageView result_img;

        public ViewHolder(View v) {
            super(v);
            textView_displayName = (TextView) v.findViewById(R.id.textView_displayName);
            textView_time = (TextView) v.findViewById(R.id.textView_time);
            result_img = (CircleImageView) v.findViewById(R.id.result_img);
            time = (ImageView) v.findViewById(R.id.time);

            Display display = activity.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();
//            result_img.getLayoutParams().height = (int) (height * 0.08);
//            result_img.getLayoutParams().width = (int) (width * 0.14);


            time.getLayoutParams().height = (int) (height * 0.03);
            time.getLayoutParams().width = (int) (width * 0.05);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onItemClicked(my_rides.get(position));
        }

    }


    public interface OnRecycleViewItemClicked {
        public void onItemClicked(Ride selected_ride);
    }
}
