package com.example.testapprr.model

import androidx.room.*

@Dao
interface DaoInterfaceNoteData {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNoteData(AudioData: Note): Long

    @Update
    fun updateNoteData(NoteData: Note)

    @Delete
    fun deleteNoteData(NoteData: Note)

    @Query("SELECT * FROM Note WHERE isRemoved = 0")
    fun getNotRemovedNoteDataList(): MutableList<Note>

    @Query("SELECT * FROM Note")
    fun getAllNoteDataList(): MutableList<Note>


}