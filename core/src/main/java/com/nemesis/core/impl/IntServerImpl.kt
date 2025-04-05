package com.nemesis.core.impl

import com.nemesis.core.api.IntServer
import javax.inject.Inject

internal class IntServerImpl @Inject constructor() : IntServer {

    override fun getInt() = 1

}