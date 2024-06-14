package com.nafi.airseat.presentation.blank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nafi.airseat.R

class BlankActivity : AppCompatActivity() {
    private var arrayList = ArrayList<BlankModel>()
    private var data = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blank)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val myAdapter = BlankAdapter(this, getData())
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL,
            ),
        )
        recyclerView.adapter = myAdapter
    }

    private fun getData(): ArrayList<BlankModel> {
        for (i in data.indices) {
            arrayList.add(BlankModel(data[i], false))
        }
        return arrayList
    }

    companion object {
        const val EXTRAS_ITEM = "EXTRAS_ITEM"

        fun startActivity(
            context: Context,
            id: String,
        ) {
            val intent = Intent(context, BlankActivity::class.java)
            intent.putExtra(EXTRAS_ITEM, id)
            context.startActivity(intent)
        }
    }
}
