package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Shadow on 9/22/2015.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    // private String[] mDataset;

    List<Ride> rides;

    public MyAdapter(List<Ride> rides) {
        this.rides = rides;
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
        holder.textView_displayName.setText(rides.get(position).name);
        holder.textView_time.setText(rides.get(position).time);
        holder.textView_date.setText(rides.get(position).date);
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


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        TextView textView_displayName;
        TextView textView_time;
        TextView textView_date;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.card_view);
            textView_displayName = (TextView) v.findViewById(R.id.textView_displayName);
            textView_time = (TextView) v.findViewById(R.id.textView_time);
            textView_date = (TextView) v.findViewById(R.id.textView_date);
        }
    }
}