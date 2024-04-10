package com.sency.smkitui.demo

import android.app.Application
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SMKitUIDemoApplication : Application() {

    private val _didFinishAuth = MutableStateFlow<Boolean?>(null)
    val didFinishAuth = _didFinishAuth.asStateFlow()

    val smKitUI: SMKitUI = SMKitUI.Configuration(applicationContext)
        .setUIKey("YOUR_KEY")
        .configure(object : ConfigurationResult {
            override fun onSuccess() {
                //The configuration was successful
                _didFinishAuth.value = true
            }

            override fun onFailure() {
                //The configuration failed with error
                _didFinishAuth.value = false
            }
        })

}