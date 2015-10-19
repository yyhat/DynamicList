package com.hat.dynamiclist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {


    private MyGridView mGridView;
    private MyGridAdapter mGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitGridView();
    }

    void InitGridView()
    {
        mGridView = (MyGridView)findViewById(R.id.gridView);
        mGridAdapter = new MyGridAdapter(MainActivity.this, getData());
        mGridView.setAdapter(mGridAdapter);
        mGridView.setDropListener(onDrop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private MyGridView.DropListener onDrop = new MyGridView.DropListener() {
        public void drop(int from, int to) {
            Map item = mGridAdapter.getItem(from);

            mGridAdapter.remove(item);
            mGridAdapter.insert(item, to);
        }
    };



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

    private ArrayList<Map<String, Object>> getData()
    {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0; i < viewImage.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", viewImage[i]);
            map.put("str", getString(viewStr[i]));
            list.add(map);
        }
        return list;
    }
}
