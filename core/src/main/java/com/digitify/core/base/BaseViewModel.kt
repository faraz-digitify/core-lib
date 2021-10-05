package com.digitify.core.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.base.interfaces.ILifecycle
import com.digitify.core.sealed.UIEvent


abstract class BaseViewModel<S : IBase.State> : ViewModel(),
    ILifecycle, IBase.ViewModel<S> {
    override val clickEvent: SingleClickEvent = SingleClickEvent()
//    fun launch(dispatcher: Dispatcher = Dispatcher.Background, block: suspend () -> Unit): Job {
//        return viewModelScope.launch(
//            when (dispatcher) {
//                Dispatcher.Main -> Dispatchers.Main
//                Dispatcher.Background -> Dispatchers.IO
//                Dispatcher.LongOperation -> Dispatchers.Default
//            }
//        ) { block() }
//    }
//
//    fun <T> launchAsync(block: suspend () -> T): Deferred<T> =
//        viewModelScope.async(Dispatchers.IO) {
//            block()
//        }
//
//    fun onClick(view: View) {
//        clickEvent.setValue(view.id)
//    }

    override fun hideLoading(onBackGround: Boolean) {
        if (onBackGround)
            viewState.uiEvent.postValue(UIEvent.Loading(false))
        else
            viewState.uiEvent.value = UIEvent.Loading(false)
    }


    override fun showLoading(onBackGround: Boolean) {
        if (onBackGround)
            viewState.uiEvent.postValue(UIEvent.Loading(true))
        else
            viewState.uiEvent.value = UIEvent.Loading(true)
    }

    override fun showToast(message: String) {
        viewState.uiEvent.value = UIEvent.Message(message)
    }

    override fun showToast(message: String, onBackGround: Boolean) {
        if (onBackGround)
            viewState.uiEvent.postValue(UIEvent.Message(message))
        else
            showToast(message)
    }

    override fun showAlertMessage(message: String) {
        viewState.uiEvent.value = UIEvent.Alert(message)
    }

    override fun showAlertMessage(message: String, onBackGround: Boolean) {
        if (onBackGround)
            viewState.uiEvent.postValue(UIEvent.Alert(message))
        else
            showAlertMessage(message)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun onCreate() {
        //to do later if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    override fun onStart() {
        //to do later if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    override fun onResume() {
        //to do later if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    override fun onPause() {
        //to do later if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    override fun onStop() {
        //to do later if needed
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun onDestroy() {
        //to do later if needed
    }

    override fun registerLifecycleOwner(owner: LifecycleOwner?) {
        unregisterLifecycleOwner(owner)
        owner?.lifecycle?.addObserver(this)
    }

    override fun unregisterLifecycleOwner(owner: LifecycleOwner?) {
        owner?.lifecycle?.removeObserver(this)
    }
}