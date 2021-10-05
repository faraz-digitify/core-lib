package com.digitify.core.base

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.sealed.UIEvent

abstract class BaseFragment<VB : ViewDataBinding, VS : IBase.State, VM : IBase.ViewModel<VS>>(@LayoutRes val contentLayoutId: Int) :
    IBase.View<VM>, Fragment(contentLayoutId),
    OnBackPressedListener {

    protected final val activityLauncher: BetterActivityResult<Intent, ActivityResult> =
        BetterActivityResult.registerActivityForResult(this)

    private lateinit var onBackPressedCallback: OnBackPressedCallback
    lateinit var mViewBinding: VB
    private var progress: Dialog? = null
    abstract fun getBindingVariable(): Int
    abstract fun onClick(id: Int)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewBinding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)
        return mViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerStateListeners()
        performDataBinding()
        viewModel.clickEvent.observe(this, {
            onClick(it)
        })
        viewModel.viewState.uiEvent.observe(viewLifecycleOwner, {
            it?.let {
                when (it) {
                    is UIEvent.Message -> {
                        showToast(it.message)
                    }
                    is UIEvent.Loading -> {
                        showLoading(it.isLoading)
                    }
                    is UIEvent.Alert -> {
                        showAlertMessage(it.message)
                    }
                    else -> {
                        // handle later
                    }
                }
            }
        })
    }

    private fun performDataBinding() {
        mViewBinding.setVariable(getBindingVariable(), viewModel)
        mViewBinding.lifecycleOwner = viewLifecycleOwner
        mViewBinding.executePendingBindings()
    }

    protected fun showToast(msg: String) {
        if (activity is BaseActivity<*, *, *>)
            (activity as BaseActivity<*, *, *>).showToast(msg)
    }

    protected fun showLoading(isVisible: Boolean) {
        if (activity is BaseActivity<*, *, *>)
            (activity as BaseActivity<*, *, *>).showLoader(isVisible)
    }

    protected fun showAlertMessage(msg: String) {
        if (activity is BaseActivity<*, *, *>)
            (activity as BaseActivity<*, *, *>).showAlertMessage(msg)
    }

//    fun launch(dispatcher: Dispatcher = Dispatcher.Main, block: suspend () -> Unit) {
//        lifecycleScope.launch(
//            when (dispatcher) {
//                Dispatcher.Main -> Dispatchers.Main
//                Dispatcher.Background -> Dispatchers.IO
//                Dispatcher.LongOperation -> Dispatchers.Default
//            }
//        ) { block() }
//    }

    fun setBackButtonDispatcher() {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onBackPressed(): Boolean = true

    private fun registerStateListeners() {
        if (viewModel is BaseViewModel<*>) {
            viewModel.registerLifecycleOwner(this)
        }
    }

    private fun unregisterStateListeners() {
        if (viewModel is BaseViewModel<*>) {
            viewModel.unregisterLifecycleOwner(this)
        }
    }

    override fun onDestroyView() {
        unregisterStateListeners()
        progress?.dismiss()
        viewModel.clickEvent.removeObservers(this)
        viewModel.viewState.uiEvent.removeObservers(this)
        super.onDestroyView()
    }
}

interface OnBackPressedListener {
    fun onBackPressed(): Boolean
}
