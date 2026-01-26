package com.peco2282.transfer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
  @Query("SELECT * FROM url_history ORDER BY timestamp DESC")
  fun getAllHistory(): Flow<List<HistoryEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(history: HistoryEntity)

  @Query("DELETE FROM url_history")
  suspend fun deleteAll()
}
