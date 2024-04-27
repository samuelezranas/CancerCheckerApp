package com.dicoding.asclepius.view.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.AppDatabase
import com.dicoding.asclepius.data.local.ClassificationResult
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.util.ViewModelFactory
import com.dicoding.asclepius.view.main.MainActivity
import com.dicoding.asclepius.view.news.NewsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var predictionList: MutableList<AppDatabase> = mutableListOf()
    private lateinit var predictionRecyclerView: RecyclerView
    private lateinit var textNotFound: TextView
    private val viewModel: HistoryViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.menuBar)
        textNotFound = findViewById(R.id.textNotFound)

        bottomNavigationView.selectedItemId = R.id.history_menu
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_menu -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.news_menu -> {
                    startActivity(Intent(this, NewsActivity::class.java))
                    true
                }
                R.id.history_menu -> {
                    true
                }
                else -> false
            }
        }

        textNotFound = findViewById(R.id.textNotFound)

        setupRecyclerView()
        observeLiveData()

        supportActionBar?.hide()
    }

    private fun showOrHideNoHistoryText() {
        if (predictionList.isEmpty()) {
            textNotFound.visibility = View.VISIBLE
            predictionRecyclerView.visibility = View.GONE
        } else {
            textNotFound.visibility = View.GONE
            predictionRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun observeLiveData() {
        viewModel.results.observe(this) { setResults(it) }
    }

    private fun setResults(results: List<ClassificationResult>) {
        val adapter = ResultAdapter()
        adapter.submitList(results)
        binding.rvHistory.adapter = adapter
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvHistory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }
}
