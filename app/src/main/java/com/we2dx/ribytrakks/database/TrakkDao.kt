package com.we2dx.ribytrakks.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrakkDao {
    @Query("SELECT * FROM trakks")
    fun getAllTrakks(): LiveData<List<TrakkDao>>


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateData(vararg trakkData: TrakkData)

    @Insert
    fun insertTrakk(vararg trakkData: TrakkData)

    @Delete
    fun delete(trakkData: TrakkData)
}