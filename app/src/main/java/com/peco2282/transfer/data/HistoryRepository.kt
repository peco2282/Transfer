package com.peco2282.transfer.data

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val historyDao: HistoryDao) {
  val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory()

  suspend fun insert(history: HistoryEntity) {
    historyDao.insert(history)
  }

  suspend fun deleteAll() {
    historyDao.deleteAll()
  }
}
