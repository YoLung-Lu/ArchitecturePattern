package com.cardinalblue.luyolung.mvc.third

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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


class MVCActiveRepositoryActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var articleView: ArticleView

    private lateinit var adapter: ArticleAdapter

    private lateinit var controller: MVCActiveRepositoryController

    private lateinit var repository: RunTimeActiveRepository

    // Subjects.
    private val onArticleListChanged: PublishSubject<Unit> = PublishSubject.create()
    private val onSelectedArticleChanged: PublishSubject<Unit> = PublishSubject.create()
    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)
        this.title = "MVC3"

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
        // Need to happen before controller start to modify repository.
        subscribeDataChanged()

        // Controller and Use cases.
        controller = MVCActiveRepositoryController(repository)
        subscribeUseCases(controller)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

    private fun subscribeUseCases(controller: MVCActiveRepositoryController) {
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
        // Article list changed.
        onArticleListChanged
            .subscribe {
                onUpdate(repository.getArticles())
            }.addTo(disposableBag)

        // Selected article changed.
        onSelectedArticleChanged
            .subscribe {
                val article = repository.getSelectedArticle()
                articleView.showArticle(article)
                back_btn.visibility =
                    if (article == null) View.INVISIBLE
                    else View.VISIBLE
            }.addTo(disposableBag)
    }

    // View behavior.
    private fun onUpdate(articles: List<Article>) {
        adapter.setData(articles)
        adapter.notifyDataSetChanged()
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
