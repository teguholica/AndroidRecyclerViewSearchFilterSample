package com.teguholica.androidrecyclerviewsearchfilter

import android.app.Application
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class App: Application() {

    val TAG = App::class.java.simpleName
    private lateinit var mRequestQueue: RequestQueue

    override fun onCreate() {
        super.onCreate()
        instance = this
        mRequestQueue = Volley.newRequestQueue(applicationContext)
    }

    private fun getRequestQueue(): RequestQueue {
        return mRequestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String = TAG) {
        req.tag = tag
        getRequestQueue().add(req)
    }

    fun cancelPendingRequests(tag: String) {
        mRequestQueue.cancelAll(tag)
    }

    companion object {

        lateinit var instance: App

    }

}
