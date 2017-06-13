package com.scenekey.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.CustomAlertDialog;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.cus_view.ChipView;
import com.scenekey.cus_view.CustomPicker;
import com.scenekey.cus_view.FlowLayout;
import com.scenekey.models.Category;
import com.scenekey.models.Venue;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created by mindiii on 4/5/17.
 */

public class Add_Event_Fragmet extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,View.OnKeyListener {
    public static final String TAG = Add_Event_Fragmet.class.toString();
    boolean clicked;
    TextView txt_venue ,txt_f1_title,txt_last;
    TextView txt_date, txt_time, txt_duration;
    String duration[];
    FlowLayout chip_grid;
    String venueId = "";
    ArrayList<Category> categoryList;
    int dd,mm,yy = -1,hh = -1,min;
    EditText edt_event_name,edt_event_discip;
    ImageView img_done ,cancel_button;
    ScrollView scrollView;
    UpdateEvent event ;

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.e("setNuickerTextColor", e + "");
                } catch (IllegalAccessException e) {
                    Log.e("setNumckerTextColor", e + "");
                } catch (IllegalArgumentException e) {
                    Log.e("setNuPickerTextColor", e + "");
                }
            }
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fx_add_event2, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(event!=null)HomeActivity.instance.setTitleVisibality(View.GONE);
        LinearLayout lnr_venue = (LinearLayout) view.findViewById(R.id.lnr_venue);
        LinearLayout lnr_date = (LinearLayout) view.findViewById(R.id.lnr_date);
        LinearLayout lnr_time = (LinearLayout) view.findViewById(R.id.lnr_time);
        LinearLayout lnr_duration = (LinearLayout) view.findViewById(R.id.lnr_duration);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        chip_grid = (FlowLayout) view.findViewById(R.id.chip_grid);
        RelativeLayout lnr_tag = (RelativeLayout) view.findViewById(R.id.lnr_tag);
        TextView txt_delete = (TextView) view.findViewById(R.id.txt_delete);
        TextView txt_update = (TextView) view.findViewById(R.id.txt_update);
        TextView txt_create = (TextView) view.findViewById(R.id.txt_create);
        txt_date = (TextView) view.findViewById(R.id.txt_date);
        txt_f1_title = (TextView) view.findViewById(R.id.txt_f1_title);
        txt_time = (TextView) view.findViewById(R.id.txt_time);
        txt_duration = (TextView) view.findViewById(R.id.txt_duration);
        txt_venue = (TextView) view.findViewById(R.id.txt_venue);
        txt_last = (TextView) view.findViewById(R.id.txt_last);
        edt_event_name = (EditText) view.findViewById(R.id.edt_event_name);
        edt_event_discip = (EditText) view.findViewById(R.id.edt_event_discip);
        edt_event_discip.setOnKeyListener(this);
        edt_event_name.setOnKeyListener(this);
        img_done = (ImageView) view.findViewById(R.id.img_done);
        cancel_button = (ImageView) view.findViewById(R.id.cancel_button);
        Font font = new Font(activity());
        font.setFontRailRegular(txt_f1_title,
                txt_venue,
                txt_duration,
                edt_event_name,
                edt_event_discip,
                txt_time,
                txt_date);
        duration = new String[]{"", "0.5 hours", "1.0 hours", "1.5 hours", "2.0 hours", "2.5 hours", "3.0 hours", "3.5 hours", "4.0 hours", "4.5 hours","5.0 hours", "5.5 hours","6.0 hours", "6.5 hours","7.0 hours", "7.5 hours","8.0 hours", "8.5 hours","9.0 hours", "9.5 hours","10 hours",""};
        setClick(lnr_venue,
                lnr_date,
                lnr_time,
                lnr_duration,
                img_done,
                cancel_button,
                txt_create,
                txt_delete,
                txt_update,
                lnr_tag);
        getAllTag();

        if(event!=null){
            RelativeLayout top_view = (RelativeLayout) view.findViewById(R.id.top_view);
            top_view.setVisibility(View.VISIBLE);
            txt_f1_title.setText(getString(R.string.editevent));
            edt_event_name.setText(event.event_name);
            venueId= event.venue_id;
            try {
                String date[] = event.date.split("-");
                yy= Integer.parseInt(date[0]);
                mm= Integer.parseInt(date[1])-1;
                dd= Integer.parseInt(date[2]);
                date = event.time.split(":");
                hh = Integer.parseInt(date[0]);
                min = Integer.parseInt(date[1]);
                txt_date.setText(new SimpleDateFormat("MMMM dd,yyyy").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event.date+ " " +event.time)));
                txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(event.date+ " " +event.time)));
            }catch (Exception e){
                yy=-1;
                hh=-1;
            }

            edt_event_discip.setText(event.description);
            edt_event_discip.setEnabled(false);
            txt_duration.setText(event.interval+" hours");
            txt_venue.setText(event.venueName);
            Log.e(TAG," Date "+yy+" " +mm+" "+dd+" "+hh+" "+min);
            txt_create.setVisibility(View.GONE);
        }
        activity().setTitle(getString(R.string.addevent));
        if(event == null){
            txt_delete.setVisibility(View.GONE);
            txt_update.setVisibility(View.GONE);
            LinearLayout lnr_del = (LinearLayout) view.findViewById(R.id.lnr_del);
            lnr_del.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        activity().hideKeyBoard();
        if(event != null){
            activity().setTitleVisibality(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(event != null)activity().setTitleVisibality(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnr_venue:
                if (!clicked) {
                    activity().addFragment(new VenuSearch_Fragment().setData(this, event != null), 1);
                    clicked = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked = false;
                        }
                    }, 500);
                }
                break;
            case R.id.lnr_tag:
                if (!clicked) {
                    Category_Tag_Fragment category_tagFragment = new Category_Tag_Fragment();
                    category_tagFragment.setAdd_event_fragmet(this);
                    activity().addFragment(category_tagFragment, 1);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked = false;
                        }
                    }, 500);
                }
                break;
            /*case R.id.label:
                if(!clicked){
                    Category_Tag_Fragment category_tagFragment = new Category_Tag_Fragment();
                    activity().addFragment(category_tagFragment,1);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            clicked = false;
                        }
                    },500);
                }
                break;*/
            case R.id.lnr_date:
                datePicker();
                break;
            case R.id.lnr_time:
                timePicker();
                break;
            case R.id.lnr_duration:
                durationPicker();
                break;
            case R.id.txt_create:
                if (checkInput()) {
                    makeScene();
                    /*if (event == null) makeScene();
                    else {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity()) {
                            @Override
                            public void leftButtonClick() {
                                updateScene();
                                this.dismiss();
                            }

                            @Override
                            public void rightButtonClick() {
                                this.dismiss();

                            }
                        };
                        customAlertDialog.setMessage(getString(R.string.confirmUpdate));
                        customAlertDialog.show();

                    }*/
                }
                break;
            case R.id.cancel_button:
                if(event == null)clearAll();
                else activity().onBackPressed();
                break;

            case R.id.txt_delete:
                CustomAlertDialog customAlertDialog2 = new CustomAlertDialog(activity()) {
                    @Override
                    public void leftButtonClick() {
                        deleteScene();
                        this.dismiss();
                    }

                    @Override
                    public void rightButtonClick() {
                        this.dismiss();

                    }
                };
                customAlertDialog2.setMessage(getString(R.string.confirmDelete));
                customAlertDialog2.show();
                break;
            case R.id.txt_update:
                if (checkInput())
                    {
                        CustomAlertDialog customAlertDialog = new CustomAlertDialog(activity()) {
                            @Override
                            public void leftButtonClick() {
                                updateScene();
                                this.dismiss();
                            }

                            @Override
                            public void rightButtonClick() {
                                this.dismiss();

                            }
                        };
                        customAlertDialog.setMessage(getString(R.string.confirmUpdate));
                        customAlertDialog.show();

                    }
                break;
        }

    }

    void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    void onVenueSelect(final Venue venue) {

        txt_venue.setText(venue.getVenue_name());
        venueId = venue.getVenue_id();


    }

    HomeActivity activity() {
        return HomeActivity.instance;
    }

    /*****************************
     * Date and Time Picker and set to text View
     ***********/
    public void datePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datepickerdialog = new DatePickerDialog(activity(),
                android.R.style.Theme_Holo_Light_Dialog, this, year, month, day);
        datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datepickerdialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        datepickerdialog.show();

    }

    public void timePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        boolean is24HourView = false;
        TimePickerDialog timePicker = new TimePickerDialog(activity(), android.R.style.Theme_Holo_Light_Dialog, this, hourOfDay, minute, is24HourView);
        timePicker.getWindow().setBackgroundDrawableResource(R.color.transparent);
        timePicker.show();

    }

    public void durationPicker() {

        final Dialog dialog = new Dialog(activity());
        dialog.setContentView(new CustomPicker(activity(), duration) {
            @Override
            public void cancelClickListner() {
                dialog.dismiss();
            }

            @Override
            public void okClickListner(String text) {
                txt_duration.setText((text.replace(getResources().getString(R.string.duration), "")).trim());
                dialog.dismiss();
            }


        });
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dd = dayOfMonth;
        mm = month ;
        yy = year;
        /*new SimpleDateFormat("MMMM dd,yyyy ").format(new Date(year,month,dayOfMonth));*/
        txt_date.setText((new SimpleDateFormat("MMMM dd , yyyy ").format(new Date(year - 1900, month, dayOfMonth))));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hh = hourOfDay;
        min = minute;
        txt_time.setText(new SimpleDateFormat("hh:mm aa").format(new Date(2017, 8, 15, hourOfDay, minute)));
    }

    /*************************************
     * The Blow Code is for Adding Tag or Category TAG
     ******************************************/


    public void addChipToView(Category category) {
        ChipView chipView = new ChipView(activity(), category.getCategory_id()) {
            @Override
            public void setDeleteListner(ChipView chipView) {
                int i = 0;
                for (Category category : categoryList) {
                    if (category.getCategory_id().equals(chipView.getIdChip())) {
                        category.setSelected(false);
                        Log.e("DeleteCalled 3", " " + category.getCategory_id());
                        categoryList.set(i, category);
                    }
                    i++;
                }
            }
        };
        chipView.setText(category.getCategory());

        chip_grid.addView(chipView);
    }

    void getAllTag() {
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.CATEGORY_TAG, false) {
            @Override
            public void onVolleyResponse(String response) {
                Log.e(TAG, response + "");
                try {
                    setTag(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {

            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        volleyGetPost.execute();
    }

    void setTag(String response) throws JSONException {
        if (categoryList == null) categoryList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(response);
        for (int i = 0; i < jsonArray.length(); i++) {
            Category category = new Category();
            JSONObject object = jsonArray.getJSONObject(i);
            if (object.has("category_id")) category.setCategory_id(object.getString("category_id"));
            if (object.has("category")) category.setCategory(object.getString("category"));
            categoryList.add(category);
        }

    }


    public ArrayList<Category> getCategoryList() {
        return categoryList;
    }

    public void removeChip(String id, String text) {
        if (chip_grid.getChildCount() > 0) {

            for (int i = 0; i <= chip_grid.getChildCount(); i++) {
                ChipView chipView = (ChipView) chip_grid.getChildAt(i);
                if (chipView.getIdChip().equals(id) && chipView.getTextView().getText().equals(text)) {
                    chip_grid.removeView(chipView);
                }
            }
        }

    }

    void makeScene(){
        activity().showProgDilog(false,TAG);
        VolleyGetPost makeScene = new VolleyGetPost(activity(),activity(),WebService.MAKE_SCENE,false) {
            @Override
            public void onVolleyResponse(String response) {
                activity().dismissProgDailog();
                Log.e(TAG,response.toString());
                // {"status":1,"message":"Scene Created"}
                try{JSONObject jsonObject = new JSONObject(response);
                if(jsonObject.has("status")){
                    if(jsonObject.getInt("status")==1) {
                        try {
                            showTost(jsonObject.getString("message"));
                        }catch (IllegalStateException e){

                        }
                        clearAll();
                        activity().onBackPressed();
                    }
                }}
                catch (Exception e){
                    try {
                        showTost(getString(R.string.somethingwentwrong));
                    }catch (IllegalStateException er){

                    }
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                try {
                    activity().dismissProgDailog();
                }catch (IllegalStateException e){

                }
                Log.e(TAG,error.toString());
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id",activity().userInfo().getUserID());
                params.put("venue_id",venueId);
                params.put("date",yy+"-"+(mm+1)+"-"+dd);
                params.put("time",hh+":"+min+":00");
                params.put("event_name",edt_event_name.getText().toString());
                params.put("interval",Double.valueOf((txt_duration.getText().toString().split(" "))[0])+"");
                params.put("description",edt_event_discip.getText().toString());
                Log.e(TAG,params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        makeScene.execute();
    }
    void updateScene(){
        activity().showProgDilog(false,TAG);
        VolleyGetPost makeScene = new VolleyGetPost(activity(),activity(),WebService.MAKE_SCENE_UPDATE,false) {
            @Override
            public void onVolleyResponse(String response) {
                activity().dismissProgDailog();
                Log.e(TAG,response.toString());
                // {"status":1,"message":"Scene Created"}
                try{JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getInt("status")==1) {
                           try {
                               showTost(jsonObject.getString("message"));
                           }catch (IllegalStateException s){

                           }
                            clearAll();
                            activity().onBackPressed();
                        }
                    }}
                catch (Exception e){
                   try {
                       showTost(getString(R.string.somethingwentwrong));
                   }catch (IllegalStateException er){

                   }
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
                Log.e(TAG,error.toString());
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("user_id",activity().userInfo().getUserID());
                params.put("venue_id",venueId);
                params.put("date",yy+"-"+(mm+1)+"-"+dd);
                params.put("time",hh+":"+min+":00");
                params.put("event_id",event.event_id);
                params.put("event_name",edt_event_name.getText().toString());
                params.put("interval",Double.valueOf((txt_duration.getText().toString().split(" "))[0])+"");
                Log.e(TAG,params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        makeScene.execute();
    }

    void deleteScene(){
        activity().showProgDilog(false,TAG);
        VolleyGetPost makeScene = new VolleyGetPost(activity(),activity(),WebService.DELETE_EVENT,false) {
            @Override
            public void onVolleyResponse(String response) {
                activity().dismissProgDailog();
                Log.e(TAG,response.toString());
                // {"status":1,"message":"Scene Created"}
                try{JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getInt("status")==1) {
                            showTost(jsonObject.getString("message"));
                            clearAll();
                            activity().backPressToposition();
                        }
                    }}
                catch (Exception e){
                    showTost(getString(R.string.somethingwentwrong));
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                activity().dismissProgDailog();
                Log.e(TAG,error.toString());
            }

            @Override
            public void onNetError() {
                activity().dismissProgDailog();

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("event_id",event.event_id);
                Log.e(TAG,params.toString());
                return params;
            }

            @NotNull
            @Override
            public Map<String, String> setHeaders(Map<String, String> params) {
                return params;
            }
        };
        makeScene.execute();
    }

    boolean checkInput(){
        boolean result = false;
        if(venueId.equals("")){
            showTost("Please select the venue");
            result= false;
        }
         else if(yy==-1){
            showTost("please select date of event");
             result= false;
         }
         else if(hh==-1){
            showTost("Please select the time");
             result= false;
        }

        else if(txt_duration.getText().toString().length()<=2){
            showTost("Please select duration");
             result= false;
        }
        else if(edt_event_name.getText().toString().length()<=0){
            showTost("Please provide event name");
            result= false;
        }
        else if(edt_event_discip.getText().toString().length()<=0){

            if(event == null) {
                result= false;
                showTost("Please add description");
            }
            else result = true;
        }
        else result = true;

         return result;
    }

    void showTost(String msg){
        try {
            activity().showToast(msg);
        }catch (IllegalStateException e){

        }catch(IllegalArgumentException e){

        }
    }

    void clearAll(){
        txt_venue.setText("");
        txt_date.setText("");
        txt_time.setText("");
        txt_duration.setText("");

        edt_event_discip.setText("");
        edt_event_name.setText("");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()){
            case R.id.edt_event_discip:
               // scrollView.smoothScrollTo(0,txt_last.getBottom());
                break;
            case R.id.edt_event_name:
               // scrollView.smoothScrollTo(0,txt_last.getTop());
                break;
        }
        return false;
    }

    public Add_Event_Fragmet setData(String venue_id, String date, String event_name, String interval, String event_id,String venueName,String description){
        this.event = new UpdateEvent( venue_id,  date,  event_name,  interval,  event_id,venueName ,description);
        return Add_Event_Fragmet.this;
    }

    public class UpdateEvent{
        String venue_id;     //:124273
        String date;         //:2017-04-12
        String time ;        //:02
        String event_name;   //:testingevent120417
        String interval;     //:5
        String event_id;     //:576407"
        String description;
        String venueName;

        public UpdateEvent(String venue_id, String date, String event_name, String interval, String event_id ,String venueName,String description) {
            this.venue_id = venue_id;
            Log.e(TAG, date);
            String dateA[] = date.replace("TO","T").replace("T"," ").split("\\s+");
            this.date = dateA[0];
            this.time = dateA[1];
            this.event_name = event_name;
            this.interval = interval;
            this.event_id = event_id;
            this.venueName = venueName;
            this.description = description;
        }
    }

}
