package com.example.diary_final.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diaries")
data class DiaryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val article: String,
    val localImagePath: String?,
    val networkImageLink: String?,
    val date: String,
    val weather: String,
    val location: String
)
