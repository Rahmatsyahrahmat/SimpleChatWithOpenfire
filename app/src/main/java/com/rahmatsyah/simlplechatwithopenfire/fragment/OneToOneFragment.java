package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.MessageAdapter;
import com.rahmatsyah.simlplechatwithopenfire.model.ChatData;
import com.rahmatsyah.simlplechatwithopenfire.model.FileData;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
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
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferNegotiator;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.mam.MamManager;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.EntityFullJid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Resourcepart;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jxmpp.util.XmppStringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToOneFragment extends Fragment {


    private static final String SENDER = "renal";
    private static final String RECIEVER = "rahmat";
    public static final String IPV4 = "192.168.100.142";
    private static final String RESOURCE = "resource";
    //renal RESOURCE =
    //rahmat RESOURCE =

    private static final int CAMERA_REQUEST = 1888;
    private static final int FILE_REQUEST = 2888;


    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<ChatData> chatData = new ArrayList<>();
    private AbstractXMPPConnection connection;
    public static final String TAG = OneToOneFragment.class.getSimpleName();
    private EditText etMessage;
    private ImageView btnSend, btnCamera, btnFile;



    public OneToOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one_to_one, container, false);

        recyclerView = v.findViewById(R.id.rvOne);
        messageAdapter = new MessageAdapter(getContext(), chatData);
        etMessage = v.findViewById(R.id.etSendMessage);
        btnSend = v.findViewById(R.id.btnSend);
        btnCamera = v.findViewById(R.id.btnCamera);
        btnFile = v.findViewById(R.id.btnFile);


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
                    sendMessage(messagePesan, RECIEVER + "@desktop-m97vqsb");
                }
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
            }
        });
        btnFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent chooseFile;
