package com.hat.dynamiclist;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * Created by anting.hu on 2015/10/19.
 */
public class PopView {

    private WindowManager.LayoutParams mWindowParams; //拖动的Viwe参数
    private WindowManager mWindowManager;
    private ImageView mDragView; //拖动View
    private Bitmap mDragBitmap; //当前拖动时的Bitmap
    private Context mContext;

    public PopView(Context context)
    {
        mContext = context;
    }

    /**
     *
     * @param bm  bitmap
     * @param x  屏幕坐标 x - mDragPointX + mCoordOffsetX;
     * @param y 屏幕坐标 y - mDragPointY + mCoordOffsetY;
     */
    public void startDragging(Bitmap bm, int x, int y)
    {
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;

        mWindowParams.x = x;
        mWindowParams.y = y;

        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;

        //flags
        //format

        mDragBitmap = bm;
        mWindowParams.windowAnimations = 0;
        mDragView = new ImageView(mContext);
        mDragView.setImageBitmap(bm);

        mWindowManager =(WindowManager) mContext.getSystemService("window");
        mWindowManager.addView(mDragView,mWindowParams);
    }

    //停止拖动
    public void stopDragging()
    {
        if(mDragView != null)
        {
            mWindowManager.removeView(mDragView);
            mDragView.setImageBitmap(null);
            mDragView = null;
        }

        if(mDragBitmap != null)
        {
            mDragBitmap.recycle();
            mDragBitmap = null;
        }
    }

    /**
     *
     * @param x  屏幕坐标 x - mDragPointX + mCoordOffsetX;
     * @param y 屏幕坐标 y - mDragPointY + mCoordOffsetY;
     */
    public  void dragView(int x, int y)
    {
        mWindowParams.x = x;
        mWindowParams.y = y;
        mWindowManager.updateViewLayout(mDragView,mWindowParams);
    }
}
