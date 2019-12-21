package com.we2dx.ribytrakks.database

import androidx.room.*
import com.we2dx.ribytrakks.database.TrakkData

@Dao
interface TrakkDao {
    @Query("SELECT * FROM trakks")
    fun getAllTrakks(): List<TrakkData>


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateData(vararg trakkData: TrakkData)

    @Insert
    fun insertTrakk(vararg trakkData: TrakkData)

    @Delete
    fun delete(trakkData: TrakkData)
}