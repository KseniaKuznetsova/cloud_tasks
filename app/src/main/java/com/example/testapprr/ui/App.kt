package com.example.testapprr.ui

import android.app.Application
import com.example.testapprr.model.Repository

class App: Application() {


    override fun onCreate() {
        super.onCreate()
        Repository.initRepository(this)
    }


}