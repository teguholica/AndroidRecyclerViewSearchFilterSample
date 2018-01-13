package com.teguholica.androidrecyclerviewsearchfilter

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.android.volley.toolbox.JsonArrayRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.os.Build
import android.view.View


class MainActivity : AppCompatActivity() {

    private lateinit var contactAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Contacts"

        contactAdapter = ContactsAdapter({
            Toast.makeText(this, "Name : ${it.name}", Toast.LENGTH_LONG).show()
        })

        with(vList) {
            whiteNotificationBar(this)
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST, 36))
            adapter = contactAdapter
        }

        fetchData()

    }

    private fun fetchData() {
        val request = JsonArrayRequest("https://api.androidhive.info/json/contacts.json", {
            if (it == null) {
                Toast.makeText(applicationContext, "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show()
            } else {
                val items = Gson().fromJson<List<Contact>>(it.toString(), object : TypeToken<List<Contact>>() {}.type)
                contactAdapter.add(items)
                contactAdapter.notifyDataSetChanged()
            }
        }, {
            Toast.makeText(applicationContext, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        })

        App.instance.addToRequestQueue(request)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                contactAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contactAdapter.filter.filter(newText)
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.action_search -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun whiteNotificationBar(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val flags = view.systemUiVisibility
            view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }
}
