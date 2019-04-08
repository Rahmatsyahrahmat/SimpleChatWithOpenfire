package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rahmatsyah.simlplechatwithopenfire.AdapterOne2One;
import com.rahmatsyah.simlplechatwithopenfire.MessageData;
import com.rahmatsyah.simlplechatwithopenfire.R;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToOneFragment extends Fragment {
    private RecyclerView recyclerView;
    private AdapterOne2One adapterOne2One;
    private ArrayList<MessageData> messageData = new ArrayList<>();
    private AbstractXMPPConnection connection;
    public static final String TAG = OneToOneFragment.class.getSimpleName();
    private EditText etMessage;
    private Button btnSend;

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
        etMessage = v.findViewById(R.id.etSendMessage);
        btnSend = v.findViewById(R.id.btnSend);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), manager.getOrientation());

        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterOne2One);

        //connection
        setConnection();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagePesan = etMessage.getText().toString();
                if (messagePesan.length() > 0) {

                    //send message to ?
                    sendMessage(messagePesan, "rahmat@desktop-ra3jkd5");
                }
            }
        });

        return v;
    }

    private void sendMessage(String messagePesan, String user) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(user);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Chat chat = chatManager.chatWith(jid);
            Message newMessage = new Message();
            newMessage.setBody(messagePesan);
            try {
                chat.send(newMessage);

                MessageData data = new MessageData("send", messagePesan);
                messageData.add(data);
                adapterOne2One = new AdapterOne2One(messageData);
                recyclerView.setAdapter(adapterOne2One);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setConnection() {
        // Create the configuration for this new connection

        new Thread() {
            @Override
            public void run() {

                InetAddress addr = null;
                try {
                    //inter ip4
                    addr = InetAddress.getByName("192.168.0.138");
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                HostnameVerifier verifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return false;
                    }
                };
                DomainBareJid serviceName = null;
                try {
                    serviceName = JidCreate.domainBareFrom("192.168.0.138");
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword("sabdo", "1234567890")
                        .setPort(5222)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .setXmppDomain(serviceName)
                        .setHostnameVerifier(verifier)
                        .setHostAddress(addr)
                        .setDebuggerEnabled(true)
                        .build();
                connection = new XMPPTCPConnection(config);

                try {
                    connection.connect();

                    connection.login();
                    if (connection.isAuthenticated() && connection.isConnected()) {

                        Log.e(TAG, "run : auth done and connect success");
// Assume we've created an XMPPConnection name "connection"._
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
                                Log.i(TAG, "New message from " + from + " : " + message.getBody());
                                MessageData data = new MessageData("received", message.getBody().toString());
                                messageData.add(data);
                                //now update recycler view
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //this ui thread important otherwise error occur
                                        adapterOne2One = new AdapterOne2One(messageData);
                                        recyclerView.setAdapter(adapterOne2One);
                                    }
                                });
                            }
                        });

                    }
                } catch (
                        SmackException e)

                {
                    e.printStackTrace();
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                } catch (
                        XMPPException e)

                {
                    e.printStackTrace();
                } catch (
                        InterruptedException e)

                {
                    e.printStackTrace();
                }

            }
        }.

                start();

    }

}
