package com.digitify.core.base

import com.digitify.core.base.BackNavigationResult

interface BackNavigationResultListener {
    fun onNavigationResult(result: BackNavigationResult)
}
