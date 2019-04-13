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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;
import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.MessageAdapter;
import com.rahmatsyah.simlplechatwithopenfire.model.ChatData;
import com.rahmatsyah.simlplechatwithopenfire.model.FileData;
import com.rahmatsyah.simlplechatwithopenfire.model.ImageData;
import com.rahmatsyah.simlplechatwithopenfire.model.MessageData;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

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
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToOneFragment extends Fragment{


    private static final String SENDER = "renal";
    private static final String RECIEVER = "rahmat";
    public static final String IPV4 = "192.168.100.142";
    private static final String RESOURCE = "/resource";
    private static final String DOMAIN = "@desktop-m97vqsb";

    private static final int FILE_REQUEST = 2888;

    private boolean isReceive = false;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<ChatData> chatData = new ArrayList<>();
    private AbstractXMPPConnection connection;
    public static final String TAG = OneToOneFragment.class.getSimpleName();
    private EditText etMessage;
    private ImageView btnSend, btnCamera, btnFile;

    private StorageReference storageReference;



    public OneToOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one_to_one, container, false);

        FirebaseApp.initializeApp(getActivity().getApplicationContext());

        recyclerView = v.findViewById(R.id.rvOne);
        etMessage = v.findViewById(R.id.etSendMessage);
        btnSend = v.findViewById(R.id.btnSend);
        btnCamera = v.findViewById(R.id.btnCamera);
        btnFile = v.findViewById(R.id.btnFile);


        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        messageAdapter = new MessageAdapter(getContext(), chatData);
        recyclerView.setAdapter(messageAdapter);

        storageReference = FirebaseStorage.getInstance().getReference();

        //connection
        setConnection();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messagePesan = etMessage.getText().toString();
                if (messagePesan.length() > 0) {

                    sendMessage(messagePesan, RECIEVER + DOMAIN);
                }
            }
        });
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup().setButtonOrientation(LinearLayoutCompat.HORIZONTAL)).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        if (r.getError() == null) {
                            Bitmap bitmap =  r.getBitmap();

                            sendImage(bitmap,imageName(),RECIEVER+DOMAIN);
//              sendImage(RECIEVER+"@desktop-m97vqsb",bitmap,"percobaan");
                            ImageData imageData = new ImageData(SENDER, bitmap);
                            messageAdapter.addItem(imageData);
                            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
                        } else {
                            Toast.makeText(getContext(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }).show(getFragmentManager());
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
            if (requestCode==FILE_REQUEST){
                ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                Log.i("Bangsat",files.get(0).getPath());
                File file = new File(files.get(0).getPath());
                Log.i("Bangsat", String.valueOf(file.canRead()));

//                sendFile(RECIEVER+"@desktop-m97vqsb",file,file.getName());
                sendFile(file,file.getName(),RECIEVER+DOMAIN);
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



    private void sendMessage(String message, String bareJid) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(bareJid);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Chat chat = chatManager.chatWith(jid);
            Message newMessage = new Message();
            newMessage.setBody(message);
            try {
                chat.send(newMessage);
                MessageData data = new MessageData(SENDER, message);
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
    private void sendMessageFile(String message, String bareJid) {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(bareJid);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }

        if (connection != null) {
            ChatManager chatManager = ChatManager.getInstanceFor(connection);
            Chat chat = chatManager.chatWith(jid);
            Message newMessage = new Message();
            newMessage.setBody(message);
            try {
                chat.send(newMessage);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void receiveMessage(){
        ChatManager chatManager = ChatManager.getInstanceFor(connection);
        chatManager.addIncomingListener(new IncomingChatMessageListener() {
            @Override
            public void newIncomingMessage(EntityBareJid from, final Message message, final Chat chat) {
                Log.i(TAG, "New message from " + from + " : " + message.getBody());
                final String m = message.getBody();
                if (m.split("@@").length>1){
                    final Bitmap[] my_image = new Bitmap[1];
                    Log.i("coba","@@");
                    final File localFile;
                    try {
                        localFile = File.createTempFile("Images", "jpg");
                        Log.i("coba 1", String.valueOf(messageAdapter.getItemCount()));
                        storageReference.child("chatImage").child(m.split("@@")[0]+".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.i("coba", String.valueOf(taskSnapshot.getTotalByteCount()));
                                my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                final ImageData imageData = new ImageData(message.getFrom().toString().split("@")[0],my_image[0]);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageAdapter.addItem(imageData);
                                        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                                    }
                                });
                            }
                        });
                        Log.i("coba 3", String.valueOf(messageAdapter.getItemCount()));
                    } catch (IOException e) {
                        Log.i("coba",e.getMessage());
                    }
                }else if (m.split("##").length>1){
                    Log.i("coba","##");
                    try {
                        final File localFile = File.createTempFile("File",m.split("##")[0].split("\\.")[m.split("##")[0].split("\\.").length-1]);
                        storageReference.child("chatFile").child(m).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                final FileData fileData = new FileData(message.getFrom().toString().split("@")[0],localFile.getName(),localFile);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageAdapter.addItem(fileData);
                                        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                                    }
                                });
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.i("coba","lain");
                    final MessageData messageData = new MessageData(message.getFrom().toString().split("@")[0],m);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            messageAdapter.addItem(messageData);
                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                        }
                    });
                }
            }
        });
    }

    private void receiveOldMessage(String bareJid) {
        if (connection!=null){
            EntityBareJid jid = null;
            try {
                jid = JidCreate.entityBareFrom(bareJid);
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
                    final Message message = (Message) r.forwardedMessages.get(i).getForwardedStanza();
                    String m = message.getBody();
                    Log.i("mam", "message received" + message.getBody());
                    final MessageData data = new MessageData(message.getFrom().toString().split("@")[0], message.getBody());
                    if (m.split("@@").length>1){
                        final Bitmap[] my_image = new Bitmap[1];
                        Log.i("coba","@@");
                        final File localFile;
                        try {
                            localFile = File.createTempFile("Images", "jpg");
                            Log.i("coba 1", String.valueOf(messageAdapter.getItemCount()));
                            storageReference.child("chatImage").child(m.split("@@")[0]+".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Log.i("coba", String.valueOf(taskSnapshot.getTotalByteCount()));
                                    my_image[0] = BitmapFactory.decodeFile(localFile.getAbsolutePath());

                                    final ImageData imageData = new ImageData(message.getFrom().toString().split("@")[0],my_image[0]);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageAdapter.addItem(imageData);
                                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                                        }
                                    });
                                }
                            });
                            Log.i("coba 3", String.valueOf(messageAdapter.getItemCount()));
                        } catch (IOException e) {
                            Log.i("coba",e.getMessage());
                        }
                    }else if (m.split("##").length>1){
                        Log.i("coba","##");
                        try {
                            final File localFile = File.createTempFile("File",m.split("##")[0].split("\\.")[m.split("##")[0].split("\\.").length-1]);
                            Log.i("Bangsat",m.split("##")[0]);
                            storageReference.child("chatFile").child(m.split("##")[0]).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    final FileData fileData = new FileData(message.getFrom().toString().split("@")[0],localFile.getName(),localFile);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageAdapter.addItem(fileData);
                                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                                        }
                                    });
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Log.i("coba","lain");
                        final MessageData messageData = new MessageData(message.getFrom().toString().split("@")[0],m);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.addItem(messageData);
                                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                            }
                        });
                    }
                }

            }
        }
    }



    private void sendImage(Bitmap image, final String message, final String bareJid){
        StorageReference mountainImagesRef = storageReference.child("chatImage/" + message + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String from = SENDER+"_"+RECIEVER;
                sendMessageFile(message+"@@"+from,bareJid);
            }
        });
    }

    private void sendFile(File file, final String message, final String bareJid){
        StorageReference mountainImagesRef = storageReference.child("chatFile/" + message);
        byte[] data = new byte[0];
        try {
            data = FileUtils.readFileToByteArray(file);
            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    String from = SENDER+"_"+RECIEVER;
                    sendMessageFile(message+"##"+from,bareJid);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setConnection() {

        new Thread() {
            @Override
            public void run() {

                InetAddress addr = null;
                try {
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

                        receiveOldMessage(RECIEVER+DOMAIN);
                        receiveMessage();




//                        FileTransferManager manager = FileTransferManager.getInstanceFor(connection);
//                        manager.addFileTransferListener(new FileTransferListener() {
//                            @Override
//                            public void fileTransferRequest(final FileTransferRequest request) {
//                                Log.i("Namanya","masuk");
//                                final IncomingFileTransfer transfer = request.accept();
//
////                                final File file = new File(mf.getAbsoluteFile()+"/Chat/" + transfer.getFileName());
//
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                            File file = new File(request.getFileName());
//                                            FileData fileData = new FileData("coba",file.getName(),file);
//                                            messageAdapter.addItem(fileData);
//                                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
//
//
//                                    }
//                                });
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



//
//                            }
//                        });



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
            transfer.sendStream(new ByteArrayInputStream(FileUtils.readFileToByteArray(file)), filename, file.length(), "A greeting");


//            transfer.sendFile(file,"A greeting");

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
    private String imageName(){
        Date date = new Date();
        String s = date.getYear()+""+date.getMonth()+""+date.getDay()+"_"+date.getHours()+""+date.getMinutes()+""+date.getSeconds();
        return s;
    }


}
