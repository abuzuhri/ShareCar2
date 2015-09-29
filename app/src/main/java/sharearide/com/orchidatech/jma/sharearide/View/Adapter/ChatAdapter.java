package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Shadow on 9/28/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<Chat> chats;

    public ChatAdapter(List<Chat> chats) {
        this.chats = chats;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card, parent, false);
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

        // TODO: get from server
        // holder.name.setText(chats.get(position).name);
        // ..
        // ..
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cv;
        ImageView image;
        TextView name;
        TextView lastChat;
        TextView status;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.card_view);
            image = (ImageView) v.findViewById(R.id.chat_image);
            name = (TextView) v.findViewById(R.id.chat_name);
            lastChat = (TextView) v.findViewById(R.id.chat_lastChat);
            status = (TextView) v.findViewById(R.id.chat_status);
        }
    }
}
