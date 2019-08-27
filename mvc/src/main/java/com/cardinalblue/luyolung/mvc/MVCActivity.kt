package com.cardinalblue.luyolung.mvc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.ui.ArticleAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvc.*
import androidx.recyclerview.widget.DividerItemDecoration



class MVCActivity : AppCompatActivity(), ViewContract.ArticleView, ArticleAdapter.ItemClickListener {

    private val viewData: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter
    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)

        // View.
        val recyclerView = findViewById<RecyclerView>(R.id.article_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter  = ArticleAdapter(this, viewData)
        adapter.setClickListener(this)
        recyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)

        // Repository.
        val repository = SharePrefRepository()
        repository.setDefaultArticle(getDefaultArticle())

        // Controller and Use cases.
        val controller = MVCController(repository, this)
        subscribeUseCases(controller)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

    private fun subscribeUseCases(controller: MVCController) {
        // Add article.
        RxView.clicks(add_article_btn)
            .subscribe {
                val article = Article(null, "2", "3", "4", "5")
                controller.createNewArticle(article)
            }.addTo(disposableBag)
    }

    // View behavior.
    override fun onUpdate(articles: MutableList<Article>) {
        viewData.clear()
        viewData.addAll(articles)
        adapter.notifyDataSetChanged()
    }

    override fun onItemClick(view: View, article: Article) {
        println(article.toString())
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
