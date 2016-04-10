package com.build2gether.fx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ArrayAdapter;
/**
 * Created by abhilashnair on 3/7/16.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ImageView;





import com.build2gether.fx.OOP.Inventory;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

/**
 * Created by abhilashnair on 3/3/16.
 */
public class InventoryArrayAdapter extends ArrayAdapter<Inventory> implements Filterable {

    private final Activity context;
    private static ArrayList<Inventory> inventory;

    public InventoryArrayAdapter(Activity context, ArrayList<Inventory> inventory) {

        super(context, R.layout.image_list_info_item, inventory);
        this.context = context;
        this.inventory = inventory;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.image_list_info_item, null, true);

        //Image
        ImageView ItemOrFavorImage = (ImageView) rowView.findViewById(R.id.ItemImage);
        //Title
        TextView ItemOrFavorTitle =  (TextView) rowView.findViewById(R.id.ItemTitle);
        //Description
        TextView ItemOrFavorDescription =  (TextView) rowView.findViewById(R.id.ItemDescription);


        //Month and Day
        TextView MonthAndDay = (TextView) rowView.findViewById(R.id.Month_day);

        //Year
        TextView Year = (TextView) rowView.findViewById(R.id.Year);


        //set image
        Picasso.with(context).load(inventory.get(position).getImage()).centerCrop().resize(560,1080).rotate(90).into(ItemOrFavorImage);
        ItemOrFavorTitle.setText(inventory.get(position).getTitle());

        //set item description
        String SetDescription = inventory.get(position).getDescription();
        if(inventory.get(position).getDescription().length() > 30) {
            String ellipses = "...";
            SetDescription = inventory.get(position).getDescription().substring(0, 31) + ellipses;
        }
        ItemOrFavorDescription.setText(SetDescription);

        //set date
        String full_date = inventory.get(position).getDate();
        String date = full_date.substring(0, full_date.indexOf(" ")).trim();
        MonthAndDay.setText(getMonth(date) + " " + getDay(date));
        Year.setText(getYear(date));






        return rowView;
        //add

    }


    public static ArrayList<Inventory> getInventory() {
        return inventory;
    }

    public static String getMonth(String date) {
        String Month =  date.substring(date.indexOf("-") + 1, date.lastIndexOf("-"));
        String monthString = "";
        switch (Month) {
            case "01":  monthString = "January";
                break;
            case "02":  monthString = "February";
                break;
            case "03":  monthString = "March";
                break;
            case "04":  monthString = "April";
                break;
            case "05":  monthString = "May";
                break;
            case "06":  monthString = "June";
                break;
            case "07":  monthString = "July";
                break;
            case "08":  monthString = "August";
                break;
            case "09":  monthString = "September";
                break;
            case "10": monthString = "October";
                break;
            case "11": monthString = "November";
                break;
            case "12": monthString = "December";
                break;
            default: monthString = "Invalid month";
                break;
        }
        return monthString;
    }

    public static String getDay(String date) {
        return date.substring(date.lastIndexOf("-") + 1, date.length());
    }

    public static String getYear(String date) {
        return date.substring(0, date.indexOf("-"));
    }


}