package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rahmatsyah.simlplechatwithopenfire.AdapterOne2One;
import com.rahmatsyah.simlplechatwithopenfire.MessageData;
import com.rahmatsyah.simlplechatwithopenfire.R;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToOneFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterOne2One adapterOne2One;
    private ArrayList<MessageData> messageData = new ArrayList<>();
    private AbstractXMPPConnection connection;
    public OneToOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one_to_one, container, false);

        recyclerView = v.findViewById(R.id.rvOne);
        adapterOne2One = new AdapterOne2One(messageData);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), manager.getOrientation());

        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterOne2One);


        return v;
    }

    private void setConnection(){
        // Create the configuration for this new connection
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder.setUsernameAndPassword("username", "password");
        try {
            configBuilder.setResource("SomeResource");
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        try {
            configBuilder.setXmppDomain("jabber.org");
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        AbstractXMPPConnection connection = new XMPPTCPConnection(configBuilder.build());
// Connect to the server
        connection.connect();
// Log into the server
        connection.login();
    }

}
