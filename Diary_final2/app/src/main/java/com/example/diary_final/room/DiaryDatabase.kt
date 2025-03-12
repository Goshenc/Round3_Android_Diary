package com.example.diary_final.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [DiaryEntity::class], version = 4, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        @Volatile
        private var instance: DiaryDatabase? = null

        fun getInstance(context: Context): DiaryDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): DiaryDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                DiaryDatabase::class.java,
                "diaries database"
            )
                .fallbackToDestructiveMigration() // 避免数据库版本变更崩溃
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 这里可以初始化数据，例如插入默认日记
                    }
                })
                .build()
        }
    }
}
