package com.scenekey.cus_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.EmojiAdapter_Demo;
import com.scenekey.helper.Constant;
import com.scenekey.model.NotificationData;
import com.scenekey.util.CircleTransform;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiTextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mindiii on 28/2/18.
 */

public abstract class ProfilePopUp_Demo extends Dialog implements View.OnClickListener, DialogInterface.OnShowListener, DialogInterface.OnDismissListener {

   private View pop_up_view;
   private RecyclerView rclv_emoji;

    private int maxNudes;
    private ArrayList<String> getList;
    public ArrayList<String> list;
    private LinearLayout lr_send_nudge ,lr_get_ndge;
   private ImageView iv_delete ,img_cross;
   private HomeActivity activity;
   private Context context;
   private LinearLayout lr_indicator;
    private ImageView iv_indicator;
   private ImageView one, two ,three ,four ,five ,zero ;
   private TextView txt_send ,tv_userName;
   private SharedPreferences preferences ;
   private String [] recent ;
  private ImageView lastSelected;

    private NotificationData data;
  private   int lastFillPosition;
  private   boolean isLastFilled;
   private ImageView profileImg;
   private static final int maxsize = 28 ; //Maximum size of recent grid view

    public ProfilePopUp_Demo(@NonNull Activity activity , int maxNudes , NotificationData nudge , int visibility) {
        super(activity, android.R.style.Theme_Translucent);
        this.activity= (HomeActivity) activity;
        this.context = activity;
        pop_up_view = LayoutInflater.from(context).inflate(R.layout.popup_nudge_notificaiton, null);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(pop_up_view);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        this.setCanceledOnTouchOutside(true);
        LinearLayout llMain =  pop_up_view.findViewById(R.id.llMain);
        rclv_emoji =  pop_up_view.findViewById(R.id.rclv_emoji);
        lr_send_nudge =  pop_up_view.findViewById(R.id.lr_send_nudge);
        lr_get_ndge =  pop_up_view.findViewById(R.id.lr_get_ndge);
        lr_indicator =  pop_up_view.findViewById(R.id.lr_indicator);
        iv_delete =  pop_up_view.findViewById(R.id.iv_delete);
        iv_indicator =  pop_up_view.findViewById(R.id.iv_indicator);
        img_cross =  pop_up_view.findViewById(R.id.img_cross);
        zero    =  pop_up_view.findViewById(R.id.zero);
        one     =  pop_up_view.findViewById(R.id.one);
        two     =  pop_up_view.findViewById(R.id. two);
        three   =  pop_up_view.findViewById(R.id.three);
        four    =  pop_up_view.findViewById(R.id.four);
        five    =  pop_up_view.findViewById(R.id.five);
        txt_send=  pop_up_view.findViewById(R.id.txt_send);
        tv_userName=  pop_up_view.findViewById(R.id.tv_userName);

        txt_send.setText("Nudge "+nudge.username);
        GridLayoutManager layoutManager = new GridLayoutManager(context,4, LinearLayoutManager.HORIZONTAL,false);

        rclv_emoji.setLayoutManager(layoutManager);
        rclv_emoji.setAdapter(new EmojiAdapter_Demo(context,this));
        this.maxNudes = maxNudes;
        list = new ArrayList<>();
        getList = new ArrayList<>();

        try {
            profileImg =  pop_up_view.findViewById(R.id.img_profile_pic2);
            Picasso.with(context).load(nudge.img).transform(new CircleTransform()).into(profileImg);
        }catch (Exception e){
            e.printStackTrace();
        }
        pop_up_view.findViewById(R.id.img_left).setVisibility(visibility);
        pop_up_view.findViewById(R.id.img_right).setVisibility(visibility);
        pop_up_view.findViewById(R.id.v_line).setVisibility(visibility);
        ((TextView)pop_up_view.findViewById(R.id.tv_nudge)).setText(visibility==View.VISIBLE?"Nudge Back":"Nudge");
        setClicks(llMain,iv_delete , img_cross ,one, two ,three ,four ,five ,zero ,txt_send,
                pop_up_view.findViewById(R.id.img_left),
                pop_up_view.findViewById(R.id.img_right));

        this.setOnShowListener(this);
        String [] ar = nudge.nudges.split(",");
        for(String s: ar){
            getList.add(StringEscapeUtils.unescapeJava(s));
        }
        setText();
        getRecentTask();
        this.setOnDismissListener(this);
        tv_userName.setText(nudge.username.split("\\s+")[0]);
        this.data = nudge;
        updateImageView(one);
        lr_get_ndge.setVisibility(visibility);

    }

