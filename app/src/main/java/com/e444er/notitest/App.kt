package com.e444er.notitest

import android.app.Application

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        NotificationChannels.create(this@App)
    }
}