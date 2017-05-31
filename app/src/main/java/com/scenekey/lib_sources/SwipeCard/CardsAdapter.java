package com.scenekey.lib_sources.SwipeCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.CircleTransform;
import com.scenekey.Utility.Font;
import com.scenekey.Utility.RoundedTransformation;
import com.scenekey.Utility.WebService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by arjun on 4/25/16.
 */
public class CardsAdapter extends ArrayAdapter<Card> {
    private final ArrayList<Card> cards;
    private final LayoutInflater layoutInflater;
    Font font;

    public CardsAdapter(Context context, ArrayList<Card> cards) {
        super(context, -1);
        this.cards = cards;
        this.layoutInflater = LayoutInflater.from(context);
        font = new Font(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Card card = cards.get(position);
        final View view = layoutInflater.inflate(R.layout.z_cus_swipe_view_item, parent, false);
        // ImageView img= (ImageView) view.findViewById(R.id.img_user);
        if (card.imageUrl != null) {
            try {
                Picasso.with(getContext()).load(WebService.BASE_IMAGE_URL+card.imageUrl).into((ImageView) view.findViewById(R.id.card_image));
            } catch (Exception e) {

            }
        } else if (card.imageId != 0) {
            ((ImageView) view.findViewById(R.id.card_image)).setImageResource(card.imageId);
        } else {
            ((ImageView) view.findViewById(R.id.card_image)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.card_text)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.card_text)).setText(card.text);
            ((TextView) view.findViewById(R.id.card_text)).setTypeface(font.FrankHeavy());
        }
        if(card.userImage != null){
            try {
                Picasso.with(getContext()).load( card.userImage).transform(new CircleTransform()).into(((ImageView)view.findViewById(R.id.img_user)));
                ((TextView) view.findViewById(R.id.txt_time)).setText(getTimeInFormat(card.date));
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if(card.imageint != 0){
            Picasso.with(getContext()).load(card.imageint).transform(new CircleTransform()).into(((ImageView)view.findViewById(R.id.img_user)));
        }

        return view;
    }

    @Override
    public Card getItem(int position) {
        return cards.get(position);
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    String getTimeInFormat(String date) {
        String dateS = date.split(" ")[1];
        String dateArray[] = dateS.split(":");
        if (Integer.parseInt(dateArray[0]) > 12) {
            int hour = Integer.parseInt(dateArray[0]) - 12;
            return hour + ":" + dateArray[1] + " pm";
        }
        return dateArray[0] + ":" + dateArray[1] + " am ";
    }
}
