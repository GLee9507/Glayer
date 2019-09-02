//package com.gene.glayer.net
//
//import android.os.Environment
//import okhttp3.*
//import okhttp3.internal.cache.DiskLruCache
//import okhttp3.internal.io.FileSystem
//import okio.BufferedSink
//import okio.BufferedSource
//import okio.ForwardingSource
//import okio.Okio
//import java.io.File
//import java.io.IOException
//
//class CacheInterceptor : Interceptor {
//    private val lru by lazy {
//        DiskLruCache.create(
//            FileSystem.SYSTEM,
//            File(Environment.getDataDirectory().absolutePath + "/netCache"),
//            1,
//            1,
//            1024 * 64
//        ).apply { initialize() }
//    }
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//
//    }
//
//    private fun getResponse(request: Request): Response? {
//        val snapshot = lru.get(request.url().toString()) ?: return null
//        return Response.Builder()
//            .body(CacheResponseBody(snapshot))
//            .build()
//    }
//
//}
//
//class CacheResponseBody(val snapshot: DiskLruCache.Snapshot) : ResponseBody() {
//    override fun contentLength(): Long {
//        return snapshot.getLength(0)
//    }
//
//    override fun contentType(): MediaType? = MediaType.parse("application/json")
//
//
//    override fun source(): BufferedSource {
//        return Okio.buffer(object : ForwardingSource(snapshot.getSource(0)) {
//            @Throws(IOException::class)
//            override fun close() {
//                snapshot.close()
//                super.close()
//            }
//        })
//    }
//}
