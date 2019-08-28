package com.cardinalblue.luyolung.architecturepattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC2
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVP1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVP2
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVVM1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVVM2
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.architecturepattern.UseCaseAdapter.ItemClickListener
import com.cardinalblue.luyolung.mvc.first.MVCBasicActivity
import com.cardinalblue.luyolung.mvc.second.MVCActivity
import com.cardinalblue.luyolung.mvp.first.MVPActivity
import com.cardinalblue.luyolung.mvp.second.MVPArticleViewActivity
import com.cardinalblue.luyolung.mvvm.first.MVVMActivity
import com.cardinalblue.luyolung.mvvm.second.MVVMRXActivity

class MainActivity : AppCompatActivity(), ItemClickListener {

    private var adapter: UseCaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.item_list)


        val useCases = mutableListOf<UseCase>()
        useCases.add(MVC1)
        useCases.add(MVC2)
        useCases.add(MVP1)
        useCases.add(MVP2)
        useCases.add(MVVM1)
        useCases.add(MVVM2)

        // set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UseCaseAdapter(this, useCases)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, useCase: UseCase) {
        val navigateIntent: Intent? =
            when (useCase) {
                MVC1 -> Intent(this, MVCBasicActivity::class.java)
                MVC2 -> Intent(this, MVCActivity::class.java)
                MVP1 -> Intent(this, MVPActivity::class.java)
                MVP2 -> Intent(this, MVPArticleViewActivity::class.java)
                MVVM1 -> Intent(this, MVVMActivity::class.java)
                MVVM2 -> Intent(this, MVVMRXActivity::class.java)
                else -> null
            }

        navigateIntent?.let {
            this.startActivity(it)
        }
    }
}