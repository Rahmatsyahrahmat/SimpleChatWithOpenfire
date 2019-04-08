package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahmatsyah.simlplechatwithopenfire.R;
import com.rahmatsyah.simlplechatwithopenfire.adapter.ChatAdapter;
import com.rahmatsyah.simlplechatwithopenfire.model.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OneToOneFragment extends Fragment {

    private View rootView;

    public OneToOneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_one_to_one, container, false);

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Renal"));
        users.add(new User("Fachril"));
        users.add(new User("Adit"));
        users.add(new User("Fani"));
        users.add(new User("Harun"));

        RecyclerView recyclerView = rootView.findViewById(R.id.oneToOneRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        ChatAdapter chatAdapter = new ChatAdapter(getContext(),users);
        recyclerView.setAdapter(chatAdapter);

        return rootView;
    }

}
