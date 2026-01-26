package com.peco2282.transfer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "url_history")
data class HistoryEntity(
  @field:PrimaryKey(autoGenerate = true)
  val id: Int = 0,
  val originalUrl: String,
  val shortenedUrl: String,
  val timestamp: Long = System.currentTimeMillis()
)
