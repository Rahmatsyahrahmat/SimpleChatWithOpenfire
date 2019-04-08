package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.MessageAdapter;
import com.rahmatsyah.simlplechatwithopenfire.model.Message;
import com.rahmatsyah.simlplechatwithopenfire.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private EditText message;
    private ImageView send;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messages = new ArrayList<>();

    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_global, container, false);;

        message = rootView.findViewById(R.id.globalMessage);
        send = rootView.findViewById(R.id.globalSend);

        User renal = new User("Renal"),
                harun = new User("Harun"),
                adit = new User("Adit"),
                fani = new User("Fani"),
                fachril = new User("Fachril"),
                farhan = new User("Farhan"),
                fano = new User("Fano");

        Message message1 = new Message("Weh run lu dimane?",adit);
        Message message2 = new Message("Gua lagi di ruang baca dit",harun);
        Message message3 = new Message("Yaelah, ayo cl",adit);
        Message message4 = new Message("Nah ide bagus tuh",fani);
        Message message5 = new Message("Gua lagi kagak ada duit",fachril);
        Message message6 = new Message("Otw",fano);
        Message message7 = new Message("Bentar gua masih kelas, 5 menit lagi kelar",farhan);
        Message message8 = new Message("Yaudah gua otw",harun);
        Message message9 = new Message("Gapapa ril, sini ae, gua bayarin semuanya",renal);


        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        messages.add(message4);
        messages.add(message5);
        messages.add(message6);
        messages.add(message7);
        messages.add(message8);
        messages.add(message9);


        recyclerView = rootView.findViewById(R.id.globalRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(getContext(),messages);
        recyclerView.setAdapter(messageAdapter);

        send.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(message.getText().toString())){
            Toast.makeText(getContext(),"Input Message",Toast.LENGTH_LONG).show();
        }
        else {
            User rahmatsyah = new User("Rahmatsyah");
            Message message = new Message(this.message.getText().toString(),rahmatsyah);
            send(message);
            this.message.setText("");
        }
    }
    private void send(Message message){
        messageAdapter.addItem(message);
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
    }
}
