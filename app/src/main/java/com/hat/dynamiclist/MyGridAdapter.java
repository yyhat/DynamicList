package com.hat.dynamiclist;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;

/**
 * Created by anting.hu on 2015/10/19.
 */
public class MyGridAdapter extends BaseAdapter {
    int[] viewImage =
            {
                    R.drawable.hos_case, R.drawable.hos_chat, R.drawable.hos_eye, R.drawable.hos_eye_2,
                    R.drawable.hos_instro, R.drawable.hos_nav, R.drawable.hos_other, R.drawable.hos_pro,
                    R.drawable.hos_promotion
            };

    int[] viewStr =
            {
                    R.string.hos_case,
                    R.string.hos_chat,
                    R.string.hos_subject,
                    R.string.hos_nav,
                    R.string.hos_yuyue,
                    R.string.hos_chat,
                    R.string.hos_shuang,
                    R.string.hos_kaiyanjiao,
                    R.string.hos_case
            };

    private Context mContext;

    @Override
    public int getCount() {
        return viewImage.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public MyGridAdapter(Context context) {
        super();
        mContext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHodler hodler = null;
        if(convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item, null);
            hodler = new MyHodler();
            hodler.mTxtView = (TextView)convertView.findViewById(R.id.txtView);
            hodler.mImgView = (ImageView)convertView.findViewById(R.id.imgView);
            convertView.setTag(hodler);
        }
        else
        {
            hodler = (MyHodler)convertView.getTag();
        }

        hodler.mTxtView.setText(viewStr[position]);
        hodler.mImgView.setImageDrawable(mContext.getResources().getDrawable(viewImage[position]));

        return convertView;
    }

    //todo 为什么是静态内部类
    private static class MyHodler {
        TextView mTxtView;
        ImageView mImgView;
    }
}