//                Intent intent;
//                chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.setType("*/*");
//                intent = Intent.createChooser(chooseFile, "Choose a file");
//                startActivityForResult(intent, FILE_REQUEST);

                Intent intent = new Intent(getContext(), FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                        .setShowFiles(true)
                        .setCheckPermission(true)
                        .setMaxSelection(1)
                        .setSkipZeroSizeFiles(true)
                        .build());
                startActivityForResult(intent, FILE_REQUEST);
            }
        });

        return v;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if (requestCode==CAMERA_REQUEST){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,75,stream);

                try {
                    sendImage(RECIEVER+"@desktop-m97vqsb",bitmap,"percobaan");
                    ImageData imageData = new ImageData(SENDER, bitmap);
                    messageAdapter.addItem(imageData);
                    recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                } catch (XMPPException e) {
                    e.printStackTrace();
                }
            }
            else if (requestCode==FILE_REQUEST){
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Log.i("Bangsat",files.get(0).getPath());
                File file = new File(files.get(0).getPath());
                Log.i("Bangsat", String.valueOf(file.canRead()));

                sendFile(RECIEVER+"@desktop-m97vqsb",file,file.getName());
                FileData fileData = new FileData(SENDER,file.getName(),file);
                messageAdapter.addItem(fileData);
                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
//                Uri uri = data.getData();
//                Log.i("Bangsat",data.getData().getPath());
//                File file =  new File(data.getData().getPath().split(":")[1]);
//                Log.i("Bangsat", String.valueOf(file.exists()));
//                Log.i("Bangsat", String.valueOf(file.setExecutable(true,false)));
//                Log.i("Bangsat", String.valueOf(file.canRead()));
//
//
//
//                sendFile(RECIEVER+"@desktop-m97vqsb",file,file.getName());
//                FileData fileData = new FileData(SENDER,file.getName(),file);
//                messageAdapter.addItem(fileData);
//                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
            }
        }
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
            Log.i("ini",e.getMessage());
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
                XMPPTCPConnectionConfiguration config = null;
                try {
                    config = XMPPTCPConnectionConfiguration.builder()
                            .setUsernameAndPassword(SENDER, "12345678")
                            .setPort(5222)
                            .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                            .setXmppDomain(serviceName)
                            .setHostnameVerifier(verifier)
                            .setHostAddress(addr)
                            .setDebuggerEnabled(true)
                            .setResource(Resourcepart.from(RESOURCE))
                            .build();
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }



                connection = new XMPPTCPConnection(config);

                try {
                    connection.connect();

                    connection.login();
                    if (connection.isAuthenticated() && connection.isConnected()) {
                        Log.e(TAG, "run : auth done and connect success");

                        //get history chat
//                        oldMessage(RECIEVER + "@desktop-m97vqsb");

// Assume we've created an XMPPConnection name "connection"._
                        ChatManager chatManager = ChatManager.getInstanceFor(connection);
                        chatManager.addIncomingListener(new IncomingChatMessageListener() {
                            @Override
                            public void newIncomingMessage(EntityBareJid from, final Message message, Chat chat) {
                                Log.i(TAG, "New message from " + from + " : " + message.getBody());
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//
//
//                                        try {
//                                            MessageData data = new MessageData(RECIEVER, message.getBody().toString());
//                                            Log.i("Ini dari incoming : ",message.getFrom().toString());
//                                            messageAdapter.addItem(data);
//                                            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
//                                            sleep(5000);
//                                        } catch (InterruptedException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                    }
//                                });

                            }
                        });

                        FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
                        manager.addFileTransferListener(new FileTransferListener() {
                            @Override
                            public void fileTransferRequest(final FileTransferRequest request) {
                                Log.i("Namanya","masuk");
                                final IncomingFileTransfer transfer = request.accept();

//                                final File file = new File(mf.getAbsoluteFile()+"/Chat/" + transfer.getFileName());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                            File file = new File(request.getFileName());
                                            FileData fileData = new FileData("coba",file.getName(),file);
                                            messageAdapter.addItem(fileData);
                                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);


                                    }
                                });
//                                InputStream inputStream = null;
//                                try {
//                                    inputStream = transfer.recieveFile();
//                                    final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ImageData imageData = new ImageData("coba",bitmap);
//                                            messageAdapter.addItem(imageData);
//                                        }
//                                    });
//                                } catch (SmackException e) {
//                                    e.printStackTrace();
//                                } catch (XMPPException.XMPPErrorException e) {
//                                    e.printStackTrace();
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }




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

    public void sendImage(String user, Bitmap bitmap, String filename) throws XMPPException {


        EntityFullJid jid = null;
        try {
            jid = JidCreate.entityFullFrom(user+"/"+RESOURCE);
            Log.i("coba",jid.toString());
            FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
            // Create the outgoing file transfer
            final OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(jid);
            // Send the file
            //transfer.sendFile(new File("abc.txt"), "You won't believe this!");
            transfer.sendStream(new ByteArrayInputStream(convertFileToByte(bitmap)), filename, convertFileToByte(bitmap).length, "A greeting");
        } catch (Exception e) {
            Log.i("hyhy",e.getMessage());
        }

//        String destination = roster.getPresence(jid).getFrom();
        // Create the file transfer manager


    }
    public void sendFile(String user, File file, String filename){
        EntityFullJid jid = null;
        try {
            jid = JidCreate.entityFullFrom(user+"/"+RESOURCE);
            Log.i("coba",jid.toString());
            FileTransferManager manager = FileTransferManager.getInstanceFor(connection);


            FileTransferNegotiator.getInstanceFor(connection);
            // Create the outgoing file transfer
            final OutgoingFileTransfer transfer = manager.createOutgoingFileTransfer(jid);
            // Send the file
//            transfer.sendFile(new File("abc.txt"), "You won't believe this!");
//            transfer.sendStream(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), filename, file.length(), "A greeting");


            transfer.sendFile(file,"A greeting");

//            transfer.sendStream(bytesStream,filename,file.length(),"A greeting");
//            transfer.sendStream(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), filename, file.length(), "A greeting");



        } catch (Exception e) {
            Log.i("Bangsat",e.getMessage());
        }
    }
    public byte[] convertFileToByte(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


}
