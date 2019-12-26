package com.example.testapprr.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Note::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun NoteData(): DaoInterfaceNoteData

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    val nameDB = "myDB"
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, nameDB).build()
                }
            }
            return INSTANCE
        }
    }
}