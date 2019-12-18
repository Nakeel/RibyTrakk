package com.we2dx.ribytrakks.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrakkData (var start_point: Float? = 0f,
                      var end_point: Float? = 0f,
                      var distance_covered: Float? = 0f,
                      var start_time: String = "",
                      var end_time: String? = "",
                      var timeTaken: String? = "",
                      @PrimaryKey
                      var trakkDate: String? = ""



                      ) {

    constructor() : this(0f,0f,0f,"","","","")

}