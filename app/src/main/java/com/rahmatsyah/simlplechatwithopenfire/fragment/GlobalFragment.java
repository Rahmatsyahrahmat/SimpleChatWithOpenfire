package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.app.AlertDialog;
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
import android.widget.Toast;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.AdapterGlobal;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageGlobal;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.InvitationListener;
import org.jivesoftware.smackx.muc.InvitationRejectionListener;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatException;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.jivesoftware.smackx.muc.packet.MUCUser;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityJid;
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
 * <p>
 * maafkan aku menduakan hatimu,
 * adakah kesempatan kedua?
 */
public class GlobalFragment extends Fragment implements View.OnClickListener {

    public static final int PORT = 5222;
    private static final String SENDER = "sabdo";
    private static final String USER_LAIN = "dila";
    public static final String IPV4 = "192.168.100.119";
    public static final String ROOM = "coba@conference.desktop-ra3jkd5";

    private AbstractXMPPConnection connection;

    private RecyclerView recyclerView;
    private ArrayList<MessageGlobal> messageData = new ArrayList<>();
    private AdapterGlobal adapterGlobal;
    private EditText etGlobal;
    private Button btnSendGlobal, btnInvite;

    private EditText etNickname;
    private Button btnAccept, btnReject;

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
        //invite
        btnInvite = v.findViewById(R.id.btnInviteGlobal);


        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapterGlobal);


        setConnection();

        btnSendGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etGlobal.getText().toString();
                if (message.length() >= 1) {
                    sendMessage(message, ROOM);
                }
            }
        });


        //inivite
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(getContext(), "Invite", Toast.LENGTH_SHORT).show();

                alertInvite();
            }
        });

        return v;
    }

    //nickname = bambang, otherId = bambang@desktop
    private void inviteUser(EntityBareJid mucJid, String nickname, EntityBareJid otherJid) {
        Log.i("InviteUser", "Tidak masuk");
        if (connection != null) {
            MultiUserChatManager manager = MultiUserChatManager.getInstanceFor(connection);
            MultiUserChat muc = manager.getMultiUserChat(mucJid);

            //reject invitation
            muc.addInvitationRejectionListener(new InvitationRejectionListener() {
                @Override
                public void invitationDeclined(EntityBareJid invitee, String reason, Message message, MUCUser.Decline rejection) {
                    Toast.makeText(getContext(), "Invit Di tolak " + message, Toast.LENGTH_SHORT).show();
                }
            });

            try {
                muc.invite(otherJid, "Gabung Dengan Group COBA");
                Log.i("InviteUser", "Bisa");

            } catch (SmackException.NotConnectedException e) {
                Log.i("InviteUser", e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.i("InviteUser", e.getMessage());
                e.printStackTrace();
            }
            Log.i("InviteUser", "Masuk");

        }
    }

    private void alertInvite(){
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_dialog_username,null);

        etNickname = dialogView.findViewById(R.id.etNicknameInvite);
        btnAccept = dialogView.findViewById(R.id.btnAcceptInvite);
        btnReject = dialogView.findViewById(R.id.btnRejectInvite);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntityBareJid mucJid = null;
                try {
                    Log.i("InviteUser", "Mulai");

                    String nama = etNickname.getText().toString();

                    mucJid = (EntityBareJid) JidCreate.bareFrom(ROOM);
                    EntityBareJid otherJid = JidCreate.entityBareFrom(nama + "@desktop-ra3jkd5");
                    inviteUser(mucJid, nama, otherJid);

                    alertDialog.dismiss();
                    Toast.makeText(getContext(), "Invite "+nama, Toast.LENGTH_SHORT).show();
                } catch (XmppStringprepException e) {
                    Log.i("InviteUser", e.getMessage());
                    Toast.makeText(getContext(), "Gagal Invite ", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.setView(dialogView);
        alertDialog.show();

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


    private void dialogInvitRoom() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
//        alertDialog.setMessage()
    }

    private void getInvititaion() {
        MultiUserChatManager.getInstanceFor(connection).addInvitationListener(new InvitationListener() {
            @Override
            public void invitationReceived(XMPPConnection conn, MultiUserChat room, EntityJid inviter, String reason, String password, Message message, MUCUser.Invite invitation) {

            }
        });
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
                        // Crate the XMPP address (JID) of the MUC.
                        EntityBareJid mucJid = (EntityBareJid) JidCreate.bareFrom(ROOM);
// Create the nickname.
                        Resourcepart nickname = Resourcepart.from(SENDER);
// A other use (we may invite him to a MUC).
                        //join group
                        joinGroup(mucJid, nickname);

                        //get message
                        getMessage();


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

    private void getMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StanzaTypeFilter filter = new StanzaTypeFilter(Message.class);
                connection.addSyncStanzaListener(new StanzaListener() {
                    @Override
                    public void processStanza(Stanza packet) throws SmackException.NotConnectedException, InterruptedException, SmackException.NotLoggedInException {
                        Message message = (Message) packet;
                        if (message.getBody() != null) {
                            Log.i("GetOldMessageGlobal", "Masuk");
                            MessageGlobal data = new MessageGlobal(message.getFrom().toString(), message.getBody().toString());
                            adapterGlobal.addItem(data);
                            recyclerView.setAdapter(adapterGlobal);
                        }
                    }
                }, filter);
            }
        });

    }

    private void joinGroup(EntityBareJid mucJid, Resourcepart nickname) {
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
