package com.cardinalblue.luyolung.mvvm.second

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cardinalblue.luyolung.mvvm.R
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.cardinalblue.luyolung.ui.ArticleAdapter
import com.cardinalblue.luyolung.ui.ArticleView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_mvvm.*


class MVVMRXActivity : AppCompatActivity() {

    private lateinit var articleView: ArticleView

    private lateinit var adapter: ArticleAdapter

    private lateinit var articleListViewModel: ArticleListRXViewModel
    private lateinit var articleViewModel: ArticleRXViewModel

    private val clickedArticle: PublishSubject<Article> = PublishSubject.create()

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)

        // View.
        articleView = findViewById(R.id.article_view)

        adapter = ArticleAdapter(this, mutableListOf())
        adapter.setClickSubject(clickedArticle)
        articleView.setAdapter(adapter)

        // View model.
        articleListViewModel = ArticleListRXViewModel(getDefaultArticle())
        articleViewModel = ArticleRXViewModel()

        subscribeUseCases()
        subscribeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

    private fun subscribeUseCases() {
        // Add article.
        RxView.clicks(add_article_btn)
            .subscribe {
                val article = ArticleGenerator.randomArticle()
                articleListViewModel.add(article)
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                articleViewModel.clear()
            }.addTo(disposableBag)

        // Clicked article.
        clickedArticle
            .subscribe { article ->
                articleViewModel.setArticle(article)
            }.addTo(disposableBag)
    }

    private fun subscribeViewModel() {
        // Change of article list.
        articleListViewModel.articleListSubject
            .subscribe {
                onUpdate(it)
            }.addTo(disposableBag)

        // Change of viewing article.
        articleViewModel.articleSubject
            .subscribe { article ->
                if (!article.isEmpty) {
                    articleView.showArticleContent(article.value!!)
                } else {
                    articleView.hideArticleContent()
                }
            }.addTo(disposableBag)

        // Back button's visibility.
        articleViewModel.articleSubject
            .subscribe { article ->
                back_btn.visibility =
                    if (!article.isEmpty) View.VISIBLE
                    else View.VISIBLE
            }.addTo(disposableBag)
    }

    // View behavior.
    private fun onUpdate(articles: List<Article>) {
        adapter.setData(articles)
        adapter.notifyDataSetChanged()
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
