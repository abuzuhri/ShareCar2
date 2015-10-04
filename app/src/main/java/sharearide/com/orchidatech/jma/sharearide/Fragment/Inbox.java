package sharearide.com.orchidatech.jma.sharearide.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inbox, null, false);
        inbox_rv = (RecyclerView) view.findViewById(R.id.inbox_rv);
        inbox_progress = (ProgressBar) view.findViewById(R.id.inbox_progress);
        messages = new ArrayList<>();
        messagesData = new HashMap<>();
        adapter = new ChatAdapter(getActivity(), messages, messagesData, new ChatAdapter.OnRecycleViewItemClicked() {

            @Override
            public void onItemClicked(Chat selected_chat, User sender, User receiver) {

            }
        });
        inbox_rv.setAdapter(adapter);
        last_chatting_users(getActivity(), new OnInboxFetchListener() {

            @Override
            public void onFetchInboxSucceed(ArrayList<Chat> allMessages, Map<Chat, ArrayList<User>> allMessagesData) {
                messages = allMessages;
                messagesData = allMessagesData;
                inbox_progress.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFetchInboxFailed(String error) {

            }
        }, getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).getLong("id", -1));
        return view;
    }


    public void last_chatting_users(final Context context, final OnInboxFetchListener listener, final long id){
        if(id == -1)
            return;
                    User user = UserDAO.getUserById(id);

                    if (user != null) {
                        String username = user.getUsername();
                        String password = user.getPassword();
                        MainUserFunctions.getInbox(context, listener, id, username, password);
                    }else{
                        Toast.makeText(context, "This User Not Stored In Local DB", Toast.LENGTH_LONG).show();
                    }
    }

}
