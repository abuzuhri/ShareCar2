package sharearide.com.orchidatech.jma.sharearide.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sharearide.com.orchidatech.jma.sharearide.Constant.AppConstant;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Ride;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.R;

/**
 * Created by Shadow on 9/28/2015.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private final Map<Chat, ArrayList<User>> chats_data;
    private final Activity activity;
    private final OnRecycleViewItemClicked listener;
    ArrayList<Chat> chats;
Typeface font;
    public ChatAdapter(Activity activity, ArrayList<Chat> chats, Map<Chat, ArrayList<User>> chats_data, OnRecycleViewItemClicked listener) {
        this.activity = activity;
        this.listener = listener;
        this.chats = chats;
        this.chats_data = chats_data;
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
        if(chats != null){
            String message_text = chats.get(position).getMessage();
            //    Toast.makeText(activity,""+message_text,Toast.LENGTH_LONG).show();
            String contacted_person;
            long user_id = activity.getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1);
            String imagUrl;

            if(chats_data.get(chats.get(position)).get(0).getRemoteId() != user_id){
                contacted_person = chats_data.get(chats.get(position)).get(0).getUsername();
                imagUrl =  chats_data.get(chats.get(position)).get(0).getImage();
            }else{
                contacted_person = chats_data.get(chats.get(position)).get(1).getUsername();
                imagUrl =  chats_data.get(chats.get(position)).get(1).getImage();

            }
            String chat_date = AppConstant.DateConvertion.getDate(chats.get(position).getDateTime());
            holder.date.setText(chat_date);
            holder.lastChat.setText(message_text);
            holder.name.setText(contacted_person);
            font= Typeface.createFromAsset(activity.getAssets(), "fonts/roboto_regular.ttf");
            holder.date.setTypeface(font);
            holder.lastChat.setTypeface(font);
            holder.name.setTypeface(font);

            if(TextUtils.isEmpty(imagUrl)){
                holder.image.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_contact_picture));
            }
            else
                Picasso.with(activity).load(Uri.parse(imagUrl)).into(holder.image);
        }


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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        CardView cv;
        CircleImageView image;
        TextView name;
        TextView lastChat;
        TextView date;

        public ViewHolder(View v) {
            super(v);
            cv = (CardView) v.findViewById(R.id.card_view);
            image = (CircleImageView) v.findViewById(R.id.chat_image);
            name = (TextView) v.findViewById(R.id.chat_name);
            lastChat = (TextView) v.findViewById(R.id.chat_lastChat);
            date = (TextView) v.findViewById(R.id.chat_date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listener.onItemClicked(chats.get(position), chats_data.get(chats.get(position)).get(0), chats_data.get(chats.get(position)).get(1));

        }
    }

    public interface OnRecycleViewItemClicked{
        public void onItemClicked(Chat selected_chat, User sender, User receiver);
    }
}
