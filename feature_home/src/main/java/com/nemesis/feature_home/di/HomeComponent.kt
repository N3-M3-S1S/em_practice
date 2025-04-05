package com.nemesis.feature_home.di

import com.nemesis.core.di.CoreComponent
import com.nemesis.feature_home.usecase.PerformRequestsUseCase
import dagger.Component

@HomeScope
@Component(dependencies = [CoreComponent::class])
interface HomeComponent {

    @Component.Factory
    interface HomeComponentFactory {
        fun create(coreComponent: CoreComponent): HomeComponent // use component here for simplicity, could be some kind of HomeDependencyProvider interface
    }

    fun getPerformRequestUseCase(): PerformRequestsUseCase

}