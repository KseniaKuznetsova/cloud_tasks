package com.example.testapprr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testapprr.R
import com.example.testapprr.model.Repository
import com.example.testapprr.ui.MainActivity
import com.example.testapprr.ui.elements.AdapterMain
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var adapterMain: AdapterMain? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainActivity = activity as MainActivity
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fabAdd.setOnClickListener {
            mainActivity.replaceFragment(ItemFragment(null, mainActivity.listNote))
        }
        fabUpdate.setOnClickListener {
            fabUpdate.animate().rotation(360 * 4f).setDuration(4 * 1000)
                .withEndAction { it.rotation = 0f }.start()
            mainActivity.syncNoteDate(listenerSuccess = {
                mainActivity.listNote = it
                updateRecycler()
                Snackbar.make(recycler_view, "Данные обновлены", Snackbar.LENGTH_SHORT).show()
            }, listenerFailed = {
                Snackbar.make(recycler_view, "Ошибка обновления", Snackbar.LENGTH_SHORT).show()
            })
        }

        adapterMain = AdapterMain(mainActivity.listNote, mainActivity)
        recycler_view.adapter = adapterMain
    }

    override fun onResume() {
        super.onResume()
        updateRecycler()
    }

    private fun updateRecycler() {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                mainActivity.listNote = Repository.getNoteDataList()!!
                mainActivity.listNote.sortBy { -it.dateChange }
                adapterMain!!.data = mainActivity.listNote
            }
            adapterMain!!.notifyDataSetChanged()
        }
    }

}
