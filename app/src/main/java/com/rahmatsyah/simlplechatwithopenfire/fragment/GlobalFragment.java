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
import com.rahmatsyah.simlplechatwithopenfire.adapter.AdapterGlobal;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageGlobal;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
import org.jxmpp.jid.FullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
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
public class GlobalFragment extends Fragment implements View.OnClickListener {

    public static final int PORT = 5222;
    private static final String SENDER = "rahmat";
    private static final String RECIEVER = "dhani";
    public static final String IPV4 = "192.168.0.138";
    public static final String SERVICE = "desktop-ra3jkd5";

    String mNickName;
    String mGroupChatName;
    private MultiUserChat multiUserChat;
    private AbstractXMPPConnection connection;
    private ConnectionConfiguration connectionConfiguration;

    private RecyclerView recyclerView;
    private ArrayList<MessageGlobal> messageData = new ArrayList<>();
    private AdapterGlobal adapterGlobal;
    private EditText etGlobal;
    private Button btnSendGlobal;

    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global, container, false);
        recyclerView = v.findViewById(R.id.rvGlobal);
        adapterGlobal = new AdapterGlobal(messageData);
        etGlobal = v.findViewById(R.id.etSendMessageGlobal);
        btnSendGlobal = v.findViewById(R.id.btnSendGlobal);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterGlobal);


        setConnection();

        btnSendGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etGlobal.getText().toString();
                if (message.length() >= 1) {
                    sendMessage(message, "coba@conference.desktop-ra3jkd5");
                }
            }
        });

        return v;
    }

    private void sendMessage(String message, String room) {
        try {
            EntityBareJid mucJid = (EntityBareJid) JidCreate.bareFrom(room);


            if (connection != null) {
                MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
                MultiUserChat muc2 = manager.getMultiUserChat(mucJid);

                Message pesan = new Message(room, String.valueOf(Message.Type.groupchat));
                pesan.setBody(message);
                try {
                    muc2.sendMessage(message);

                    Log.i("MengirimPesanGlobal", "Masuk");
                    MessageGlobal data = new MessageGlobal(SENDER, message);
                    adapterGlobal.addItem(data);
                    recyclerView.setAdapter(adapterGlobal);
                    etGlobal.getText().clear();

                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }


    }

    private void setConnection() {
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
                        .setPort(PORT)
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
                        Log.i("koneksi", "run : auth done and connect success");

// Assume we've created an XMPPConnection name "connection"._
                        // Create the XMPP address (JID) of the MUC.
                        EntityBareJid mucJid = (EntityBareJid) JidCreate.bareFrom("coba@conference.desktop-ra3jkd5");
// Create the nickname.
                        Resourcepart nickname = Resourcepart.from(SENDER);
// A other use (we may invite him to a MUC).
                        FullJid otherJid = JidCreate.fullFrom(SENDER + "@desktop-ra3jkd5/Smack");
                        //join group
                        joinGroup(mucJid, nickname, otherJid);

                        //get message
                        getMessage(mucJid);

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

    private void getMessage(EntityBareJid mucJid) {
        StanzaTypeFilter filter = new StanzaTypeFilter(Message.class);
        connection.addSyncStanzaListener(new StanzaListener() {
            @Override
            public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
                Message message = (Message) packet;
                if(message.getBody() != null){
                    MessageGlobal data = new MessageGlobal(message.getFrom().toString(), message.getBody().toString());
                    adapterGlobal.addItem(data);
                    recyclerView.setAdapter(adapterGlobal);
                }
            }
        }, filter);
    }

    private void joinGroup(EntityBareJid mucJid, Resourcepart nickname, FullJid otherJid) {
        MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
        MultiUserChat muc2 = manager.getMultiUserChat(mucJid);
        try {
            muc2.join(nickname);
            Log.i("JoinRoom", "Masuk");
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MultiUserChatException.NotAMucServiceException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

    }

}
