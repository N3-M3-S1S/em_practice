package com.nemesis.core.di

import com.nemesis.core.api.IntServer
import com.nemesis.core.api.StringServer
import dagger.BindsInstance
import dagger.Component

@CoreScope
@Component(modules = [CoreModule::class])
interface CoreComponent {

    @Component.Factory
    interface CoreComponentFactory {
        fun create(@BindsInstance @StringServerResponse stringServerResponse: String): CoreComponent
    }

    fun getStringServer(): StringServer

    fun getIntServer(): IntServer

}