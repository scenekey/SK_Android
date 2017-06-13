package com.scenekey.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.fragments.Category_Tag_Fragment;
import com.scenekey.models.Category;

import java.util.ArrayList;

/**
 * Created by mindiii on 11/5/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> implements Filterable {
    public ArrayList<Category> list;
    Context context;
    Category_Tag_Fragment fragment;
    private ArrayList<Category> filteredList;
    private CustomFilter mFilter;

    public CategoryAdapter(ArrayList<Category> list, Context context, Category_Tag_Fragment fragment) {

        this.list = list;
        this.context = context;
        mFilter = new CustomFilter(CategoryAdapter.this);
        filteredList = new ArrayList<Category>();
        filteredList.addAll(list);
        this.fragment = fragment;


    }

    public void setAllAgain() {
        filteredList.addAll(list);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        Holder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rclv_categorytag, parent, false);
        holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final Category obj = filteredList.get(position);

        holder.txt_category.setText(obj.getCategory());
        if (obj.isSelected())
            holder.txt_category.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
        else holder.txt_category.setTextColor(fragment.getResources().getColor(R.color.black));
        if (position == filteredList.size() - 1) {
            holder.txt_border.setVisibility(View.GONE);
        } else {
            holder.txt_border.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!obj.isSelected()) {
                    holder.txt_category.setTextColor(fragment.getResources().getColor(R.color.colorPrimary));
                    fragment.addChipToView(obj);
                    fragment.addChipTomainEvent(obj);
                    obj.setSelected(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (filteredList.size() != 0) return filteredList.size();
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    /**
     * This is class is used for Filter the text
     */
    public class CustomFilter extends Filter {
        private CategoryAdapter mAdapter;

        private CustomFilter(CategoryAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }


        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredList.clear();
            final FilterResults results = new FilterResults();
            if (charSequence.length() == 0) {
                filteredList.addAll(list);
            } else {
                final String filterPattern = charSequence.toString().toLowerCase().trim();
                for (final Category mWords : list) {
                    if (mWords.getCategory().toLowerCase().startsWith(filterPattern)) {
                        filteredList.add(mWords);
                    }
                }
            }
        /**///System.out.println("Count Number " + filteredList.size());
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        /**///System.out.println("Count Number 2 " + ((ArrayList<FriendRequestBean>) filterResults.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }

    /***************************************************************************************************************/

    public class Holder extends RecyclerView.ViewHolder {

        TextView txt_category, txt_border;


        public Holder(View itemView) {
            super(itemView);
            txt_category = (TextView) itemView.findViewById(R.id.txt_category);
            txt_border = (TextView) itemView.findViewById(R.id.txt_border);

        }


    }

}
