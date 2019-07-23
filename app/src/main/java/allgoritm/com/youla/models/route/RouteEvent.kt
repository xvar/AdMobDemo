package allgoritm.com.youla.models.route

import allgoritm.com.youla.models.YRouteEvent

sealed class RouteEvent: YRouteEvent {
    class Settings : RouteEvent()
    class Home: RouteEvent()
}