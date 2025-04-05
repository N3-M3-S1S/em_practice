package com.nemesis.empractice.di

import com.nemesis.empractice.MainActivity
import com.nemesis.feature_home.di.HomeComponent
import dagger.Component

@AppScope
@Component(dependencies = [HomeComponent::class])
interface AppComponent {

    @Component.Factory
    interface AppComponentFactory {
        fun create(homeComponent: HomeComponent): AppComponent
    }

    fun inject(mainActivity: MainActivity)

}