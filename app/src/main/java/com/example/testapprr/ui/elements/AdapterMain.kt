package com.example.testapprr.ui.elements


import android.annotation.SuppressLint
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapprr.R
import com.example.testapprr.model.Note
import com.example.testapprr.model.Repository
import com.example.testapprr.ui.MainActivity
import com.example.testapprr.ui.fragments.ItemFragment
import kotlinx.android.synthetic.main.item_recycler_main.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal class AdapterMain(
    var data: MutableList<Note>,
    private val activity: MainActivity
) :
    RecyclerView.Adapter<AdapterMain.BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val viewHolder: BaseViewHolder
        val view = from(parent.context).inflate(R.layout.item_recycler_main, parent, false)
        viewHolder = BaseViewHolder(view)
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = data[position]
        val view = holder.itemView
        view.textDate.text = item.dateNote
        view.textName.text = item.nameNote
        view.switch_state.setOnCheckedChangeListener { _, _ -> }
        view.switch_state.isChecked = item.isCompleted
        view.switch_state.setOnCheckedChangeListener { _, isChecked ->
            item.isCompleted = isChecked
            item.dateChange = System.currentTimeMillis()
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    Repository.updateNoteData(item)
                }
            }
        }
        view.itemCard.setOnClickListener {
            activity.replaceFragment(ItemFragment(item, data))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    internal open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
