package com.scenekey.cus_view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scenekey.R;

/**
 * Created by mindiii on 10/5/17.
 */

public abstract class CustomPicker extends RelativeLayout {
    /*  Context context;
      public CustomPicker(Context context) {
          super(context);
          this.context = context;
          initiateview();
      }

      void initiateview(){
          View view = inflate(context, R.layout.z_cus_picker,this);
          NumberPicker np = (NumberPicker) view.findViewById(R.id.number);
         String [] duration = new String[]{"0.5 hours","1.0 hours","2.0 hours","3.0 hours"};
          np.setDisplayedValues(duration);
          np.setMaxValue(duration.length-1);
          np.setMinValue(0);
          np.setValue(duration.length/2);
          Log.e("TAG"," Number Picker Child Count"+np.getChildCount());

          np.setBackgroundResource(R.color.white);
          np.setWrapSelectorWheel(false);
      }*/
    Context context;
    RecyclerView recycler_view;
    String[] list;
    int positionM;
    int positionM2;
    TextView txt_cancel;
    TextView txt_ok;
    TextView txt_duration;
    int visibleItemCount;
    int totalItemCount;
    int pastVisiblesItems;
    boolean loading;

    public CustomPicker(Context context, String[] list) {
        super(context);
        this.context = context;
        this.list = list;
        initiateview();
    }

    public CustomPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    void initiateview() {
        View view = inflate(context, R.layout.z_cus_picker, this);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        txt_duration = (TextView) view.findViewById(R.id.txt_duration);
        txt_ok = (TextView) view.findViewById(R.id.txt_ok);
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelClickListner();
            }
        });
        txt_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                okClickListner(txt_duration.getText().toString());
            }
        });


        final RecyclerView.Adapter adapter = new Adapter();
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        positionM = 1;
        recycler_view.setLayoutManager(layoutManager);
        recycler_view.setAdapter(adapter);
        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int firstFull = layoutManager.findFirstCompletelyVisibleItemPosition();
                int first = layoutManager.findFirstVisibleItemPosition();
                int lastFull = layoutManager.findFirstCompletelyVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();
                if ((last - first) >= 3) {
                    positionM = firstFull;
                    positionM2 = positionM + 1;
                    adapter.notifyDataSetChanged();
                } else if (lastFull - firstFull == 0) {
                    if (firstFull != 0) {
                        positionM = firstFull;
                        positionM2 = 0;
                    } else {
                        positionM = 1;
                        positionM2 = 0;
                    }
                }


                adapter.notifyDataSetChanged();


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstFull = layoutManager.findFirstCompletelyVisibleItemPosition();
                int first = layoutManager.findFirstVisibleItemPosition();
                int lastFull = layoutManager.findFirstCompletelyVisibleItemPosition();
                int last = layoutManager.findLastVisibleItemPosition();
                if ((last - first) >= 3) {
                    positionM = firstFull;
                    positionM2 = positionM + 1;
                    adapter.notifyDataSetChanged();
                } else if (lastFull - firstFull == 0) {
                    if (firstFull != 0) {
                        positionM = firstFull;
                        positionM2 = 0;
                    } else {
                        positionM = 1;
                        positionM2 = 0;
                    }
                }
                /*if((last - first)>2){
                    positionM = firstFull;
                    adapter.notifyDataSetChanged();
                }
                else if(lastFull-firstFull==0){
                    if(firstFull != 0)positionM =firstFull;
                }
                else if(layoutManager.findFirstCompletelyVisibleItemPosition()!=0){ positionM = layoutManager.findFirstCompletelyVisibleItemPosition();
                    adapter.notifyDataSetChanged();}
                else {
                    positionM=1;
                    adapter.notifyDataSetChanged();
                }*/
                adapter.notifyDataSetChanged();
                Log.e("Positions", firstFull + " : " + lastFull + " : " + first + " : " + last + " M " + positionM);
            }
        });


    }

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    abstract public void cancelClickListner();

    abstract public void okClickListner(String text);

    public class Adapter extends RecyclerView.Adapter<Adapter.Holder> {


        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.z_cus_picker_view, null));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.txt.setText(list[position]);
            if (positionM == position || positionM2 == position) {
                holder.txt.setTextColor(getResources().getColor(R.color.black));
                holder.txt.setTextScaleX(1.2f);
                if (positionM == position)
                    txt_duration.setText(getResources().getString(R.string.duration) + " " + holder.txt.getText());
            } else {
                holder.txt.setTextColor(getResources().getColor(R.color.picker_Dim));
                holder.txt.setTextScaleX(1f);

            }
        }


        @Override
        public int getItemCount() {
            if (list != null) return list.length;
            return 0;
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView txt;

            public Holder(View itemView) {
                super(itemView);
                txt = (TextView) itemView.findViewById(R.id.txt);

            }
        }
    }


}
