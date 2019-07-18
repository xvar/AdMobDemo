package allgoritm.com.youla.models.entity

import android.os.Parcel
import android.os.Parcelable

data class Badge(val title: String?,
                 val textColor: String?,
                 val backgroundColor: String?,

                 val contentDescription: String? = title,
                 val showDistance : Boolean = false,
                 val showDelivery : Boolean = false
 ) : Parcelable {

    //java-compatibility
    constructor(title: String?, textColor: String?, backgroundColor: String?) :
            this(title, textColor, backgroundColor, title, false, false)

    constructor(): this ("", "", "", "")

    //cut-out

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(textColor)
        writeString(backgroundColor)
        writeString(contentDescription)
    }

    fun isEmpty() = title.isNullOrEmpty() || textColor.isNullOrEmpty() || backgroundColor.isNullOrEmpty()

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Badge> = object : Parcelable.Creator<Badge> {
            override fun createFromParcel(source: Parcel): Badge = Badge(source)
            override fun newArray(size: Int): Array<Badge?> = arrayOfNulls(size)
        }
    }
}