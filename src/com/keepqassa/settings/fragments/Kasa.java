package com.keepqassa.settings.fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.android.settings.R;

import java.util.Random;

public class Kasa extends Preference {

    public Kasa(Context context, AttributeSet attrs) {
        super(context, attrs);
	        setLayoutResource(context.getResources().
                getIdentifier("layout/keepqassa_banner", null, context.getPackageName()));

    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        final boolean selectable = false;
        final Context context = getContext();
        ImageView mQassaImg = holder.itemView.findViewById(context.getResources().
                getIdentifier("id/qassa_img", null, context.getPackageName()));
        Animation animShake = AnimationUtils.loadAnimation(context, R.anim.qassa_shake);

        holder.itemView.setFocusable(selectable);
        holder.itemView.setClickable(selectable);
        holder.setDividerAllowedAbove(false);
        holder.setDividerAllowedBelow(false);

        mQassaImg.setClickable(true);
        mQassaImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mQassaImg.startAnimation(animShake);
                String[] randomStrings = new String[]{"Seberapa besarpun masalah, yakinkan hatimu bahwa semua pasti baik baik saja, dgn begitu hatimu bisa tenang",
                    "Insinyur itu org org pintar, tapi mereka blm bisa menemukan alat utk mengukur tekanan mental",
                    "Hidup ini seperti perlombaan, kalau kalian tidak cepat, kalian akan kalah",
                    "Hati kita itu mudah takut. yg pnting bgaimna cara kita meyakinkan hati kita jika smua itu psti akan baik-baik saja",
                    "Give me some sunshine. Give me some rain. Give me another chance. I wanna grow up once again",
                    "Jgn belajar utk menjadi kaya saja. Tapi belajarlah menjadi ahli",
                    "Untuk apa mempublikasikan kelemahan orang didepan umum",
                    "Tingkatan hanya menciptakan perpecahan",
                    "Jgn mengejar kesukseskan. Jadilah insinyur hebat, dan kesuksesan akan menghampirimu.",
                    "Buat yang menjadi hobimu, menjadi pekerjaanmu, dengan begitu kau akan bekerja seperti bermain.",
                    "Air garam adalah penghantar listrik yang baik, kami mengetahuinya, dia mempraktekkannya",
                    "Kalau bisa disederhanakan, untuk apa dibuat bertele-tele dan rumit?",
                    "Orang yang belajar demi pengetahuan, bukan sekedar ijazah, adalah orang yang luar biasa",
                    "Tak ada yang mengingat orang yang kedua, yang diingat cuma orang pertama",
                    "Sangat mudah memberi nasehat, tapi sulit menjalaninya",
                    "Jadilah apa pun yang kau suka, apa pun menurut hatimu",
                    "Jangan belajar untuk mengejar keberhasilan, tapi cari yang terbaik",
                    "Jika terlintas pikiran bodoh di otakmu, pandang foto orang tuamu dan bayangkan apa yang akan terjadi pada senyum mereka jika kau mati."};
                Toast.makeText(context.getApplicationContext(), randomStrings[new Random().nextInt(randomStrings.length)], Toast.LENGTH_SHORT).show();
            }
        });
    }

}
