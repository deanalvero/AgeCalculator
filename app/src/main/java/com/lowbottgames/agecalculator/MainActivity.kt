package com.lowbottgames.agecalculator

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.lowbottgames.agecalculator.adapter.AgeListAdapter
import com.lowbottgames.agecalculator.database.PersonModel
import com.lowbottgames.agecalculator.dialog.InputDialogFragment
import com.lowbottgames.agecalculator.viewmodel.AgeCalculatorViewModel
import com.lowbottgames.agecalculator.viewmodel.AgeCalculatorViewModelFactory

class MainActivity : AppCompatActivity(), InputDialogFragment.DPFOnDateSetListener {

    private lateinit var viewModel: AgeCalculatorViewModel
    private lateinit var ageListAdapter: AgeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, AgeCalculatorViewModelFactory(application)).get(AgeCalculatorViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        ageListAdapter = AgeListAdapter()
        ageListAdapter.listener = object : AgeListAdapter.AgeListAdapterListener {

            override fun onItemClick(personModel: PersonModel) {
                val intent = Intent(this@MainActivity, InfoActivity::class.java)
                intent.putExtra(InfoActivity.KEY_ID, personModel.id)

                startActivity(intent)
            }
        }
        viewModel.persons.observe(this, Observer {
            ageListAdapter.items = it
            ageListAdapter.notifyDataSetChanged()
        })

        recyclerView.adapter = ageListAdapter

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val collapsingToolbarLayout: CollapsingToolbarLayout = findViewById(R.id.collapsingToolbar)
        collapsingToolbarLayout.title = getString(R.string.app_name)

        findViewById<ExtendedFloatingActionButton>(R.id.extendedFloatingActionButton).setOnClickListener {
            showInputFragment()
        }

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            ageListAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                ageListAdapter.notifyDataSetChanged()
                true
            }
            R.id.action_add_person -> {
                showInputFragment()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showInputFragment() {
        val fragment = InputDialogFragment.newInstance()
        fragment.show(supportFragmentManager, "InputDialogFragment")
    }

    override fun onInputSet(name: String, year: Int, month: Int, day: Int, hour: Int, minute: Int) {
        val person = PersonModel(
            name = name,
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute
        )
        viewModel.insert(person)
    }
}