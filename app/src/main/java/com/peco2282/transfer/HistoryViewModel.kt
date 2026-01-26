package com.peco2282.transfer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.peco2282.transfer.data.AppDatabase
import com.peco2282.transfer.data.HistoryEntity
import com.peco2282.transfer.data.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: HistoryRepository
  val allHistory: Flow<List<HistoryEntity>>

  init {
    val historyDao = AppDatabase.getDatabase(application).historyDao()
    repository = HistoryRepository(historyDao)
    allHistory = repository.allHistory
  }

  fun insert(history: HistoryEntity) = viewModelScope.launch {
    repository.insert(history)
  }

  fun deleteAll() = viewModelScope.launch {
    repository.deleteAll()
  }
}
