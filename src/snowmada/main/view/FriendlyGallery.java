package snowmada.main.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

public class FriendlyGallery extends Gallery {
    FriendlyScrollView currScrollView;

    public FriendlyGallery(Context context) {
        super(context);
    }
    public FriendlyGallery(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);  
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        currScrollView = getCurrScrollView();
        return super.onInterceptTouchEvent(ev);     
    }
    @Override
    public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(currScrollView != null)
            currScrollView.scrollBy(0, (int) distanceY);
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(currScrollView != null)
            currScrollView.fling(-(int) distanceY);
        return super.onFling(e1, e2, distanceX, distanceY);     
    }

    private FriendlyScrollView getCurrScrollView() {
        int pos = getFirstVisiblePosition();
        if(pos != getAdapter().getCount()-1)
            return (FriendlyScrollView)this.getSelectedView();
        else
            return null;
    }
}