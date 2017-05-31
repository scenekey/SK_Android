package com.scenekey.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.scenekey.R;
import com.scenekey.Utility.VolleyGetPost;
import com.scenekey.Utility.WebService;
import com.scenekey.activity.HomeActivity;
import com.scenekey.adapter.VenuAdapter;
import com.scenekey.helper.VenueComparator_Sort;
import com.scenekey.models.Venue;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by mindiii-rahul on 1/5/17.
 */

public class VenuSearch_Fragment extends Fragment implements View.OnClickListener {

    public static final String TAG = VenuSearch_Fragment.class.toString();
    RecyclerView recyvlerview_venu;
    GridLayoutManager layoutManager;
    ArrayList<Venue> venuelist;
    Add_Event_Fragmet add_event_fragmet;
    int page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fx_venu_fragment, null);
        recyvlerview_venu = (RecyclerView) view.findViewById(R.id.recyvlerview_venu);
        activity().setBBvisiblity(View.GONE);
        ImageView img_f1_back = (ImageView) view.findViewById(R.id.img_f1_back);
        img_f1_back.setOnClickListener(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutManager = new GridLayoutManager(getContext(), 1);
        venuelist = new ArrayList<>();
        final EditText edt_search_f2 = (EditText) view.findViewById(R.id.edt_search_f2);
        edt_search_f2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "Edittext " + start + " : " + before + " : " + count);
                if (count > 0 || start > 0) {
                    page = 0;
                    venuelist.clear();
                    getAllVenue(page, s + "", true);
                    page++;
                } else if (count == 0) {
                    page = 0;
                    venuelist.clear();
                    getAllVenue(page, "", true);
                    page++;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        final VenuAdapter adapter = new VenuAdapter((HomeActivity) getActivity(), venuelist, this);
        recyvlerview_venu.setLayoutManager(layoutManager);
        recyvlerview_venu.setAdapter(adapter);
        recyvlerview_venu.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int position = layoutManager.findLastVisibleItemPosition();
                //Log.e(TAG," : "+position);
                if (position >= 9) {
                    if (position == (venuelist.size() - 1)) {
                        if (edt_search_f2.getText().length() <= 0) {
                            getAllVenue(page, "", false);
                            page++;
                        } else {
                            getAllVenue(page, edt_search_f2.getText().toString(), false);
                            page++;
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        getAllVenue(page, "", true);
    }

    void getAllVenue(final int page, final String name, final boolean clear) {
        VolleyGetPost volleyGetPost = new VolleyGetPost(activity(), activity(), WebService.VENUE_SEARCH, false) {
            @Override
            public void onVolleyResponse(String response) {
                //Log.e(TAG,"volley response"+page+name+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (clear) venuelist.clear();
                    setVenueArray(jsonObject.getJSONArray("venue"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onVolleyError(VolleyError error) {
                Log.e(TAG, "volley error" + error);
            }

            @Override
            public void onNetError() {

            }

            @Override
            public Map<String, String> setParams(Map<String, String> params) {
                params.put("lat", activity().getlatlong()[0]);
                params.put("long", activity().getlatlong()[1]);
                params.put("page", page + "");
                params.put("name", name + "");
                Log.e(TAG, params.toString());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity().setBBvisiblity(View.VISIBLE);
        activity().setTitleVisibality(View.GONE);
    }

    HomeActivity activity() {
        return HomeActivity.instance;
    }

    synchronized void setVenueArray(JSONArray jsonArray) throws JSONException {

        for (int i = 0; i < jsonArray.length(); i++) {
            Venue venue = new Venue();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.has("name")) venue.setVenue_name(jsonObject.getString("name"));
            if (jsonObject.has("id")) venue.setVenue_id(jsonObject.getString("id"));
            if (jsonObject.has("venue_city")) venue.setCity(jsonObject.getString("venue_city"));
            if (jsonObject.has("venue_state")) venue.setState(jsonObject.getString("venue_state"));
            if (jsonObject.has("image")) venue.setImage(jsonObject.getString("image"));
            venuelist.add(venue);
        }
        Collections.sort(venuelist, new VenueComparator_Sort());
        recyvlerview_venu.getAdapter().notifyDataSetChanged();
    }

    public void setAdd_event_fragmet(Add_Event_Fragmet add_event_fragmet) {
        this.add_event_fragmet = add_event_fragmet;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_f1_back:
                activity().onBackPressed();
                break;
        }

    }

    public void onrecylcerViewItemClick(int position) {
        Log.e(TAG, page + " : " + venuelist.size());
        add_event_fragmet.onVenueSelect(venuelist.get(position));
        activity().onBackPressed();

    }
}
