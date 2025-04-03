package com.nemesis.empractice

import android.os.Bundle
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity(R.layout.main) {
    lateinit var router: Router
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = Router(supportFragmentManager, R.id.fragmentContainer)
    }

}
