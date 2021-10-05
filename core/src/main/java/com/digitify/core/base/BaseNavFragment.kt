package com.digitify.core.base

import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.digitify.core.R
import com.digitify.core.base.interfaces.IBase
import kotlin.properties.Delegates

private const val ARGUMENT_NAVIGATION_REQUEST_CODE = "NAVIGATION_REQUEST_CODE"

const val DESTINATION_NOT_SET = -1
const val REQUEST_CODE_NOT_SET = -1

abstract class BaseNavFragment<VB : ViewDataBinding, VS : IBase.State, VM : IBase.ViewModel<VS>>(@LayoutRes val layoutId: Int) :
    BaseFragment<VB, VS, VM>(layoutId) {
    private val requestCode: Int
        get() = arguments?.getInt(ARGUMENT_NAVIGATION_REQUEST_CODE, REQUEST_CODE_NOT_SET)
            ?: REQUEST_CODE_NOT_SET

    /**
     * Navigates to the specified destination screen.
     *
     * @param destinationId the id of the destination screen (either the new Activity or Fragment)
     * @param extras the extra arguments to be passed to the destination screen
     * @param navigationExtras
     */
    fun navigate(
        @IdRes destinationId: Int,
        extras: Bundle? = Bundle(),
        navigationExtras: Navigator.Extras? = null,
        navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    ) {
        findNavController().navigate(
            destinationId,
            extras,
            navOptions,
            navigationExtras,
        )
    }

    /**
     * Navigates to the specified destination screen.
     *
     * @param directions the direction that leads to the destiantion screen.
     * @param navigationExtras
     */
    protected fun navigate(directions: NavDirections, navigationExtras: Navigator.Extras? = null) {
        navigationExtras?.let { navExtras ->
            findNavController().navigate(directions, navExtras)
        } ?: run {
            findNavController().navigate(directions)
        }
    }

    fun navigateWithPopup(
        @IdRes destinationId: Int, @IdRes popupTo: Int, extras: Bundle? = Bundle(),
        navigationExtras: Navigator.Extras? = null, enableAnimation: Boolean = true
    ) {
        navigate(
            destinationId,
            extras,
            navigationExtras,
            navOptions = navOptions {
                popUpTo(popupTo) {
                    inclusive = true
                }
                if (enableAnimation) {
                    anim {
                        enter = R.anim.slide_in_right
                        exit = R.anim.slide_out_left
                        popEnter = R.anim.slide_in_left
                        popExit = R.anim.slide_out_right
                    }
                }
            })
    }

    /**
     * Navigates back (pops the back stack) to the previous [MvvmFragment] on the stack.
     */
    protected fun navigateBack() {
        findNavController().popBackStack()
    }

    protected fun navigateUp(): Boolean = findNavController().navigateUp()

    protected fun navigateForResultWithAnimation(
        requestCode: Int, navDirections: NavDirections, navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }
    ) {
        this.exitTransition = Fade()
        navigateForResult(
            resId = navDirections.actionId,
            requestCode = requestCode,
            args = navDirections.arguments,
            navOptions = navOptions,
            navigatorExtras = null
        )
    }

    protected fun navigateForResult(
        requestCode: Int, navDirections: NavDirections, navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }, extras: Bundle? = Bundle(),

        navigatorExtras: Navigator.Extras? = null
    ) =
        navigateForResult(
            resId = navDirections.actionId,
            requestCode = requestCode,
            args = extras,
            navOptions = navOptions,
            navigatorExtras = navigatorExtras,

            )

    protected fun navigateForResult(
        @IdRes resId: Int,
        requestCode: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        },
        navigatorExtras: Navigator.Extras? = null
    ) {
        val argsWithRequestCode = (args ?: Bundle()).apply {
            putInt(ARGUMENT_NAVIGATION_REQUEST_CODE, requestCode)
        }
        navigatorExtras?.let {
            findNavController().navigate(
                resId,
                argsWithRequestCode,
                navOptions,
                navigatorExtras
            )
        } ?: findNavController().navigate(resId, argsWithRequestCode, navOptions)
    }


    protected fun navigate(
        navDirections: NavDirections,
        args: Bundle?
    ) {
        navigateForResult(navDirections.actionId, REQUEST_CODE_NOT_SET, args)
    }

    protected fun navigateBackWithResult(resultCode: Int, data: Bundle? = null): Boolean =
        navigateBackWithResult(
            DESTINATION_NOT_SET,
            BackNavigationResult(requestCode, resultCode, data)
        )

    protected fun navigateBackWithResult(
        @IdRes destination: Int,
        resultCode: Int,
        data: Bundle? = null
    ): Boolean =
        navigateBackWithResult(destination, BackNavigationResult(requestCode, resultCode, data))

    protected fun initEnterTransitions() {
        sharedElementEnterTransition = ChangeBounds()
        enterTransition = Fade()
    }

    private fun navigateBackWithResult(
        @IdRes destination: Int,
        result: BackNavigationResult
    ): Boolean {
        val childFragmentManager =
            requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager
        var backStackListener: FragmentManager.OnBackStackChangedListener by Delegates.notNull()
        backStackListener = FragmentManager.OnBackStackChangedListener {
            (childFragmentManager?.fragments?.get(0) as? BackNavigationResultListener)?.onNavigationResult(
                result
            )
            childFragmentManager?.removeOnBackStackChangedListener(backStackListener)
        }
        childFragmentManager?.addOnBackStackChangedListener(backStackListener)
        val backStackPopped = if (destination == DESTINATION_NOT_SET) {
            findNavController().popBackStack()
        } else {
            findNavController().popBackStack(destination, true)
        }
        if (!backStackPopped) {
            childFragmentManager?.removeOnBackStackChangedListener(backStackListener)
        }
        return backStackPopped
    }
}