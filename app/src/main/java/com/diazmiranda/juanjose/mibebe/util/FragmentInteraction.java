package com.diazmiranda.juanjose.mibebe.util;

import android.content.Context;
import androidx.fragment.app.Fragment;

public class FragmentInteraction extends Fragment {

    protected OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
        void showFloatingButton(boolean show);
    }
}
