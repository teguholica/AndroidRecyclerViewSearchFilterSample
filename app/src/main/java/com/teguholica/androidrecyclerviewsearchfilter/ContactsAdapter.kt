package com.teguholica.androidrecyclerviewsearchfilter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_row_item.view.*

class ContactsAdapter(
        val listener: (contact: Contact) -> Unit
): RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {

    val contactList: MutableList<Contact> = mutableListOf()
    val contactListFiltered: MutableList<Contact> = mutableListOf()

    fun add(contacts: List<Contact>) {
        contactList.addAll(contacts)
        contactListFiltered.addAll(contacts)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.load(contactListFiltered[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = contactListFiltered.size

    override fun getFilter(): Filter = (object: Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filteredList = ArrayList<Contact>()
            if (p0.isNullOrEmpty()) {
                filteredList.addAll(contactList)
            } else {
                filteredList.addAll(contactList.filter { it.name.contains(p0.toString(), true) || it.phone.contains(p0.toString(), true) })
            }

            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            contactListFiltered.clear()
            contactListFiltered.addAll(p1?.values as Collection<Contact>)
            notifyDataSetChanged()
        }

    })

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun load(contact: Contact) {
            with(itemView) {
                name.text = contact.name
                phone.text = contact.phone

                Glide.with(itemView.context)
                        .load(contact.image)
                        .apply(RequestOptions.circleCropTransform())
                        .into(thumbnail)

                setOnClickListener { listener(contact) }
            }
        }

    }

}