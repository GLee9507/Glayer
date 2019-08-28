package com.gene.libglayer.model

import android.os.Parcel
import android.os.Parcelable

data class Media(
    val title: String? = "",
    val artist: String? = "",
    val album: String? = "",
    val data: String? = "",
    val mimeType: String? = "",
    val displayName: String? = "",
    val size: Int = -1,
    val duration: Int = -1,
    val id: Int = -1
) : Parcelable {
    override fun toString(): String {
        return "Media(title='$title', artist='$artist', album='$album', data='$data', mimeType='$mimeType', displayName='$displayName', size=$size, duration=$duration, id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null) false else hashCode() == other.hashCode()
    }

    override fun hashCode(): Int {
        return id
    }

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(artist)
        writeString(album)
        writeString(data)
        writeString(mimeType)
        writeString(displayName)
        writeInt(size)
        writeInt(duration)
        writeInt(id)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Media> = object : Parcelable.Creator<Media> {
            override fun createFromParcel(source: Parcel): Media = Media(source)
            override fun newArray(size: Int): Array<Media?> = arrayOfNulls(size)
        }
    }
}