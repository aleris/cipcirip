package ro.adriantosca.cipcirip.geo

import kotlin.math.*

object GeoHelper {
    private const val EARTH_RADIUS_METERS = 6_371_000.0

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val distLat = Math.toRadians(lat2 - lat1)
        val distLon = Math.toRadians(lng2 - lng1)
        val halfDistLat = distLat / 2
        val halfDistLon = distLon / 2
        val a = sin(halfDistLat) * sin(halfDistLat) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(halfDistLon) * sin(halfDistLon)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }

    fun calculateFudge(latitude: Double) = cos(Math.toRadians(latitude)).pow(2.0)
}