package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sharearide.com.orchidatech.jma.sharearide.Chat.ChatActivity;
import sharearide.com.orchidatech.jma.sharearide.Chat.Common;
import sharearide.com.orchidatech.jma.sharearide.Database.DAO.UserDAO;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.Chat;
import sharearide.com.orchidatech.jma.sharearide.Database.Model.User;
import sharearide.com.orchidatech.jma.sharearide.Logic.MainUserFunctions;
import sharearide.com.orchidatech.jma.sharearide.R;
import sharearide.com.orchidatech.jma.sharearide.Utility.InternetConnectionChecker;
import sharearide.com.orchidatech.jma.sharearide.View.Adapter.ChatAdapter;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInboxFetchListener;
import sharearide.com.orchidatech.jma.sharearide.View.Interface.OnInternetConnectionListener;


public class Inbox extends Fragment {

    private FloatingActionButton addMessage;
    private Toolbar tool_bar;
    private ArrayList<Chat> messages;
    Map<Chat, ArrayList<User>> messagesData;
    ChatAdapter adapter;
    RecyclerView inbox_rv;
    ProgressBar inbox_progress;
    private LinearLayoutManager llm;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, null, false);
        inbox_rv = (RecyclerView) view.findViewById(R.id.inbox_rv);
        inbox_progress = (ProgressBar) view.findViewById(R.id.inbox_progress);
        inbox_rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());

        messages = new ArrayList<>();
        messagesData = new HashMap<>();
        adapter = new ChatAdapter(getActivity(), messages, messagesData, new ChatAdapter.OnRecycleViewItemClicked() {

            @Override
            public void onItemClicked(Chat selected_chat, User sender, User receiver) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                String receiverEmail;
                String senderEmail;
                Long receiverId;
                long senderId = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1);


                long id = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1);
                if (id == sender.getRemoteId()) {
                    receiverId = receiver.getRemoteId();
                    receiverEmail = receiver.getEmail();
                    senderEmail = sender.getEmail();
                } else {
                    receiverId = sender.getRemoteId();
                    receiverEmail = sender.getEmail();
                    senderEmail = receiver.getEmail();
                }

                intent.putExtra("ReceiverEmail", receiverEmail);
                intent.putExtra("ReceiverId", receiverId);
                intent.putExtra("SenderEmail", senderEmail);
                intent.putExtra("SenderId", senderId);
                //intent.putExtra(Common.PROFILE_ID, String.valueOf(myId));
                startActivity(intent);


            }
        });
        inbox_rv.setLayoutManager(llm);

        inbox_rv.setAdapter(adapter);
        InternetConnectionChecker.isConnectedToInternet(getActivity(), new OnInternetConnectionListener() {
            @Override
            public void internetConnectionStatus(boolean status) {
                if (status) {
                    last_chatting_users(getActivity(), new OnInboxFetchListener() {

                        @Override
                        public void onFetchInboxSucceed(ArrayList<Chat> allMessages, Map<Chat, ArrayList<User>> allMessagesData) {
                            messages.addAll(allMessages);
                            messagesData.putAll(allMessagesData);
                            inbox_progress.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFetchInboxFailed(String error) {
                            inbox_progress.setVisibility(View.GONE);


                        }
                    }, getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
                } else {
                    LayoutInflater li = LayoutInflater.from(getActivity());
                    View v = li.inflate(R.layout.warning, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    // set more_info.xml to alertdialog builder
                    alertDialogBuilder.setView(v);
                    TextView tittle = (TextView) v.findViewById(R.id.tittle);
                    TextView textView7 = (TextView) v.findViewById(R.id.textView7);
                    ImageButton close_btn = (ImageButton) v.findViewById(R.id.close_btn);
                    Typeface font= Typeface.createFromAsset(getActivity().getAssets(), "fonts/roboto_light.ttf");
                    tittle.setTypeface(font);
                    textView7.setTypeface(font);


                    // create alert dialog
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    close_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            inbox_progress.setVisibility(View.GONE);

                        }
                    });
                    // show it
                    alertDialog.show();
                }


            }
        });

        return view;
    }


    public void last_chatting_users(final Context context, final OnInboxFetchListener listener, final long id){
        if(id == -1)
            return;
        User user = UserDAO.getUserById(id);

        if (user != null) {
            String username = user.getUsername();
            String password = user.getPassword();
            MainUserFunctions.getInbox(context, listener, id);
        }else{
            Toast.makeText(context, "This User Not Stored In Local DB", Toast.LENGTH_LONG).show();
        }
    }

}
