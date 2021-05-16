package com.example.android.besthdwallpapers.ui.activity.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.besthdwallpapers.repository.Repository
import com.example.android.besthdwallpapers.util.Constants.LAUNCHES
import com.example.android.besthdwallpapers.util.getAny
import com.example.android.besthdwallpapers.util.putAny
import kotlinx.coroutines.launch

class MainViewModel(private val preferences: SharedPreferences) : ViewModel() {
    val isRateDialogShow=MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            var numberOfLaunches=preferences.getAny(0,LAUNCHES) as Int
            when(numberOfLaunches){
                in 0..3->{
                    numberOfLaunches++
                    preferences.putAny(LAUNCHES,numberOfLaunches)
                }
                else->isRateDialogShow.postValue(true)
            }
        }
    }
}