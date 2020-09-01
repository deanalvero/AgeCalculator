package com.lowbottgames.agecalculator.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lowbottgames.agecalculator.R
import com.lowbottgames.agecalculator.database.PersonModel
import com.lowbottgames.agecalculator.util.DataHelper
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime

class AgeListAdapter : RecyclerView.Adapter<AgeListAdapter.AgeListViewHolder>() {

    var items: List<PersonModel>? = null
    var listener: AgeListAdapterListener? = null

    class AgeListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textView_name)
        private val textViewAge: TextView = itemView.findViewById(R.id.textView_age)
        private val textViewBirthdate: TextView = itemView.findViewById(R.id.textView_birthdate)

        fun bind(item: PersonModel) {
            textViewName.text = item.name

            val birthdate = LocalDateTime(item.year, item.month + 1, item.day, item.hour, item.minute)
            textViewAge.text = DataHelper.age(birthdate)
            textViewBirthdate.text = birthdate.toString("dd MMMM YYYY")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_person, parent, false)
        return AgeListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: AgeListViewHolder, position: Int) {
        val item = items!![position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(items!![holder.adapterPosition])
        }
    }

    interface AgeListAdapterListener {
        fun onItemClick(personModel: PersonModel)
    }
}