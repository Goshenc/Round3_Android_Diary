package com.example.diary_final.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.diary_final.room.DiaryEntity
import com.example.diary_final.room.DiaryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val diaryDao = DiaryDatabase.getInstance(application).diaryDao()
    private val _diaryEntity = MutableLiveData<DiaryEntity?>()
    val diaryEntity: LiveData<DiaryEntity?> get() = _diaryEntity

    fun loadDiaryDetails(diaryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val diary = diaryDao.getDiaryById(diaryId)
            _diaryEntity.postValue(diary) // 更新 LiveData，通知 UI 更新
        }
    }
}
