package allgoritm.com.youla.views.loadingRecyclerView.Dummy;

import allgoritm.com.youla.admob.demo.R;
import allgoritm.com.youla.utils.ScreenUtils;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/**
 * Created by next on 16.02.16.
 */
public abstract class LRVDummy extends FrameLayout {

    protected ImageView dummyImageView;
    protected TextView dummyTitleTextView;
    protected TextView dummyDescriptionTextView;
    protected Button dummyButton;

    protected ViewGroup dummyWrapper;
    protected RelativeLayout dummyRelativeLayout;

    private int toolbarHeight;

    public LRVDummy(Context context) {
        super(context);
        prepare(context, null);
    }

    public LRVDummy(Context context, AttributeSet attrs) {
        super(context, attrs);
        prepare(context, attrs);
    }

    public LRVDummy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        prepare(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LRVDummy(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        prepare(context, attrs);
    }

    private void prepare(Context context, AttributeSet attributeSet){
        View view = inflate(context, R.layout.lrv_dummy_view, this);
        dummyImageView = (ImageView)view.findViewById(R.id.dummy_imageView);
        dummyTitleTextView = (TextView)view.findViewById(R.id.dummyTitle_textView);
        dummyDescriptionTextView = (TextView)view.findViewById(R.id.dummyDescription_textView);
        dummyButton = (Button)view.findViewById(R.id.dummyButton_button);
        dummyWrapper = (ViewGroup)view.findViewById(R.id.dummy_wrapper);
        dummyRelativeLayout = (RelativeLayout)view.findViewById(R.id.dummy_relativeLayout);

        toolbarHeight = ScreenUtils.dpToPx(56);
    }

    public void show(final int margin) {
        show(margin, 0);
    }

    public void show(final int margin, final int bottomNavigationHeight) {
        this.post(new Runnable() {
            @Override
            public void run() {
                int marginTop = margin;
                int height = LRVDummy.this.getHeight();
                int contentHeight = dummyWrapper.getHeight();
                MarginLayoutParams params = (MarginLayoutParams)dummyWrapper.getLayoutParams();
                marginTop = Math.min(marginTop, (height - contentHeight));
                if (marginTop > 0) {
                    params.topMargin = (int)((height - contentHeight - marginTop + toolbarHeight - bottomNavigationHeight) / 2);
                }
                else {
                    params.topMargin = ScreenUtils.dpToPx(16);
                }
                dummyWrapper.setLayoutParams(params);
                dummyRelativeLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                show();
            }
        });
    }

    public void show() {
        dummyRelativeLayout.setVisibility(VISIBLE);
    }

    public void hide() {
        dummyRelativeLayout.setVisibility(INVISIBLE);
    }

    public void setButtonListener(OnClickListener listener) {
        dummyButton.setOnClickListener(listener);
    }
}
