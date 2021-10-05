package com.digitify.core.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.databinding.ViewDataBinding
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import com.digitify.core.R
import com.digitify.core.base.interfaces.IBase
import com.digitify.core.extensions.addExtras
import com.digitify.core.extensions.plus
import com.digitify.core.utils.EXTRA

/**
 * A base BaseNavViewModel Activity with built-in support for Android X Navigation Concept and ViewModel.
 */
abstract class BaseNavActivity<VB : ViewDataBinding, VS : IBase.State, VM : IBase.ViewModel<VS>> :
    BaseActivity<VB, VS, VM>() {

    /**
     * Used to obtain the exact id of the navigation graph to be used by this activity.
     *
     * @return the id of the navigation graph
     */
    @get:NavigationRes
    abstract val navigationGraphId: Int

    /**
     * Override this property to specify a custom Start Destination.
     *
     * @return the exact id of the destination to be used as the starting one.
     */
    @get:IdRes
    protected open val navigationGraphStartDestination: Int = 0

    /**
     * Accesses the The NavController associated with the current activity.
     */
    val navController: NavController
        get() = findNavController(R.id.nav_host_fragment)

    /**
     * The initial input to be provided to the start destination fragment.
     */
    protected open var startDestinationInput: Bundle? = Bundle()
    protected open var extrasBundle = Bundle()
    private var navHostFragment: NavHostFragment? = null

    @CallSuper
    override fun init(savedInstanceState: Bundle?) {
        initNavigationGraph()
    }

    override fun preInit(savedInstanceState: Bundle?) {
        super.preInit(savedInstanceState)
        if (intent?.hasExtra(EXTRA) == true) {
            startDestinationInput = intent?.getBundleExtra(EXTRA)
        }
    }

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            onDestinationChanged(controller, destination, arguments)
        }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
    }

    abstract fun onDestinationChanged(
        controller: NavController?,
        destination: NavDestination?,
        arguments: Bundle?
    )

    protected fun getCurrentFragment() = navHostFragment?.childFragmentManager?.fragments?.get(0)


    private fun initNavigationGraph() {
        try {
            navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            navHostFragment?.navController?.apply {
                navInflater
                graph = navInflater.inflate(navigationGraphId).also {
                    it.startDestination =
                        (if (navigationGraphStartDestination != 0) navigationGraphStartDestination else it.startDestination)
                    it.addExtras(extrasBundle.plus(startDestinationInput ?: Bundle()))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException(e.message)
        }
    }


    /**
     * Navigates to the specified destination screen.
     *
     * @param destinationId the id of the destination screen (either the new Activity or Fragment)
     * @param extras the extra arguments to be passed to the destination screen
     */
    protected fun navigate(@IdRes destinationId: Int, extras: Bundle? = null) {
        navController.navigate(destinationId, extras)
    }

    /**
     * Navigates to the specified destination screen.
     *
     * @param directions the direction that leads to the destination screen.
     * @param navigationExtras
     */
    protected fun navigate(directions: NavDirections, navigationExtras: Navigator.Extras? = null) {
        navigationExtras?.let { navExtras ->
            navController.navigate(directions, navExtras)
        } ?: run {
            navController.navigate(directions)
        }
    }

    override fun onBackPressed() {
        if ((getCurrentFragment() as BaseFragment<*, *, *>).onBackPressed())
            super.onBackPressed()
    }
}
