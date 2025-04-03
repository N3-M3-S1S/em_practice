package com.nemesis.empractice

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commitNow

private const val MAX_FRAGMENTS_COUNT = 3

class Router(
    private val fragmentManager: FragmentManager,
    private val fragmentContainerId: Int
) {
    private var currentFragmentIndex = 0

    init {
        if (!tryRestoreCurrentFragmentIndex()) {
            addInitialFragment()
        }
    }

    private fun tryRestoreCurrentFragmentIndex(): Boolean {
        if (fragmentManager.fragments.isEmpty()) {
            return false
        }
        val currentFragment = fragmentManager.fragments.first { !it.isHidden }
        currentFragmentIndex = currentFragment.tag?.toIntOrNull() ?: 0
        return true
    }

    private fun addInitialFragment() {
        fragmentManager.commitNow {
            add(
                fragmentContainerId,
                createFragmentForCurrentIndex(),
                getFragmentTagForCurrentIndex()
            )
        }
    }

    /*
        Use show/hide just to keep fragment's state on orientation change.
        Alternative - use replace and keep fragments in the map.
     */
    fun navigate(direction: NavigationDirection) {
        fragmentManager.commitNow {
            setCustomAnimations(direction.enterAnimationId, direction.exitAnimationId)

            val currentFragment =
                requireNotNull(fragmentManager.findFragmentByTag(getFragmentTagForCurrentIndex()))
            hide(currentFragment)

            currentFragmentIndex = calculateNextFragmentIndex(direction)

            val nextFragmentTag = getFragmentTagForCurrentIndex()
            val hiddenFragment = fragmentManager.findFragmentByTag(nextFragmentTag)
            if (hiddenFragment != null) {
                show(hiddenFragment)
            } else {
                add(fragmentContainerId, createFragmentForCurrentIndex(), nextFragmentTag)
            }
        }
    }

    private fun createFragmentForCurrentIndex() = RouterFragment().apply {
        setFragmentNumber(currentFragmentIndex)
    }

    private fun getFragmentTagForCurrentIndex() = currentFragmentIndex.toString()

    private fun calculateNextFragmentIndex(navigationDirection: NavigationDirection) =
        when (navigationDirection) {
            NavigationDirection.BACKWARD -> if (currentFragmentIndex == 0) MAX_FRAGMENTS_COUNT - 1 else currentFragmentIndex - 1
            NavigationDirection.FORWARD -> if (currentFragmentIndex == MAX_FRAGMENTS_COUNT - 1) 0 else currentFragmentIndex + 1
        }

    enum class NavigationDirection {
        BACKWARD, FORWARD
    }

    private val NavigationDirection.enterAnimationId
        get() = when (this) {
            NavigationDirection.BACKWARD -> R.anim.backward_enter_anim
            NavigationDirection.FORWARD -> R.anim.forward_enter_anim
        }

    private val NavigationDirection.exitAnimationId
        get() = when (this) {
            NavigationDirection.BACKWARD -> R.anim.backward_exit_anim
            NavigationDirection.FORWARD -> R.anim.forward_exit_anim
        }
}