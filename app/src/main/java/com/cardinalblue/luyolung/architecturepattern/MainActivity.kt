package com.cardinalblue.luyolung.architecturepattern

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC2
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVC3
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVP1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVP2
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVVM1
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVVM2
import com.cardinalblue.luyolung.architecturepattern.UseCase.MVVM3
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.architecturepattern.UseCaseAdapter.ItemClickListener
import com.cardinalblue.luyolung.mvc.first.AllInOneActivity
import com.cardinalblue.luyolung.mvc.second.MVCActivity
import com.cardinalblue.luyolung.mvc.third.MVCActiveRepositoryActivity
import com.cardinalblue.luyolung.mvp.first.MVPActivity
import com.cardinalblue.luyolung.mvp.second.MVPArticleViewActivity
import com.cardinalblue.luyolung.mvvm.first.MVVMActivity
import com.cardinalblue.luyolung.mvvm.second.MVVMRXActivity
import com.cardinalblue.luyolung.mvvm.third.MVVMWrapRXActivity

class MainActivity : AppCompatActivity(), ItemClickListener {

    private var adapter: UseCaseAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.title = "Architecture Pattern"

        val recyclerView = findViewById<RecyclerView>(R.id.item_list)

        val useCases = mutableListOf<UseCase>()
        useCases.add(MVC1)
        useCases.add(MVC2)
        useCases.add(MVC3)
        useCases.add(MVP1)
        useCases.add(MVP2)
        useCases.add(MVVM1)
        useCases.add(MVVM2)
        useCases.add(MVVM3)

        // set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UseCaseAdapter(this, useCases)
        adapter!!.setClickListener(this)
        recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, useCase: UseCase) {
        val navigateIntent: Intent? =
            when (useCase) {
                MVC1 -> Intent(this, AllInOneActivity::class.java)
                MVC2 -> Intent(this, MVCActivity::class.java)
                MVC3 -> Intent(this, MVCActiveRepositoryActivity::class.java)
                MVP1 -> Intent(this, MVPActivity::class.java)
                MVP2 -> Intent(this, MVPArticleViewActivity::class.java)
                MVVM1 -> Intent(this, MVVMActivity::class.java)
                MVVM2 -> Intent(this, MVVMRXActivity::class.java)
                MVVM3 -> Intent(this, MVVMWrapRXActivity::class.java)
                else -> null
            }

        navigateIntent?.let {
            this.startActivity(it)
        }
    }
}