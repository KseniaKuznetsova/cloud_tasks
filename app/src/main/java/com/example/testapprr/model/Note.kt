package com.example.testapprr.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Note")
data class Note(

    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "dateChange") var dateChange: Long = 0,
    @ColumnInfo(name = "textNote") var textNote: String = "",
    @ColumnInfo(name = "dateNote") var dateNote: String = "",
    @ColumnInfo(name = "nameNote") var nameNote: String = "",
    @ColumnInfo(name = "isCompleted") var isCompleted: Boolean = false,
    @ColumnInfo(name = "isRemoved") var isRemoved: Boolean = false
) {
    fun toMap(): MutableMap<String, Any?> {
        val mutableMap = mutableMapOf<String, Any?>()
        mutableMap["id"] = id.toString()
        mutableMap["dateChange"] = dateChange
        mutableMap["textNote"] = textNote
        mutableMap["dateNote"] = dateNote
        mutableMap["nameNote"] = nameNote
        mutableMap["isCompleted"] = isCompleted
        mutableMap["isRemoved"] = isRemoved
        return mutableMap
    }
}