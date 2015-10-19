package com.hat.dynamiclist;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by anting.hu on 2015/10/19.
 */
public class MyGridAdapter extends ArrayAdapter<Map<String, Object>> {

    private Context mContext;
    private ArrayList<Map<String, Object>> array;

    public MyGridAdapter(Context context, ArrayList<Map<String, Object>> data) {
        super(context, R.layout.list_item, data);
        mContext= context;
        array = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHodler hodler = null;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            hodler = new MyHodler();
            hodler.mTxtView = (TextView)convertView.findViewById(R.id.txtView);
            hodler.mImgView = (ImageView)convertView.findViewById(R.id.imgView);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (MyHodler)convertView.getTag();
        }

        hodler.mTxtView.setText(array.get(position).get("str").toString());
        hodler.mImgView.setImageResource(Integer.parseInt(array.get(position).get("img").toString()));

        return convertView;
    }

    //todo 为什么是静态内部类
    private static class MyHodler {
        TextView mTxtView;
        ImageView mImgView;
    }
}
