package com.cardinalblue.luyolung.architecturepattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC1
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC2
import com.cardinalblue.luyolung.architecturepattern.UseCaseAdapter.ItemClickListener
import com.cardinalblue.luyolung.mvc.MVCActivity

class MainActivity : AppCompatActivity(), ItemClickListener {

    private var adapter: UseCaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.item_list)


        val useCases = mutableListOf<UseCase>()
        useCases.add(MVC1)

        // set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UseCaseAdapter(this, useCases)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, useCase: UseCase) {
        when (useCase) {
            MVC1 -> {
                val intent = Intent(this, MVCActivity::class.java)
                this.startActivity(intent)
            }
            MVC2 -> {

            }
        }
    }
}