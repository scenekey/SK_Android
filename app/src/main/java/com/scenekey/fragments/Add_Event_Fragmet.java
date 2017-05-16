package com.scenekey.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.scenekey.R;
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

public class Add_Event_Fragmet extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    public static final String TAG = Add_Event_Fragmet.class.toString();
    boolean clicked;
    TextView txt_venue;
    TextView txt_date, txt_time, txt_duration;
    String duration[];
    FlowLayout chip_grid;
    ArrayList<Category> categoryList;

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
        View view = inflater.inflate(R.layout.fx_add_event, null);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HomeActivity.instance.setTitleVisibality(View.GONE);
        LinearLayout lnr_venue = (LinearLayout) view.findViewById(R.id.lnr_venue);
        LinearLayout lnr_date = (LinearLayout) view.findViewById(R.id.lnr_date);
        LinearLayout lnr_time = (LinearLayout) view.findViewById(R.id.lnr_time);
        LinearLayout lnr_duration = (LinearLayout) view.findViewById(R.id.lnr_duration);
        chip_grid = (FlowLayout) view.findViewById(R.id.chip_grid);
        RelativeLayout lnr_tag = (RelativeLayout) view.findViewById(R.id.lnr_tag);
        txt_date = (TextView) view.findViewById(R.id.txt_date);
        txt_time = (TextView) view.findViewById(R.id.txt_time);
        txt_duration = (TextView) view.findViewById(R.id.txt_duration);
        txt_venue = (TextView) view.findViewById(R.id.txt_venue);
        duration = new String[]{"", "0.5 hours", "1.0 hours", "1.5 hours", "2.0 hours", "2.5 hours", "3.0 hours", "3.5 hours", ""};
        setClick(lnr_venue,
                lnr_date,
                lnr_time,
                lnr_duration,
                lnr_tag);
        getAllTag();

    }

    @Override
    public void onStart() {
        super.onStart();
        activity().hideKeyBoard();
        activity().setTitleVisibality(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        activity().setTitleVisibality(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lnr_venue:
                if (!clicked) {
                    VenuSearch_Fragment venuSearch_fragment = new VenuSearch_Fragment();
                    activity().addFragment(venuSearch_fragment, 1);
                    venuSearch_fragment.setAdd_event_fragmet(this);
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
        }

    }

    void setClick(View... views) {
        for (View view : views) {
            view.setOnClickListener(this);
        }
    }

    void onVenueSelect(final Venue venue) {

        txt_venue.setText(venue.getVenue_name());


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
        /*new SimpleDateFormat("MMMM dd,yyyy ").format(new Date(year,month,dayOfMonth));*/
        txt_date.setText((new SimpleDateFormat("MMMM dd , yyyy ").format(new Date(year - 1900, month, dayOfMonth))));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        /*new SimpleDateFormat("MMMM dd,yyyy hh:mm aa").format(new Date(year,month,dayOfMonth));*/
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
}
