package com.example.kidzeee;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView MainRecyclerview;
    private KidzeeeLinearManager LayoutManager;
    private ImageButton MainNextButton ;
    private List<KidzeeModel> KidzeeList;
    private int CurrentItem=0;
    private TextToSpeech MainTextToSpeech;
    private RecyclerView SingleItemCorrectRecyclerView,SingleItemJumbledRecyclerView;
    private CorrectAdapter correctAdapter;
    private Toast WrongToast;
    private View WrongView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        MainRecyclerview = findViewById(R.id.main_recyclerview);
        MainRecyclerview.setHasFixedSize(true);
        LayoutManager = new KidzeeeLinearManager(this,LinearLayoutManager.HORIZONTAL,false);
        MainRecyclerview.setLayoutManager(LayoutManager);
        MainNextButton = findViewById(R.id.main_next_button);

        KidzeeList = new ArrayList<>();


        fruitList();

        MainNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(CurrentItem < KidzeeList.size())
               {
                   CurrentItem++;
                   MainRecyclerview.scrollToPosition(CurrentItem);
               }
            }
        });

        MainTextToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS)
                {
                    int result = MainTextToSpeech.setLanguage(Locale.US);

                    if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(getApplicationContext(),"Language not supported",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"TextToSpeech initialization failed.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        KidzeeAdapter adapter = new KidzeeAdapter();
        MainRecyclerview.setAdapter(adapter);

        WrongToast = new Toast(this);
        WrongView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wrong_view_layout,null);
        WrongToast.setView(WrongView);
        WrongToast.setGravity(Gravity.CENTER,0,0);
    }

    private void fruitList() {
        KidzeeList.add(new KidzeeModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYP3GJ7QuLRvmuh-agl1sXHtMZ1VQwWKwxADnICJDaeiYPUqv_","MANGO"));
        KidzeeList.add(new KidzeeModel("https://5.imimg.com/data5/WA/US/MY-18632401/apple-fruit-500x500.jpg","APPLE"));
        KidzeeList.add(new KidzeeModel("https://images-na.ssl-images-amazon.com/images/I/81WJyO53YAL._SY550_.jpg","PINEAPPLE"));
        KidzeeList.add(new KidzeeModel("https://previews.123rf.com/images/utima/utima1202/utima120200025/12521354-wet-orange-fruit-with-leaves-isolated-on-white.jpg","ORANGE"));
        KidzeeList.add(new KidzeeModel("https://i5.walmartimages.ca/images/Large/463/3_r/6000191284633_R.jpg","WATERMELON"));
        KidzeeList.add(new KidzeeModel("https://ae.pricenacdn.com/files/images/products/original/933/Banana-Yellow-India-1kg-Approx-Weight_11074279_032b1d60acc6d81ec479d6a6dd68b825_t.jpg","BANANA"));
        KidzeeList.add(new KidzeeModel("https://cdn.medusajuice.co.uk/wp-content/uploads/2017/12/peach-e-liquid.png","PEACH"));
        KidzeeList.add(new KidzeeModel("http://www.zarat.kp.gov.pk/assets/uploads/crops/a2237670547780096024583f333bcefd.jpeg","CARROT"));
        KidzeeList.add(new KidzeeModel("https://balidirectstore" +
                ".com/app/uploads/2018/04/Fresh-lemons-on-the-rustic-tale-640x360.jpg","LEMON"));
        KidzeeList.add(new KidzeeModel("https://5.imimg.com/data5/RA/LA/MY-46372253/guava-2fperu" +
                "-500x500.png","GUAVA"));
    }

    public class KidzeeeLinearManager extends LinearLayoutManager
    {

        public KidzeeeLinearManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollHorizontally() {
            return false;
        }
    }

    public class KidzeeAdapter extends RecyclerView.Adapter<KidzeeAdapter.KidzeeViewHolder>
    {

        @NonNull
        @Override
        public KidzeeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new KidzeeAdapter.KidzeeViewHolder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_item_layout,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(@NonNull KidzeeViewHolder kidzeeViewHolder, int i) {

            Glide.with(getApplicationContext()).load(KidzeeList.get(i).getImageUri()).into(kidzeeViewHolder.SingleItemImageView);
            SingleItemJumbledRecyclerView.setAdapter(new SingleKidzeeAdapter(KidzeeList.get(i).getOrgName(),GenerateRandomString(KidzeeList.get(i).getOrgName())));
            correctAdapter= new CorrectAdapter(KidzeeList.get(i).getOrgName(),0);
            correctAdapter.notifyDataSetChanged();
            SingleItemCorrectRecyclerView.setAdapter(correctAdapter);

        }

        @Override
        public int getItemCount() {
            return KidzeeList.size();
        }


        public class KidzeeViewHolder extends RecyclerView.ViewHolder {

            ImageView SingleItemImageView;

            public KidzeeViewHolder(@NonNull View itemView) {
                super(itemView);

                SingleItemImageView = itemView.findViewById(R.id.single_item_imageview);

                SingleItemJumbledRecyclerView = itemView.findViewById(R.id.single_item_jumbled_reyclerview);
                SingleItemJumbledRecyclerView.setHasFixedSize(true);
                SingleItemJumbledRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

                SingleItemCorrectRecyclerView = itemView.findViewById(R.id.single_item_correct_reyclerview);
                SingleItemCorrectRecyclerView.setHasFixedSize(true);
                SingleItemCorrectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));


            }
        }
    }



    private String GenerateRandomString(String orgName) {

        int length = orgName.length();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        while(orgName.length()<10)
        {
            char c = chars.charAt(rnd.nextInt(chars.length()));

            if(!orgName.contains(String.valueOf(c)))
            {
                orgName=orgName+String.valueOf(c);
            }
        }


        return shuffle(orgName);
    }

    public String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }

        return output.toString();
    }

    public class SingleKidzeeAdapter extends RecyclerView.Adapter<SingleKidzeeAdapter.SingleKidzeeViewholder>
    {
        String OrgString;
        String RandomString;
        int count=0;
        public SingleKidzeeAdapter(String orgString, String o) {
            OrgString = orgString;
            RandomString= o;
        }

        @NonNull
        @Override
        public SingleKidzeeViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
            return new SingleKidzeeAdapter.SingleKidzeeViewholder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_item_layout_button,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(@NonNull final SingleKidzeeViewholder singleKidzeeViewholder, final int i) {

            final char ch = RandomString.charAt(i);
            singleKidzeeViewholder.SingleItem.setText(String.valueOf(ch));

            singleKidzeeViewholder.SingleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ch==(OrgString.charAt(count)))
                    {
                        if(WrongView.isShown())
                        {
                            WrongView.setVisibility(View.GONE);
                        }
                        count++;
                        singleKidzeeViewholder.SingleItem.setVisibility(View.GONE);
                        MainTextToSpeech.speak(String.valueOf(ch),TextToSpeech.QUEUE_ADD,null);
                        correctAdapter= new CorrectAdapter(OrgString,count);
                        correctAdapter.notifyDataSetChanged();
                        SingleItemCorrectRecyclerView.setAdapter(correctAdapter);


                    }
                    else
                    {
                        ShowCustomToast(0);
                        MainTextToSpeech.speak("Wrong",TextToSpeech.QUEUE_ADD,null);
                    }

                    if(count==OrgString.length())
                    {
                        MainTextToSpeech.speak(OrgString,TextToSpeech.QUEUE_ADD,null);
                        SingleItemJumbledRecyclerView.clearAnimation();
                        SingleItemJumbledRecyclerView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.fade_out));
                        SingleItemJumbledRecyclerView.getAnimation().start();
                        SingleItemJumbledRecyclerView.setVisibility(View.INVISIBLE);
                    }


                }
            });
        }


        @Override
        public int getItemCount() {
            return RandomString.length();
        }

        public class SingleKidzeeViewholder extends RecyclerView.ViewHolder {

            Button SingleItem;

            public SingleKidzeeViewholder(@NonNull View itemView) {
                super(itemView);

                SingleItem = itemView.findViewById(R.id.single_item_button);
            }
        }
    }

    private void ShowCustomToast(int i) {

        WrongView.setVisibility(View.VISIBLE);
        WrongToast.setDuration(i);
        WrongToast.show();
    }


    @Override
    protected void onDestroy() {

        if(MainTextToSpeech!=null)
        {
            MainTextToSpeech.stop();
            MainTextToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private class CorrectAdapter extends RecyclerView.Adapter<CorrectAdapter.CorrectViewholder> {

        String CorrectString;
        int correctlength;

        public CorrectAdapter(String correctString, int correctlength) {
            CorrectString = correctString;
            this.correctlength = correctlength;
        }

        @NonNull
        @Override
        public CorrectViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new CorrectAdapter.CorrectViewholder(LayoutInflater.from(getApplicationContext()).inflate(R.layout.single_item_layout_button,viewGroup,false));
        }

        @Override
        public void onBindViewHolder(@NonNull CorrectViewholder correctViewholder, int i) {

            if(correctlength>i)
            {
                correctViewholder.OrgButton.setText(String.valueOf(CorrectString.charAt(i)));
                correctViewholder.OrgButton.setBackgroundResource(R.drawable.circular_background_green);
            }
            else
            {
                correctViewholder.OrgButton.setText("_");
                correctViewholder.OrgButton.setBackgroundResource(R.drawable.circular_background_red);

            }

        }

        @Override
        public int getItemCount() {
            return CorrectString.length();
        }

        public class CorrectViewholder extends RecyclerView.ViewHolder {

            Button OrgButton;

            public CorrectViewholder(@NonNull View itemView) {
                super(itemView);

                OrgButton = itemView.findViewById(R.id.single_item_button);
            }
        }
    }
}
