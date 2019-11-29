package org.gern.coyote

import io.ktor.application.call
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respondText
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/")
class IndexPath

@KtorExperimentalLocationsAPI
fun Route.index() {
    get<IndexPath> {
        call.respondText("Hello world")
    }
}
