package com.hat.dynamiclist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
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
public class MyGridView extends GridView {
    private int mTouchSlop; //当前系统默认滑动距离

    private int mDragPointY; // 在点击点当前item控件中偏移
    private int mDragPointX;// 在点击点当前item控件中偏移

    private int mCoordOffsetY = -1; // gridView在屏幕中的偏移位置
    private int mCoordOffsetX = -1;// gridView在屏幕中的偏移位置

    private Rect mTempRect = new Rect();
    private DropListener mDropListener;

    private PopView mPopView;

    private int mItemHeightHalf = 32;
    private int mItemWidthHalf = 32;// xiaochp
    private int mItemHeightNormal = 64;

    private int mDragPos; // which item is being dragged
    private int mFirstDragPos; // where was the dragged item originally
    private int mHeight;
//    private ListMoveHandler mListMoveHandler;
    private int mTempY; //在move过程中临时保存位置
    private int mTempX; //在move过程中临时保存位置
    private View mDragItem; //拖动的时候，隐藏当前位置 item


    public MyGridView(Context context) {
        this(context, null, 0);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mPopView = new PopView(context);
//        mListMoveHandler = new ListMoveHandler();
        Log.i("test", "mTouchSlop: " + mTouchSlop);
    }

    //触屏事件
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mDropListener != null && mPopView.mDragView != null) {
            int action = ev.getAction();
            switch (ev.getAction()) {
                //拖动结束
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    Rect r = mTempRect;
                    mPopView.mDragView.getDrawingRect(r);
                    mPopView.stopDragging();

                    if (mDropListener != null && mDragPos >= 0 && mDragPos < getCount()) {
                        mDropListener.drop(mFirstDragPos, mDragPos);
                    }
//
//                    if (mListMoveHandler.mIsStart) {
//                        mListMoveHandler.stop();
//                    }

                    unExpandViews(false);
                    break;

                case MotionEvent.ACTION_MOVE:
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();

                    mTempX = x;
                    mTempY = y;
//
//                    if (y - mDragPointY < 0) //超出屏幕最上面
//                    {
//                        if (!mListMoveHandler.mIsStart) {
//                            mListMoveHandler.start(false);
//                        } else if (mListMoveHandler.mIsUp) {
//                            mListMoveHandler.stop();
//                            mListMoveHandler.start(false);
//                        }
//                    } else if (y - mDragPointY + mItemHeightNormal + mCoordOffsetY > 480) //应该读取真实高度
//                    {
//                        if (!mListMoveHandler.mIsStart) {
//                            mListMoveHandler.start(true);
//                        } else if (mListMoveHandler.mIsUp) {
//                            mListMoveHandler.stop();
//                            mListMoveHandler.start(true);
//                        }
//                    } else {
//                        if (mListMoveHandler.mIsStart)
//                            mListMoveHandler.stop();
//                    }

                    mPopView.dragView( x - mDragPointX + mCoordOffsetX, y - mDragPointY + mCoordOffsetY);
                    int itemNum = getItemForPosition(x, y);
                    if (itemNum > 0)
                    {
                        if (itemNum != mDragPos)
                        {
                            Log.i("test", "itemNum=" + itemNum + ", mDragPos=" + mDragPos);
                            mDragPos = itemNum;
                            doExpansion();
                        }
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }


    //触屏 拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //得到点击在父控件中的位置
                int x = (int) ev.getX();
                int y = (int) ev.getY();

                //得到点击到item的索引
                int itemIndex = pointToPosition(x, y);
                if (itemIndex == AdapterView.INVALID_POSITION)
                    break;

                //必须要-getFirstVisiblePosition， 因为gridView和ListView会回收View
                View item = getChildAt(itemIndex - getFirstVisiblePosition());
                mDragItem = item;

                mDragPointX = x - item.getLeft();
                mDragPointY = y - item.getTop();

                //只初始化一次即可
                if (mCoordOffsetX == -1)
                    mCoordOffsetX = ((int) ev.getRawX()) - x;

                if (mCoordOffsetY == -1)
                    mCoordOffsetY = ((int) ev.getRawY()) - y;

                //得到Item大小
                Rect r = mTempRect;
                r.left = item.getLeft();
                r.right = item.getRight();
                r.top = item.getTop();
                r.bottom = item.getBottom();

                mItemHeightHalf = (r.bottom - r.top) / 2;
                mItemWidthHalf = (r.right - r.left) / 2;// xiaochp
                mItemHeightNormal = r.bottom - r.top;

                Log.i("test","onInterceptTouchEvent itemIndex=" + itemIndex );
                // 点击在Item控件范围内
                if (x > r.left && x < r.right) {
                    //提高响应速度
                    item.setDrawingCacheEnabled(true);

                    Bitmap bitmap = Bitmap.createBitmap(item.getDrawingCache());
                    mPopView.startDragging(bitmap, x - mDragPointX + mCoordOffsetX, y - mDragPointY + mCoordOffsetY);
                    mDragPos = itemIndex;
                    mFirstDragPos = mDragPos;
                    mHeight = getHeight();

                    return false;
                }
                mPopView.mDragView = null;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }


    // 需要调整以适应新的拖动算法
    private int getItemForPosition(int x, int y) {
        int adjustedy = y - mDragPointY + mItemHeightHalf; // 中心点
        int adjustedx = x - mDragPointX + mItemWidthHalf; // 中心点
        int pos = myPointToPosition(adjustedx, adjustedy);
        return pos;
    }

    /*
	 * Restore size and visibility for all listitems
	 */
    private void unExpandViews(boolean deletion) {
        for (int i = 0; ; i++) {
            View v = getChildAt(i);
            if (v == null) {
                if (deletion) {
                    // HACK force update of mItemCount
                    int position = getFirstVisiblePosition();
                    int y = getChildAt(0).getTop();
                    setAdapter(getAdapter());
                    // xiaochp setSelectionFromTop(position, y);
                    // end hack
                }
                layoutChildren(); // force children to be recreated where needed
                v = getChildAt(i);
                if (v == null) {
                    break;
                }
            }
            ViewGroup.LayoutParams params = v.getLayoutParams();
            params.height = mItemHeightNormal;
            v.setLayoutParams(params);
            v.setVisibility(View.VISIBLE);
        }
    }




    /*
	 * Adjust visibility and size to make it appear as though an item is being
	 * dragged around and other items are making room for it: If dropping the
	 * item would result in it still being in the same place, then make the
	 * dragged listitem's size normal, but make the item invisible. Otherwise,
	 * if the dragged listitem is still on screen, make it as small as possible
	 * and expand the item below the insert point. If the dragged item is not on
	 * screen, only expand the item below the current insertpoint.
	 */
    private void doExpansion()
    {
        View first = getChildAt(mFirstDragPos - getFirstVisiblePosition());
        Log.v("vv.equals(mDragItem>>>", ">>first=" + first);
        for (int i = 0; ; i++) {
            View vv = getChildAt(i);
            if (vv == null) {
                Log.v("vv.equals(mDragItem>>>", "break>>i=" + i);
                break;
            }
            int height = mItemHeightNormal;
            int visibility = View.VISIBLE;

            if (vv.equals(mDragItem) && first != null) {
                visibility = View.INVISIBLE;
            }
            ViewGroup.LayoutParams params = vv.getLayoutParams();
            params.height = height;
            vv.setLayoutParams(params);
            vv.setVisibility(visibility);
        }
    }

    /*
    * pointToPosition() doesn't consider invisible views, but we need to, so
    * implement a slightly different version.
    */
    private int myPointToPosition(int x, int y) {
        Rect frame = mTempRect;
        final int count = getChildCount();
        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            child.getHitRect(frame);
            if (frame.contains(x, y)) {
                return getFirstVisiblePosition() + i;
            }
        }
        return INVALID_POSITION;
    }

    public interface DropListener {
        void drop(int from, int to);
    }

    public void setDropListener(DropListener onDrop) {
        // TODO Auto-generated method stub
        mDropListener = onDrop;
    }
