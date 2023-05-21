package org.d3if3090.carwash.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.d3if3090.carwash.model.HasilCarwash


@Dao
interface HistoryDao {
    @Insert
    fun insert(history: HistoryEntity)

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getLastHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history ORDER BY id DESC LIMIT 1")
    fun getLastHistoryData(): LiveData<HistoryEntity>

    @Query("DELETE FROM history")
    fun deleteAllHistory()

    @Query("DELETE FROM history WHERE id = :id")
    fun deleteHistory(id: Long)

    @Query("SELECT * FROM history WHERE id = :id")
    fun getHistoryById(id: Long): LiveData<HistoryEntity>
}