package com.example.testapprr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.testapprr.R
import com.example.testapprr.model.Note
import com.example.testapprr.model.Repository
import com.example.testapprr.ui.fragments.MainFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    var listNote = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(MainFragment())
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.main_container, fragment)
            .addToBackStack(fragment.javaClass.name)
            .commitAllowingStateLoss()
    }

    fun syncNoteDate(
        listenerSuccess: (result: MutableList<Note>) -> Unit,
        listenerFailed: () -> Unit
    ) {
        getListNoteFromFireBase(listenerSuccess = { listServer ->
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    val listLocal = Repository.getAllNoteDataList()!!
                    val resultList = mutableListOf<Note>()
                    resultList.addAll(listLocal)
                    listServer.forEachIndexed { index, serverNote ->
                        val localNote = listLocal.find { it.id == serverNote.id }
                        if (localNote == null) {
                            resultList.add(serverNote)
                            Repository.insertNoteData(serverNote)
                        } else {
                            if (serverNote.dateChange > localNote.dateChange) {
                                resultList[index] = serverNote
                                Repository.updateNoteData(serverNote)
                            }
                        }
                    }
                    listenerSuccess(resultList)
                    sendListNoteToFireBase(resultList)
                }
            }
        },
            listenerFailed = {
                listenerFailed()
            })
    }

    private fun sendListNoteToFireBase(
        date: MutableList<Note>
    ) {
        val parent = FirebaseDatabase.getInstance().reference.child("notes")
        date.forEach {
            parent.child("id" + it.id).setValue(it.toMap())
        }
    }


    private fun getListNoteFromFireBase(
        listenerSuccess: (result: MutableList<Note>) -> Unit,
        listenerFailed: () -> Unit
    ) {

        val parent = FirebaseDatabase.getInstance().reference
        parent.child("notes")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val data = dataSnapshot.value
                        println(data)
                        if (data is Map<*, *>) {
                            listenerSuccess(collectNotes(data as Map<String, *>))
                        } else {
                            listenerSuccess(mutableListOf())
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        listenerFailed()
                    }
                })
    }

    private fun collectNotes(mapNotes: Map<String, *>): MutableList<Note> {
        val listNotes = mutableListOf<Note>()
        mapNotes.forEach { (key, value) ->
            val singleRoom = value as Map<String, *>
            listNotes.add(setNotesFieldsFromMap(singleRoom))
        }
        listNotes.sortBy { it.dateChange }
        return listNotes
    }

    private fun setNotesFieldsFromMap(
        singleNote: Map<String, *>
    ): Note {
        val resultNote = Note(singleNote["id"].toString().toLong())
        if (singleNote["dateChange"] != null)
            resultNote.dateChange = singleNote["dateChange"].toString().toLong()
        if (singleNote["nameNote"] != null)
            resultNote.nameNote = singleNote["nameNote"].toString()
        if (singleNote["dateNote"] != null)
            resultNote.dateNote = singleNote["dateNote"].toString()
        if (singleNote["textNote"] != null)
            resultNote.textNote = singleNote["textNote"].toString()
        if (singleNote["isCompleted"] != null)
            resultNote.isCompleted = singleNote["isCompleted"].toString().toBoolean()
        if (singleNote["isRemoved"] != null)
            resultNote.isRemoved = singleNote["isRemoved"].toString().toBoolean()
        return resultNote
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            super.onBackPressed()
        } else {
            finish()
        }
    }


}
