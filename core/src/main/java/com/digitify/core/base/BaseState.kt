package com.digitify.core.base

import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.sealed.UIEvent

abstract class BaseState : BaseObservable(), IBase.State {
    override var toolbarTitle: MutableLiveData<String> = MutableLiveData("")
    override var toolsBarVisibility: ObservableBoolean = ObservableBoolean()
    override var uiEvent: MutableLiveData<UIEvent> = MutableLiveData()
}