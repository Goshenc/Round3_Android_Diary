package com.example.diary_final.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.diary_final.room.DiaryEntity
import com.example.diary_final.room.DiaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val diaryDao = DiaryDatabase.getInstance(application).diaryDao()

    val allDiaries: LiveData<List<DiaryEntity>> = diaryDao.getDiaries()

    fun deleteDiary(diaryEntity: DiaryEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryDao.deleteDiary(diaryEntity)
        }
    }

    fun searchDiaries(query: String): LiveData<List<DiaryEntity>> {
        return diaryDao.searchDiaries("%$query%")
    }
}
