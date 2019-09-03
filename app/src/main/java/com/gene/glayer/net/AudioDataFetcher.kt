package com.gene.glayer.net

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.io.File

class AudioDataFetcher:DataFetcher<File> {
    override fun getDataClass(): Class<File> {
        return File::class.java
    }

    override fun cleanup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataSource(): DataSource {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in File>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}