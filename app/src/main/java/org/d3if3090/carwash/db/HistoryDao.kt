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

    @Query("DELETE FROM history")
    fun deleteAllHistory()

    @Query("DELETE FROM history WHERE id = :id")
    fun deleteHistory(id: Int)

    @Query("SELECT * FROM history WHERE id = :id")
    fun getHistoryById(id: Int): LiveData<HistoryEntity>
}