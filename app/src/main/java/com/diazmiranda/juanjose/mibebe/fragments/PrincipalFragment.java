package com.diazmiranda.juanjose.mibebe.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.diazmiranda.juanjose.mibebe.R;
import com.diazmiranda.juanjose.mibebe.util.Constants;

public class PrincipalFragment extends Fragment implements View.OnClickListener {

    //private PrincipalViewModel mViewModel;

    public static PrincipalFragment newInstance() {
        return new PrincipalFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_principal, container, false);
        startComponents(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        //mViewModel = ViewModelProviders.of( this ).get( PrincipalViewModel.class );
        // TODO: Use the ViewModel
    }

    public void startComponents(View root) {
        ImageView facebook = root.findViewById(R.id.btn_facebook);
        ImageView twitter = root.findViewById(R.id.btn_twitter);
        ImageView youtube = root.findViewById(R.id.btn_youtube);

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        youtube.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch(view.getId()) {
            case R.id.btn_facebook:
                try {
                    String facebookUrl = "";
                    long versionCode = getActivity().getPackageManager().getPackageInfo("com.facebook.katana", 0).getLongVersionCode();
                    facebookUrl = (versionCode >= 3002850 ? "fb://facewebmodal/f?href=https://www.facebook.com/" : "fb://page/") + Constants.FACEBOOK_URL;

                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
                } catch(Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + Constants.FACEBOOK_URL ));
                }
                break;
            case R.id.btn_twitter:
                try {
                    getActivity().getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + Constants.TWITTER_URL));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + Constants.TWITTER_URL));
                }
                break;
            case R.id.btn_youtube:
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Constants.YOUTUBE_URL));
                try {
                    intent.setPackage("com.google.android.youtube");
                } catch (Exception e) {
                }
                break;
        }
        startActivity(intent);
    }
}
