package com.nemesis.core.impl

import com.nemesis.core.api.StringServer
import com.nemesis.core.di.StringServerResponse
import javax.inject.Inject

internal class StringServerImpl @Inject constructor(@StringServerResponse private val response: String) :
    StringServer {

    override fun getString() = response

}