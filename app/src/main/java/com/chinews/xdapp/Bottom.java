package com.chinews.xdapp;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Bottom#newInstance} factory method to
// * create an instance of this fragment.
// */
public class Bottom extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
//
    //public Bottom() {
    //    // Required empty public constructor
    //}

    ///**
    // * Use this factory method to create a new instance of
    // * this fragment using the provided parameters.
    // *
    // * @param param1 Parameter 1.
    // * @param param2 Parameter 2.
    // * @return A new instance of fragment Bottom.
    // */
    // TODO: Rename and change types and number of parameters
    //public static Bottom newInstance(String param1, String param2) {
    //    Bottom fragment = new Bottom();
    //    Bundle args = new Bundle();
    //    args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
    //    fragment.setArguments(args);
    //    return fragment;
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //  TODO: Rename and change types of parameters
        // String mParam1 = getArguments().getString(ARG_PARAM1);
        // String mParam2 = getArguments().getString(ARG_PARAM2);
        //}
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buttom, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        MobileAds.initialize(requireContext(), initializationStatus -> {
        });
        AdView mAdView = requireView().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                // textclock
                TextClock textclock = requireView().findViewById(R.id.textclock);
                assert textclock != null;
                textclock.setFormat24Hour("yyyy-MM-dd HH:mm:ss");
                textclock.setFormat12Hour("yyyy-MM-dd hh:mm:ssa");
                //random text
                TextView textView = getView().findViewById(R.id.textView18);
                textclock.setGravity(Gravity.TOP | Gravity.END);
                textView.setGravity(Gravity.TOP | Gravity.START);
                textView.setVisibility(View.VISIBLE);
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.TOP);
                    textView.setVisibility(View.INVISIBLE);
                    TextView textgold = requireView().findViewById(R.id.textView18);
                    textgold.setOnClickListener(v -> {
                    });
                }
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                // Code to be executed when an ad request fails.
                TextClock textclock = requireView().findViewById(R.id.textclock);
                textclock.setFormat24Hour("yyyy-MM-dd HH:mm:ss");
                textclock.setFormat12Hour("yyyy-MM-dd hh:mm:ssa");
                //random text
                TextView textView = getView().findViewById(R.id.textView18);
                textclock.setGravity(Gravity.BOTTOM | Gravity.END);
                textView.setGravity(Gravity.BOTTOM | Gravity.START);
                textView.setVisibility(View.VISIBLE);
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    textView.setVisibility(View.INVISIBLE);
                    TextView textgold = requireView().findViewById(R.id.textView18);
                    textgold.setOnClickListener(v -> {
                    });
                }
            }

            private void today_gold_text() {
                int r;
                r = (int) (Math.random() * 6);
                TextView textgold = requireView().findViewById(R.id.textView18);
                //sound pool
                //noinspection deprecation
                SoundPool sPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
                int music0 = sPool.load(getContext(), R.raw.a, 1);
                int music1 = sPool.load(getContext(), R.raw.b, 1);
                int music2 = sPool.load(getContext(), R.raw.e, 1);
                int music3 = sPool.load(getContext(), R.raw.d, 1);
                switch (r) {
                    case 1:
                        textgold.setText(R.string.s);
                        textgold.setOnClickListener(v ->
                                sPool.play(music1, 1, 1, 0, 0, 1));
                        break;
                    case 2:
                        textgold.setText(R.string.t);
                        textgold.setOnClickListener(v ->
                                sPool.play(music3, 1, 1, 0, 0, 1));
                        break;
                    case 3:
                        textgold.setText(R.string.u);
                        textgold.setOnClickListener(v ->
                                sPool.play(music0, 1, 1, 0, 0, 1));
                        break;
                    case 4:
                        textgold.setText(R.string.v);
                        textgold.setOnClickListener(v -> {
                            Toast.makeText(getContext(), R.string.w, Toast.LENGTH_SHORT).show();
                            new CountDownTimer(4000, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    Toast.makeText(getContext(), getString(R.string.x) + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
                                }

                                public void onFinish() {
                                    Toast.makeText(getContext(), R.string.y, Toast.LENGTH_SHORT).show();
                                }
                            }.start();
                            new CountDownTimer(10000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }

                                public void onFinish() {
                                    System.exit(1);
                                    throw new RuntimeException(getString(R.string.z));
                                }
                            }.start();
                        });
                        break;
                    case 5:
                        textgold.setText(R.string.aa);
                        textgold.setOnClickListener(v ->
                                sPool.play(music2, 1, 1, 0, 0, 1));
                        break;
                    default:
                        textgold.setText(R.string.ab);
                        textgold.setOnClickListener(v -> {
                        });
                        break;
                }
            }
        });
    }

}