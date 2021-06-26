package com.chinews.xdapp;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

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
    public Bottom() {
        // Required empty public constructor
    }

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
    //@Override
    //public View onCreateView(LayoutInflater inflater, ViewGroup container,
    //                         Bundle savedInstanceState) {
    //    // Inflate the layout for this fragment
    //    return inflater.inflate(R.layout.fragment_buttom, container, false);
    //}

    //@Override
    //public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
    //    super.onViewStateRestored(savedInstanceState);
    //}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView textView = requireView().findViewById(R.id.textView18);
        super.onViewCreated(view, savedInstanceState);
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

                textclock.setGravity(Gravity.TOP | Gravity.END);
                textView.setGravity(Gravity.TOP | Gravity.START);
                textView.setVisibility(View.VISIBLE);
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.TOP);
                    textView.setVisibility(View.INVISIBLE);
                    TextView textView = requireView().findViewById(R.id.textView18);
                    textView.setOnClickListener(v -> {
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
                textclock.setGravity(Gravity.BOTTOM | Gravity.END);
                textView.setGravity(Gravity.BOTTOM | Gravity.START);
                textView.setVisibility(View.VISIBLE);
                if (!getResources().getConfiguration().locale.getLanguage().startsWith("en")) {
                    today_gold_text();
                } else {
                    textclock.setGravity(Gravity.CENTER | Gravity.BOTTOM);
                    textView.setVisibility(View.INVISIBLE);
                    textView.setOnClickListener(v -> {
                    });
                }
            }

            private void today_gold_text() {
                int r;
                r = (int) (Math.random() * 6);
                //sound pool
                //noinspection deprecation
                SoundPool sPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
                int music0 = sPool.load(getContext(), R.raw.a, 1);
                int music1 = sPool.load(getContext(), R.raw.b, 1);
                int music2 = sPool.load(getContext(), R.raw.e, 1);
                int music3 = sPool.load(getContext(), R.raw.d, 1);
                switch (r) {
                    case 1:
                        textView.setText(R.string.s);
                        textView.setOnClickListener(v ->
                                sPool.play(music1, 1, 1, 0, 0, 1));
                        break;
                    case 2:
                        textView.setText(R.string.t);
                        textView.setOnClickListener(v ->
                                sPool.play(music3, 1, 1, 0, 0, 1));
                        break;
                    case 3:
                        textView.setText(R.string.u);
                        textView.setOnClickListener(v ->
                                sPool.play(music0, 1, 1, 0, 0, 1));
                        break;
                    case 4:
                        textView.setText(R.string.v);
                        textView.setOnClickListener(v -> {
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
                        textView.setText(R.string.aa);
                        textView.setOnClickListener(v ->
                                sPool.play(music2, 1, 1, 0, 0, 1));
                        break;
                    default:
                        textView.setText(R.string.ab);
                        textView.setOnClickListener(v -> {
                        });
                        break;
                }
            }
        });
    }

}