package fodel.com.fodelscanner.view;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.FrameLayout;
public class TouchableWrapper extends FrameLayout {

	private float downX = 0;  
    private float downY = 0;  
    private float upX = 0;  
    private float upY = 0;  
    private static final float MOVE_DISTANCE = 40;  
    private UpdateMapAfterUserInterection updateMapAfterUserInterection;  
  
    public TouchableWrapper(Context context) {  
        super(context);  
        // Force the host activity to implement the UpdateMapAfterUserInterection Interface  
        try {  
            updateMapAfterUserInterection = (UpdateMapAfterUserInterection) context;
        } catch (ClassCastException e) {  
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");  
        }  
    }  
  
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        switch (ev.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
//          lastTouched = SystemClock.uptimeMillis();  
            downX = ev.getX();  
            downY = ev.getY();  
            break;  
        case MotionEvent.ACTION_UP:  
            upX = ev.getX();  
            upY = ev.getY();  
            float intervalX = Math.abs(upX - downX);  
            float intervalY = Math.abs(upY - downY);  
            if (intervalX > MOVE_DISTANCE || intervalY > MOVE_DISTANCE) {  
                // Update the map  
                updateMapAfterUserInterection.onUpdateMapAfterUserInterection();  
            }  
            break;  
        }  
        return super.dispatchTouchEvent(ev);  
    }  
  
    // Map Activity must implement this interface  
    public interface UpdateMapAfterUserInterection {  
        public void onUpdateMapAfterUserInterection();  
    }  
}
