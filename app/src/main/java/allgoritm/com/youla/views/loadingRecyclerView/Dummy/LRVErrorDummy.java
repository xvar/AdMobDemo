package allgoritm.com.youla.views.loadingRecyclerView.Dummy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by next on 16.02.16.
 */
public class LRVErrorDummy extends LRVDummy {

    public LRVErrorDummy(Context context) {
        super(context);
        prepare();
    }

    public LRVErrorDummy(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare();
    }

    public LRVErrorDummy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepare();
    }

    public LRVErrorDummy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        prepare();
    }

    private void prepare() {
        dummyButton.setVisibility(View.VISIBLE);
    }

}
