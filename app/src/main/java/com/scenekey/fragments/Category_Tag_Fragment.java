package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.scenekey.R;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.CategoryAdapter;
import com.scenekey.cus_view.ChipView;
import com.scenekey.cus_view.FlowLayout;
import com.scenekey.models.Category;

/**
 * Created by mindiii on 9/5/17.
 */

public class Category_Tag_Fragment extends Fragment {

    public static final String TAG = Category_Tag_Fragment.class.toString();

    RecyclerView recyclerviewcat;
    FlowLayout chip_linear;

    Add_Event_Fragmet add_event_fragmet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fx_category_tag_fragment, null);
        recyclerviewcat = (RecyclerView) view.findViewById(R.id.recyclerviewcat);
        chip_linear = (FlowLayout) view.findViewById(R.id.chip_linear);
        recyclerviewcat.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setChips();
        EditText edt_search_f2 = (EditText) view.findViewById(R.id.edt_search_f2);
        edt_search_f2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                recyclerviewcat.setVisibility(View.VISIBLE);
                setRecyclerviewcat();
                ((CategoryAdapter) recyclerviewcat.getAdapter()).getFilter().filter("");

                return false;
            }
        });
        edt_search_f2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                recyclerviewcat.setVisibility(View.VISIBLE);
                ((CategoryAdapter) recyclerviewcat.getAdapter()).getFilter().filter(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });
    }


    HomeActivity activity() {
        return HomeActivity.instance;
    }


    public void addChipToView(Category category) {
        ChipView chipView = new ChipView(activity(), category.getCategory_id()) {
            @Override
            public void setDeleteListner(ChipView chipView) {
                int i = 0;
                for (Category category : add_event_fragmet.getCategoryList()) {
                    if (category.getCategory_id().equals(chipView.getIdChip())) {
                        category.setSelected(false);
                        add_event_fragmet.getCategoryList().set(i, category);
                    }
                    i++;
                }
                recyclerviewcat.getAdapter().notifyDataSetChanged();
            }
        };
        chipView.setText(category.getCategory());
        category.setSelected(true);


        /***************** Chekcing and setting the main list ********************/
        int i = 0;
        for (Category category1 : add_event_fragmet.getCategoryList()) {
            if (category1.getCategory_id().equals(category.getCategory_id())) {
                add_event_fragmet.getCategoryList().set(i, category);
            }
            i++;
        }
        /**********************                               ******************/
        chip_linear.addView(chipView);
    }

    public void addChipTomainEvent(Category category) {
        add_event_fragmet.addChipToView(category);
    }


    public void setAdd_event_fragmet(Add_Event_Fragmet add_event_fragmet) {
        this.add_event_fragmet = add_event_fragmet;
    }

    void setRecyclerviewcat() {
        if (recyclerviewcat.getAdapter() == null) {
            CategoryAdapter adapter = new CategoryAdapter(add_event_fragmet.getCategoryList(), activity(), this);
            recyclerviewcat.setLayoutManager(new LinearLayoutManager(activity()));
            recyclerviewcat.setAdapter(adapter);
        }

    }

    void setChips() {
        for (Category category : add_event_fragmet.getCategoryList()) {
            if (category.isSelected()) {
                ChipView chipView = new ChipView(activity(), category.getCategory_id()) {
                    @Override
                    public void setDeleteListner(ChipView chipView) {
                        int i = 0;
                        for (Category category : add_event_fragmet.getCategoryList()) {
                            if (category.getCategory_id().equals(chipView.getIdChip())) {
                                category.setSelected(false);
                                Log.e("DeleteCalled", " " + category.getCategory_id());
                                add_event_fragmet.getCategoryList().set(i, category);
                                try {
                                    add_event_fragmet.removeChip(category.getCategory_id(), category.getCategory());
                                } catch (Exception e) {

                                }
                            }
                            i++;

                        }
                    }
                };
                chipView.setText(category.getCategory());

                chip_linear.addView(chipView);
            }
        }
    }
}
