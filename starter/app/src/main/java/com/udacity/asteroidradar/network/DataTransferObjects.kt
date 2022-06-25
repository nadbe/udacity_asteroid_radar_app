package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates

data class NearEarthObject(
    @Json(name = "near_earth_objects")
    val nearEarthObject: Map<String, List<AsteroidObject>>
)

data class AsteroidObject(
    val id: Long,
    @Json(name = "name")
    val codename: String,
    @Json(name = "absolute_magnitude_h")
    val absoluteMagnitude: Double,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: AsteroidDiameter,
    @Json(name = "close_approach_data")
    val approachData: List<ApproachData>,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardous: Boolean
)

data class AsteroidDiameter(
    @Json(name = "kilometers")
    val estimatedDiameterKilometers: AsteroidDiameterKilometers
)


data class AsteroidDiameterKilometers(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterKilometersMax: Double
)


data class ApproachData(
    @Json(name = "close_approach_date")
    val closeApproachDate: String,
    @Json(name = "relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @Json(name = "miss_distance")
    val missDistanceFromEarth: MissDistance
)


data class RelativeVelocity(
    @Json(name = "kilometers_per_second")
    val kilometersPerSecond: String
)


data class MissDistance(
    val astronomical: String
)


fun NearEarthObject.asDomainModel(): List<Asteroid> {

    var list: MutableList<Asteroid> = mutableListOf()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {

        var asteroidsDayList: List<AsteroidObject> = nearEarthObject.get(formattedDate) ?: listOf()

        list.addAll(
            asteroidsDayList.map {
                Asteroid(
                id = it.id,
                codename = it.codename,
                closeApproachDate = it.approachData[0].closeApproachDate,
                absoluteMagnitude = it.absoluteMagnitude,
                estimatedDiameter = it.estimatedDiameter.estimatedDiameterKilometers.estimatedDiameterKilometersMax,
                relativeVelocity = it.approachData[0].relativeVelocity.kilometersPerSecond.toDouble(),
                distanceFromEarth = it.approachData[0].missDistanceFromEarth.astronomical.toDouble(),
                isPotentiallyHazardous = it.isPotentiallyHazardous)
        })
    }
    return list
}