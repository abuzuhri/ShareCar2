package sharearide.com.orchidatech.jma.sharearide.View.Adapter;
import android.app.Activity;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
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
Typeface font;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        try {
            if (rides != null) {
                String name = ridesData.get(rides.get(position)).getUsername();
//                Toast.makeText(activity, "" + name, Toast.LENGTH_LONG).show();
                holder.textView_displayName.setText(name);
                String date_time = AppConstant.DateConvertion.getDate(rides.get(position).getDateTime());
                holder.textView_time.setText(date_time);
                String imagUrl = ridesData.get(rides.get(position)).getImage();
                if(TextUtils.isEmpty(imagUrl)){
                    holder.result_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_contact_picture));
                }
                else{
                    holder.load_progress.setVisibility(View.VISIBLE);
                    Picasso.with(activity).load(Uri.parse(imagUrl)).into(holder.result_img, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.load_progress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            holder.load_progress.setVisibility(View.GONE);
                            holder.result_img.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_contact_picture));
                        }
                    });
                }

//                    Picasso.with(activity).load(Uri.parse(imagUrl)).into(holder.result_img);
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
         CircleImageView result_img;
          ImageView time;
         ProgressBar load_progress;
        public ViewHolder(View v) {
            super(v);
            textView_displayName = (TextView) v.findViewById(R.id.textView_displayName);
            textView_time = (TextView) v.findViewById(R.id.textView_time);
            result_img = (CircleImageView) v.findViewById(R.id.result_img);
            time = (ImageView) v.findViewById(R.id.time);
            load_progress = (ProgressBar) v.findViewById(R.id.load_image_progress);
            font= Typeface.createFromAsset(activity.getAssets(), "fonts/roboto_regular.ttf");
            textView_displayName.setTypeface(font);
            textView_time.setTypeface(font);


            Display display = activity.getWindowManager().getDefaultDisplay();
            int height = display.getHeight();
            int width = display.getWidth();
            result_img.getLayoutParams().height = (int) (height * 0.09);
            result_img.getLayoutParams().width = (int) (width * 0.15);


            time.getLayoutParams().height = (int) (height * 0.03);
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