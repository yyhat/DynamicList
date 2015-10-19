package com.hat.dynamiclist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by anting.hu on 2015/10/19.
 */
public class MyGridView extends GridView
{
    private int mTouchSlop; //当前系统默认滑动距离

    private int mDragPointY; // 在点击点当前item控件中偏移
    private int mDragPointX;// 在点击点当前item控件中偏移

    private int mCoordOffsetY = -1; // gridView在屏幕中的偏移位置
    private int mCoordOffsetX = -1;// gridView在屏幕中的偏移位置

    private Rect mTempRect = new Rect();
    private DragShadowBuilder mDropListener;

    private PopView mPopView;

    private int mItemHeightHalf = 32;
    private int mItemWidthHalf = 32;// xiaochp
    private int mItemHeightNormal = 64;

    private int mDragPos; // which item is being dragged
    private int mFirstDragPos; // where was the dragged item originally
    private int mDragPointY; // at what offset inside the item did the user grab
    private int mHeight;

    public MyGridView(Context context) {
        this(context, null, 0);
    }

    public MyGridView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs,defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mPopView = new PopView(context);
    }

    //触屏 拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //得到点击在父控件中的位置
                int x = (int)ev.getX();
                int y = (int)ev.getY();

                //得到点击到item的索引
                int itemIndex = pointToPosition(x, y);
                if(itemIndex == AdapterView.INVALID_POSITION)
                    break;
                View item = getChildAt(itemIndex - getFirstVisiblePosition());

                mDragPointX = x - item.getLeft();
                mDragPointY = y - item.getTop();

                //只初始化一次即可
                if(mCoordOffsetX == -1)
                    mCoordOffsetX =((int)ev.getRawX()) - x;

                if(mCoordOffsetY == -1)
                    mCoordOffsetY =((int)ev.getRawY()) - y;

                Rect r = mTempRect;
                r.left = item.getLeft();
                r.right = item.getRight();
                r.top = item.getTop();
                r.bottom = item.getBottom();

                mItemHeightHalf = (r.bottom - r.top) / 2;
                mItemWidthHalf = (r.right - r.left) / 2;// xiaochp
                mItemHeightNormal = r.bottom - r.top;

                if(x > r.left && x < r.right) {
                    //todo
                    item.setDrawingCacheEnabled(true);

                    Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
                    mPopView.startDragging(bitmap, x - mDragPointX + mCoordOffsetX, y - mDragPointY + mCoordOffsetY);
                    mDragPos = itemIndex;
                    mFirstDragPos = mDragPos;
                    mHeight = getHeight();
                    return false;
                }
        return super.onInterceptTouchEvent(ev);
    }

    //触屏事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(mDragView != null)
        {
            switch (ev.getAction())
            {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    break;
            }
        }
        return super.onTouchEvent(ev);
    }



        public interface DropListener {
            void drop(int from, int to);
        }
}
