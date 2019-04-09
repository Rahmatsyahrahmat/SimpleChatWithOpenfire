package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.MessageAdapter;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

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
import org.jivesoftware.smackx.mam.MamManager;
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


    private static final String SENDER = "sabdo";
    private static final String RECIEVER = "rahmat";
    public static final String IPV4 = "192.168.1.143";


    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
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
        messageAdapter = new MessageAdapter(getContext(), messageData);
        etMessage = v.findViewById(R.id.etSendMessage);
        btnSend = v.findViewById(R.id.btnSend);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(messageAdapter);

        //connection
        setConnection();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagePesan = etMessage.getText().toString();
                if (messagePesan.length() > 0) {

                    //send message to ? Reciever
                    sendMessage(messagePesan, RECIEVER + "@desktop-ra3jkd5");
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

                MessageData data = new MessageData(SENDER, messagePesan);
                messageAdapter.addItem(data);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                etMessage.getText().clear();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void oldMessage(String user) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(user);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        MamManager manager = MamManager.getInstanceFor(connection);
        MamManager.MamQueryResult r = null;
        try {
            r = manager.mostRecentPage(jid, 10);
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
        if (r.forwardedMessages.size() >= 1) //printing first of them
        {
            for (int i = 0; i < r.forwardedMessages.size(); i++) {
                Message message = (Message) r.forwardedMessages.get(i).getForwardedStanza();
                Log.i("mam", "message received" + message.getBody());

                final MessageData data = new MessageData(message.getFrom().toString().split("@")[0], message.getBody());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.addItem(data);
                    }
                });
            }

        }
    }

    private void sendFile(String user) {


    }

    private void setConnection() {
        // Create the configuration for this new connection

        new Thread() {
            @Override
            public void run() {

                InetAddress addr = null;
                try {
                    //inter ip4
                    addr = InetAddress.getByName(IPV4);
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
                    serviceName = JidCreate.domainBareFrom(IPV4);
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setUsernameAndPassword(SENDER, "1234567890")
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

                        //get history chat
                        oldMessage(RECIEVER + "@desktop-ra3jkd5");

// Assume we've created an XMPPConnection name "connection"._
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, final Message message, Chat chat) {
                                Log.i(TAG, "New message from " + from + " : " + message.getBody());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        MessageData data = new MessageData(RECIEVER, message.getBody().toString());
                                        messageAdapter.addItem(data);
                                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
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
