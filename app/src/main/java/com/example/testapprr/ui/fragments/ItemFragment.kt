package com.example.testapprr.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.testapprr.R
import com.example.testapprr.convertCalendarToData
import com.example.testapprr.model.Note
import com.example.testapprr.model.Repository
import com.example.testapprr.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_add_show_item.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ItemFragment(
    var item: Note?,
    private val data: MutableList<Note>
) : Fragment() {
    private lateinit var mainActivity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity
        return inflater.inflate(R.layout.fragment_add_show_item, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (item != null) {
            nameTextInputEditText.setText(item!!.nameNote, TextView.BufferType.EDITABLE)
            dateTextInputEditText.setText(item!!.dateNote, TextView.BufferType.EDITABLE)
            descriptionTextInputEditText.setText(item!!.textNote, TextView.BufferType.EDITABLE)
            is_completed_check_box.isChecked = item!!.isCompleted
            remove_btn.visibility = View.VISIBLE
        }
        dateTextInputEditText.setOnClickListener { showCalendar() }
        dateTextInputEditText.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) showCalendar() }
        save_btn.setOnClickListener {
            if (item == null) {
                item = Note(
                    null,
                    System.currentTimeMillis(),
                    descriptionTextInputEditText.text.toString(),
                    dateTextInputEditText.text.toString(),
                    nameTextInputEditText.text.toString(),
                    is_completed_check_box.isChecked
                )
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        val id = Repository.insertNoteData(item!!)
                        item!!.id = id!!
                        data.add(item!!)
                    }
                    mainActivity.onBackPressed()
                }
            } else {
                item!!.apply {
                    dateChange = System.currentTimeMillis()
                    textNote = descriptionTextInputEditText.text.toString()
                    dateNote = dateTextInputEditText.text.toString()
                    nameNote = nameTextInputEditText.text.toString()
                    isCompleted = is_completed_check_box.isChecked
                }
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO) {
                        Repository.updateNoteData(item!!)
                    }
                    mainActivity.onBackPressed()
                }
            }
        }

        remove_btn.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    item!!.isRemoved = true
                    item!!.dateChange = System.currentTimeMillis()
                    println(item)
                    Repository.updateNoteData(item!!)
                }
                mainActivity.onBackPressed()
            }
        }

        back_btn.setOnClickListener {
            mainActivity.onBackPressed()
        }
    }

    private fun showCalendar() {
        val currentDate = Calendar.getInstance()
        val date = Calendar.getInstance()
        val dialog = DatePickerDialog(
            context!!,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                val timeDialog = TimePickerDialog(
                    context,
                    TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        dateTextInputEditText.setText(convertCalendarToData(date))
                    },
                    currentDate.get(Calendar.HOUR_OF_DAY),
                    currentDate.get(Calendar.MINUTE),
                    true
                )

                timeDialog.show()
            },
            currentDate.get(Calendar.YEAR),
            currentDate.get(Calendar.MONTH),
            currentDate.get(Calendar.DATE)
        )
        dialog.show()
    }


}
