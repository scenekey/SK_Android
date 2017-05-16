package com.scenekey.lib_sources.SwipeCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scenekey.R;
import com.scenekey.Utility.Font;
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
        Card card = cards.get(position);
        View view = layoutInflater.inflate(R.layout.z_cus_swipe_view_item, parent, false);
        // ImageView img= (ImageView) view.findViewById(R.id.img_user);
        if (card.imageUrl != null) {
            try {
                Picasso.with(getContext()).load("http://mindiii.com/scenekeyNew/scenekey/" + card.imageUrl).into((ImageView) view.findViewById(R.id.card_image));
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
        ((TextView) view.findViewById(R.id.helloText)).setText(card.name);

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
}
