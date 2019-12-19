package com.we2dx.ribytrakks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trakks")
data class TrakkData (
    @PrimaryKey(autoGenerate = true)
    var trakkID: Int? = 0,
    var start_pointLat: Float? = 0f,
    var end_pointLat: Float? = 0f,
    var start_pointLng: Float? = 0f,
    var end_pointLng: Float? = 0f
//    var distance_covered: Float? = 0f,
//    var start_time: String = "",
//    var end_time: String? = "",
//    var timeTaken: String? = "",
//    var trakkDate: String? = ""



                      ) {

    constructor() : this(0,0f,0f,0f,0f)

}