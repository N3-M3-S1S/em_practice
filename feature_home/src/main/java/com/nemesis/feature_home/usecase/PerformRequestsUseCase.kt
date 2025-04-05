package com.nemesis.feature_home.usecase

import com.nemesis.core.api.IntServer
import com.nemesis.core.api.StringServer
import com.nemesis.feature_home.api.Response
import com.nemesis.feature_home.di.HomeScope
import javax.inject.Inject

@HomeScope
class PerformRequestsUseCase @Inject constructor(
    private val stringServer: StringServer,
    private val intServer: IntServer
) {

    operator fun invoke(): Response = Response(s = stringServer.getString(), i = intServer.getInt())

}