//
//    private class ListMoveHandler extends Handler {
//
//        private final int SCROLLDISTANCE = 20;
//        private final int SCROLLDURATION = 200;
//        private final int MESSAGEWHAT = 111;
//        private final int MESSAGEDELAY = 100;
//
//        private boolean mIsStart = false;  //todo
//        private boolean mIsUp = false; //todo
//
//        public void start(boolean isUp) {
//            mIsUp = isUp;
//            this.mIsStart = true;
//            this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
//        }
//
//        public void stop() {
//            this.mIsStart = false;
//            this.removeMessages(MESSAGEWHAT);
//        }
//
//        public void myDragView() {
//            MyGridView.this.mPopView.dragView(MyGridView.this.mTempX,
//                    MyGridView.this.mTempY);
//
//            int itemnum = getItemForPosition(MyGridView.this.mTempX,
//                    MyGridView.this.mTempY);
//            if (itemnum >= 0) {
//
//                if (itemnum != equals) {
//                    Log.i("test", "myDragView itemnum=" + itemnum + ", mDragPos=" + mDragPos);
//                    MyGridView.this.mDragPos = itemnum;
//
//                    MyGridView.this.doExpansion();
//                }
//            }
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//
//            Log.i("test", "handleMessage: " + msg.what);
//            super.handleMessage(msg);
//
//            myDragView();
//
//            if (mIsStart) {
//                this.sendEmptyMessageDelayed(MESSAGEWHAT, MESSAGEDELAY);
//            }
//        }
//    }
}