    private void setClicks(View... views){
        for(View view :views) view.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        rclv_emoji.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstFull = ((GridLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                updateIndicator(firstFull>28?firstFull/28:0);
            }
        });
        updateIndicator();
    }

    public void setText(String text){
        if(list.size()< maxNudes){
            list.add(text);
            int i=0;
            for(String str : list){
                ((EmojiTextView)lr_send_nudge.getChildAt(i)).setText(str.trim());
                i++;
            }
        }
    }

    public void setText(){

        int i=0;
        for(String str : getList){
            ((EmojiTextView)lr_get_ndge.getChildAt(i)).setText(StringEscapeUtils.unescapeJava(str.trim()));
            Log.e("value" ,str);
            i++;
            if(i==4)return;
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.llMain:
                activity.hideStatusBar();
                this.dismiss();
                break;

            case R.id.iv_delete:
                delete();
                break;
            case R.id.img_cross:
                this.dismiss();
                break;
            case R.id.zero:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(-1);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.one:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(0);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.two:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(1);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.three:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(2);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.four:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(3);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.five:
                ((EmojiAdapter_Demo)rclv_emoji.getAdapter()).setList(4);
                rclv_emoji.smoothScrollToPosition(0);
                updateImageView((ImageView) v);
                break;
            case R.id.txt_send:
                onSendCLick((TextView) v,this,data);
                break;
            case R.id.img_right:
                onNextClick((ImageView) v,this);
                break;
            case R.id.img_left:
                onPrevClick((ImageView) v,this);
                break;
        }

    }

    private void delete(){
        if(list.size()>0){
            int position = list.size()-1;
            list.remove(position);
            ((EmojiTextView)lr_send_nudge.getChildAt(position)).setText("");

        }
    }

    public void updateIndicator(){
        updateIndicator(0);
    }

    private void updateIndicator(int position){
        int i =((EmojiAdapter_Demo)rclv_emoji.getAdapter()).getIndicatorCount((int) context.getResources().getDimension(R.dimen._240sdp), (int) context.getResources().getDimension(R.dimen._40sdp));
        lr_indicator.setWeightSum(i);
        int padding = (int) context.getResources().getDimension(R.dimen._3sdp);
        for (int j=0;j<i;j++){
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(iv_indicator.getLayoutParams());
            imageView.setImageResource(R.drawable.iv_indicator);
            imageView.setSelected(false);
            imageView.setPadding(padding,padding,padding,padding);
            if(position==j)imageView.setSelected(true);
            if(j==0)lr_indicator.removeAllViews();
            lr_indicator.addView(imageView);
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        updateIndicator();
    }

    public String []  getRecent(){
        preferences = context.getSharedPreferences(Constant.PREF_EMOJI,Context.MODE_PRIVATE);
        String s = preferences.getString(Constant.PREF_string,"[]");
        String ar [] = new String [maxsize];
        int i=0;
        for(String txt :s.substring(1,s.length()-1).split(",")){
            ar[i] = txt.trim();
            if(txt.trim().isEmpty() && !isLastFilled) {
                lastFillPosition = i;
                isLastFilled = true;
            }
            i++;
        }
        recent = ar;
        return ar;
    }

    public synchronized void setRecent(final String text){
        new AsyncTask<Void,Void ,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                preferences = context.getSharedPreferences(Constant.PREF_EMOJI,Context.MODE_PRIVATE);
                if(lastFillPosition <maxsize){
                    if(canAddtoList(text))
                    {
                        recent[lastFillPosition] = text.trim();
                        lastFillPosition++;
                    }
                }
                else {
                    if(canAddtoList(text))
                    {
                        recent[0]= text.trim();
                        lastFillPosition = 1;
                    }
                }
                {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.apply();
                    editor.putString(Constant.PREF_string, Arrays.toString(recent));
                    editor.commit();

                }

                return null;
            }
        }.execute();


    }

   private void getRecentTask(){
        new AsyncTask<Void ,Void ,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                recent = new String[maxsize];
                for(String s: recent)s="";
                getRecent();
                return null;
            }
        }.execute();
    }

    private boolean canAddtoList(String text){
        for(String s :recent){
            if(s!= null && s.equals(text)) return false;
        }
        return true;
    }

   private void updateImageView(ImageView selected){
        updateIndicator();
        setImage(selected,true);
        if(lastSelected != null)setImage(lastSelected,false);
        lastSelected = selected;
    }

   private void setImage(ImageView view ,boolean isactive){
        view.setBackgroundResource(isactive?  R.color.old_primary     :R.drawable.bg_table     );
        switch (view.getId()){
            case R.id.zero: view.setImageResource(isactive?  R.drawable.ic_zero_active     :R.drawable.ic_zero     );    break;
            case R.id.one:  view.setImageResource(isactive?  R.drawable.ic_one_active      :R.drawable.ic_one      );    break;
            case R.id.two:  view.setImageResource(isactive?  R.drawable.ic_two_active      :R.drawable.ic_two      );    break;
            case R.id.three:view.setImageResource(isactive?  R.drawable.ic_three_active    :R.drawable.ic_three    );    break;
            case R.id.four: view.setImageResource(isactive?  R.drawable.ic_four_car_active :R.drawable.ic_four_car );    break;
            case R.id.five: view.setImageResource(isactive?  R.drawable.ic_five_active     :R.drawable.ic_five     );    break;
        }
    }

    public abstract void onClickView(TextView textView , ProfilePopUp_Demo profilePopUp);
    public abstract void onSendCLick(TextView textView , ProfilePopUp_Demo profilePopUp , final NotificationData obj );
    public abstract void onPrevClick(ImageView textView , ProfilePopUp_Demo profilePopUp);
    public abstract void onNextClick(ImageView textView , ProfilePopUp_Demo profilePopUp);
    public abstract void onDismiss(ProfilePopUp_Demo profilePopUp);

    @Override
    public void onDismiss(DialogInterface dialog) {
        onDismiss(this);
    }

    protected void updateData(NotificationData nudge){
        getList.clear();
        this.data = nudge;
        String [] ar = nudge.nudges.split(",");
        Log.e("Data" , nudge.nudges);
        for(String s: ar){
            getList.add(StringEscapeUtils.unescapeJava(s));

        }
        setText();
        tv_userName.setText(nudge.username.split("\\s+")[0]);
        try {
            Picasso.with(context).load(data.img).transform(new CircleTransform()).into(profileImg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}