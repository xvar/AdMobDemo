package allgoritm.com.youla.views.loadingRecyclerView.Dummy

import android.content.Context
import android.util.AttributeSet
import android.view.View

class CommonErrorDummy: LRVErrorDummy {

    constructor(context: Context): super (context, null)
    constructor(context: Context, attrs: AttributeSet?): super (context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)

    fun fillDummy(imageRes: Int, titleRes: Int, descriptionRes: Int, buttonTextRes: Int) {
        show()
        dummyImageView.setImageResource(imageRes)
        dummyTitleTextView.setText(titleRes)
        dummyDescriptionTextView.setText(descriptionRes)
        dummyButton.setText(buttonTextRes)
    }


    fun retryBtn(): View = dummyButton

}