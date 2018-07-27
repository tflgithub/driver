package fodel.com.fodelscanner.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.ListView;

import fodel.com.fodelscanner.R;

/**
 * Created by Administrator on 2017/2/24.
 */
public class ScaleHeadListView extends ListView {

    private int mImageViewHeight;
    private ImageView mImage;

    public void setImageView(ImageView mImage) {
        this.mImage = mImage;
    }

    public ScaleHeadListView(Context context) {
        super(context);
        mImageViewHeight = context.getResources().getDimensionPixelSize(R.dimen.x50);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

        if (deltaY < 0) {
            mImage.getLayoutParams().height = mImage.getHeight() - deltaY;
            mImage.requestLayout();
        } else {
            mImage.getLayoutParams().height = mImage.getHeight() - deltaY;
            mImage.requestLayout();
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View header = (View) mImage.getParent();
        int deltaY = header.getTop();
        if (mImage.getHeight() > mImageViewHeight) {
            mImage.getLayoutParams().height = mImage.getHeight() + deltaY;
            header.layout(header.getLeft(), 0, header.getBottom(), header.getRight());
            mImage.requestLayout();
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            MyAnimation myAnimation = new MyAnimation(mImageViewHeight);
            myAnimation.setDuration(700);
            myAnimation.setInterpolator(new AnticipateInterpolator());
            myAnimation.start();
        }
        return super.onTouchEvent(ev);
    }

    class MyAnimation extends Animation {

        private int extraHeight;

        public MyAnimation(int height) {
            extraHeight = mImage.getHeight() - height;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mImage.getLayoutParams().height = (int) (mImage.getHeight() - extraHeight* interpolatedTime);
            mImage.requestLayout();
            super.applyTransformation(interpolatedTime, t);
        }
    }
}
