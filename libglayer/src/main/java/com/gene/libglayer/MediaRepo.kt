package com.gene.libglayer

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.databinding.ObservableField
import com.gene.libglayer.model.Media
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaRepo : ObservableField<MediaListMap>() {
    var versionCode = 0

    fun scan(path: String? = null) {
        val realPath: String = path ?: Environment.getExternalStorageDirectory().absolutePath
        MediaScannerConnection.scanFile(
            APP,
            arrayOf(realPath),
            arrayOf("audio/*")
        ) { path, uri ->
            Log.d("glee", path + uri)
        }
    }

    private val audioObserver = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            searchAndUpdate()
        }
    }

    init {
        APP.contentResolver.registerContentObserver(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            true,
            audioObserver
        )
    }

    private var titleIndex: Int = -1
    private var artistIndex: Int = -1
    private var albumIndex: Int = -1
    private var durationIndex: Int = -1
    private var dataIndex: Int = -1
    private var mimeTypeIndex: Int = -1
    private var sizeIndex: Int = -1
    private var displayNameIndex: Int = -1
    private var idIndex: Int = -1

    fun searchAndUpdate() {
        IO.launch {
            val search = search()
            withContext(Dispatchers.Main) {
                versionCode++
                set(search)
            }
        }
    }

    @SuppressLint("Recycle")
    fun search(): MediaListMap {
        val listMap = MediaListMap()

        val cursor = APP.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media._ID
            ),
            null, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        ) ?: return listMap
        try {
            if (titleIndex == -1) {
                titleIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                artistIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                albumIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
                durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                dataIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                mimeTypeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
                sizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                displayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                idIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            }
            Log.d("glee", Thread.currentThread().name)
            while (cursor.moveToNext()) {
                val media = Media(
                    title = cursor.getString(titleIndex),
                    artist = cursor.getString(artistIndex),
                    album = cursor.getString(albumIndex),
                    data = cursor.getString(dataIndex),
                    mimeType = cursor.getString(mimeTypeIndex),
                    displayName = cursor.getString(displayNameIndex),
                    duration = cursor.getInt(durationIndex),
                    size = cursor.getInt(sizeIndex),
                    id = cursor.getInt(idIndex)
                )
                listMap.add(media)
            }
        } catch (e: Exception) {
            Log.d("glee", e.toString())
        } finally {
            try {
                cursor.close()
            } catch (e: Exception) {

            }
        }
        return listMap
    }

    fun onCleared() {
        APP.contentResolver.unregisterContentObserver(audioObserver)
    }
}