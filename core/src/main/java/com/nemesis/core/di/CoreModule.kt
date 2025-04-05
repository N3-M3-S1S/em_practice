package com.nemesis.core.di

import com.nemesis.core.api.IntServer
import com.nemesis.core.api.StringServer
import com.nemesis.core.impl.IntServerImpl
import com.nemesis.core.impl.StringServerImpl
import dagger.Binds
import dagger.Module

@Module
internal abstract class CoreModule {

    @CoreScope
    @Binds
    abstract fun bindsStringServer(stringServerImpl: StringServerImpl): StringServer

    @CoreScope
    @Binds
    abstract fun bindsIntServer(intServerImpl: IntServerImpl): IntServer

}