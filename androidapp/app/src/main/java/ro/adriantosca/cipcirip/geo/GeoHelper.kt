package ro.adriantosca.cipcirip.geo

object GeoHelper {
    private const val EARTH_RADIUS_METERS = 6_371_000.0

    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val distLat = Math.toRadians(lat2 - lat1)
        val distLon = Math.toRadians(lng2 - lng1)
        val halfDistLat = distLat / 2
        val halfDistLon = distLon / 2
        val a = Math.sin(halfDistLat) * Math.sin(halfDistLat) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(halfDistLon) * Math.sin(halfDistLon)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return EARTH_RADIUS_METERS * c
    }

    fun calculateFudge(latitude: Double) = Math.pow(Math.cos(Math.toRadians(latitude)), 2.0)
}