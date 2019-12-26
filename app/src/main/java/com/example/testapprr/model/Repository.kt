package com.example.testapprr.model

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Repository {

    lateinit var context: Context

    fun initRepository(context: Context) {
        Repository.context = context
    }

    fun insertNoteData(NoteData: Note):Long? =
        AppDatabase.getAppDataBase(context)?.NoteData()?.insertNoteData(NoteData)


    fun updateNoteData(NoteData: Note) {
        AppDatabase.getAppDataBase(context)?.NoteData()?.updateNoteData(NoteData)
    }

    fun deleteNoteData(NoteData: Note) {
        AppDatabase.getAppDataBase(context)?.NoteData()?.deleteNoteData(NoteData)
    }

    fun getNoteDataList() = AppDatabase.getAppDataBase(context)?.NoteData()?.getNotRemovedNoteDataList()
    fun getAllNoteDataList() = AppDatabase.getAppDataBase(context)?.NoteData()?.getAllNoteDataList()
}
