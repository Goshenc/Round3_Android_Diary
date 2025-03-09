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
//SELECT * FROM diaries
//查询数据库中 diaries 表的所有列。
//
//WHERE title LIKE :query OR article LIKE :query
//指定条件：
//
//title LIKE :query：表示日记的标题中包含与 query 参数匹配的字符串。
//OR article LIKE :query：或日记的正文（article）中包含与 query 参数匹配的字符串。
//LIKE 操作符：允许使用 SQL 通配符（如 %），通常需要在调用方法时传入类似 %关键字% 的字符串，表示模糊搜索。
    @Query("SELECT * FROM diaries WHERE title LIKE :query OR article LIKE :query")
    fun searchDiaries(query: String): LiveData<List<DiaryEntity>>//livedata

    @Delete
    suspend fun deleteDiary(diaryEntity: DiaryEntity)



    @Query("SELECT * FROM diaries")
    fun getDiaries(): LiveData<List<DiaryEntity>>//livedata

    @Query("SELECT * FROM diaries WHERE id = :diaryId")
    suspend fun getDiaryById(diaryId: Int): DiaryEntity
}
