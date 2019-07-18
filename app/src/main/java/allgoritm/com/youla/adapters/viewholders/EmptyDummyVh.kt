package allgoritm.com.youla.adapters.viewholders

import allgoritm.com.youla.admob.demo.R
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import allgoritm.com.youla.adapters.EmptyDummyItem


class EmptyDummyVh(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    val dummyImageView = view.findViewById<ImageView>(R.id.dummy_imageView)
    val dummyTitleTextView = view.findViewById<TextView>(R.id.dummyTitle_textView)
    val dummyDescriptionTextView = view.findViewById<TextView>(R.id.dummyDescription_textView)
    val dummyButton = view.findViewById<Button>(R.id.dummyButton_button)
    val dummyWrapper = view.findViewById<ViewGroup>(R.id.dummy_wrapper)
    val dummyRelativeLayout = view.findViewById<RelativeLayout>(R.id.dummy_relativeLayout)

    fun bind(item: EmptyDummyItem) {
        dummyRelativeLayout.visibility = View.VISIBLE
        if (item.imageRes != -1) {
            dummyImageView.setImageResource(item.imageRes)
        }
        dummyTitleTextView.text = item.title
        dummyDescriptionTextView.text = item.description
        dummyButton.isVisible = item.buttonText.isNotBlank()
        if (item.buttonText.isNotBlank()) {
            dummyButton.text = item.buttonText
        }
    }

}