package com.example.cachevideowithexoplayer

import android.app.Application
import android.content.Context

class MyApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        mApplicationClass = this
    }

    companion object{
        @JvmStatic
        private lateinit var mApplicationClass: MyApplication

        @JvmStatic
        val application: MyApplication by lazy { mApplicationClass }

        fun getContext(): Context {
            return application.baseContext
        }
    }
}