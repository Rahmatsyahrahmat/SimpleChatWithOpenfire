package com.rahmatsyah.simlplechatwithopenfire.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rahmatsyah.simlplechatwithopenfire.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFragment extends Fragment implements View.OnClickListener {


    public GlobalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_global, container, false);
        ;

        return v;
    }

    @Override
    public void onClick(View v) {

    }

}
