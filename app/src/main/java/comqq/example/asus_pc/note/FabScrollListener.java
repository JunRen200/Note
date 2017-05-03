package comqq.example.asus_pc.note;

import android.support.v7.widget.RecyclerView;
import android.view.ViewConfiguration;

/**
 * Created by asus-pc on 2017/5/3.
 */

public class FabScrollListener extends RecyclerView.OnScrollListener {
    interface HideScrollListener{
        public void onShow();
        public void onHide();
    }
    private int distance = 0;//滑动的距离，根据距离于临界值比较判断是向上滑还是向下滑
    private HideScrollListener hideListener;//这里使用监听让Activity进行实现
    private boolean visible = true;//是否可见

    public FabScrollListener(HideScrollListener hideScrollListener) {
        // TODO Auto-generated constructor stub
        this.hideListener = hideScrollListener;
    }
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        //dy有正有负，向上滑为负 向下滑为正
        if (distance < -ViewConfiguration.getTouchSlop() && !visible) {
            //显示Fab和ToolBar
            hideListener.onShow();
            distance = 0;
            visible = true;
        } else if (distance > ViewConfiguration.getTouchSlop() && visible) {
            //隐藏Fab和ToolBar
            hideListener.onHide();
            distance = 0;
            visible = false;
        }
        if ((dy > 0 && visible) || (dy < 0 && !visible))//向下滑并且可见  或者  向上滑并且不可见
            distance += dy;
    }
}