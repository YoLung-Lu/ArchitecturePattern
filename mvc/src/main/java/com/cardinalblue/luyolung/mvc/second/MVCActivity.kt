package com.cardinalblue.luyolung.mvc.second

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.ui.ArticleAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvc.*
import com.cardinalblue.luyolung.mvc.R
import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeActiveRepository
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.cardinalblue.luyolung.ui.ArticleView
import io.reactivex.subjects.PublishSubject


class MVCActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var articleView: ArticleView

    private lateinit var adapter: ArticleAdapter

    private lateinit var controller: MVCController

    private lateinit var repository: RunTimeActiveRepository

    private val onArticleListChanged: PublishSubject<Unit> = PublishSubject.create()
    private val onSelectedArticleChanged: PublishSubject<Unit> = PublishSubject.create()
    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)

        // View.
        articleView = findViewById(R.id.article_view)

        // Adapter.
        adapter = ArticleAdapter(this, mutableListOf())
        adapter.setClickListener(this)
        articleView.setAdapter(adapter)

        // Repository.
        repository = RunTimeActiveRepository()
        repository.setDefaultArticle(getDefaultArticle())
        repository.addArticleListChangedSubscriber(onArticleListChanged)
        repository.addArticleChangedSubscriber(onSelectedArticleChanged)

        // View scribe to model.
        subscribeDataChanged()

        // Controller and Use cases.
        controller = MVCController(repository, this)
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
                val article = ArticleGenerator.randomArticle()
                controller.createNewArticle(article)
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                controller.backFromArticle()
            }.addTo(disposableBag)
    }

    private fun subscribeDataChanged() {
        onArticleListChanged.subscribe {
            onUpdate(repository.getArticles())
        }.addTo(disposableBag)

        onSelectedArticleChanged.subscribe {
            val article = repository.getSelectedArticle()

            if (article != null) {
                showArticleContent(article)
            } else {
                hideArticleContent()
            }
        }.addTo(disposableBag)
    }

    // View behavior.
    fun onUpdate(articles: List<Article>) {
        adapter.setData(articles)
        adapter.notifyDataSetChanged()
    }

    private fun showArticleContent(article: Article) {

        back_btn.visibility = View.VISIBLE

        articleView.showArticleContent(article)
    }

    private fun hideArticleContent() {

        back_btn.visibility = View.INVISIBLE

        articleView.hideArticleContent()
    }

    override fun onItemClick(view: View, article: Article) {
        controller.selectArticle(article)
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
