package fodel.com.fodelscanner.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by fula on 2018/5/23.
 */

public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {

    int totalItemCount;// 总数量
    int lastVisibieItem;// 最后一个可见的item;
    boolean isLoading;// 判断变量
    IloadListener iLoadListener;// 接口变量
    boolean isLast;//最后

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadMoreListView(Context context) {
        super(context);
        initView();
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItemCount == lastVisibieItem && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading && !isLast) {
                isLoading = true;
                iLoadListener.onLoad();
            }
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibieItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    public void setIloadListener(IloadListener iLoadListener) {
        this.iLoadListener = iLoadListener;
    }

    // 加载更多数据的回调接口
    public interface IloadListener {
        void onLoad();
    }

    public void setLast(boolean isLast) {
        this.isLast = isLast;
    }

    // 加载完成通知隐藏
    public void loadComplete() {
        isLoading = false;
    }
}
