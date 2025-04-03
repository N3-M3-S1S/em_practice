package com.nemesis.empractice

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlin.random.Random

private const val FRAGMENT_COLOR_KEY = "color"
private const val FRAGMENT_INDEX_KEY = "index"

class RouterFragment : Fragment(R.layout.router_fragment) {
    private var fragmentIndex = 0
    private var fragmentColor = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            fragmentColor = savedInstanceState.getInt(FRAGMENT_COLOR_KEY)
            fragmentIndex = savedInstanceState.getInt(FRAGMENT_INDEX_KEY)
        } else {
            fragmentColor = generateRandomColor()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(view) {
            setBackgroundColor(fragmentColor)
            findViewById<TextView>(R.id.fragmentNumberText).text = buildFragmentNumberText()

            val router = (requireActivity() as MainActivity).router
            findViewById<Button>(R.id.navigateBackwardButton).setOnClickListener {
                router.navigate(
                    Router.NavigationDirection.BACKWARD
                )
            }
            findViewById<Button>(R.id.navigateForwardButton).setOnClickListener {
                router.navigate(
                    Router.NavigationDirection.FORWARD
                )
            }
        }
    }

    private fun buildFragmentNumberText() = "Fragment #$fragmentIndex"

    fun setFragmentNumber(number: Int) {
        fragmentIndex = number
    }

    private fun generateRandomColor() =
        Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(FRAGMENT_COLOR_KEY, fragmentColor)
        outState.putInt(FRAGMENT_INDEX_KEY, fragmentIndex)
    }

}