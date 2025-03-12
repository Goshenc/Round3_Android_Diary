package com.example.diary_final.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryDao {
    @Insert
    suspend fun insertDiary(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM diaries WHERE title LIKE :query OR article LIKE :query")
    fun searchDiaries(query: String): LiveData<List<DiaryEntity>>

    @Delete
    suspend fun deleteDiary(diaryEntity: DiaryEntity)

    @Query("SELECT * FROM diaries")
    suspend fun getAllDiaries(): List<DiaryEntity>

    @Query("SELECT * FROM diaries")
    fun getDiaries(): LiveData<List<DiaryEntity>>

    @Query("SELECT * FROM diaries WHERE id = :diaryId")
    suspend fun getDiaryById(diaryId: Int): DiaryEntity
}
