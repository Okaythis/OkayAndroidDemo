package com.ogieben.okaydemo.ui.main

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {

    val sessionId: MutableLiveData<Long> = MutableLiveData()
}