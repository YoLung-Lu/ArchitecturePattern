package com.cardinalblue.luyolung.mvvm.third

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


class MVVMWrapRXActivity : AppCompatActivity() {

    private lateinit var articleView: ArticleView

    private lateinit var adapter: ArticleAdapter

    private lateinit var articleListViewModel: ArticleListViewModel

    private val clickedArticle: PublishSubject<Article> = PublishSubject.create()

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)
        this.title = "MVVM3"

        // View.
        articleView = findViewById(R.id.article_view)

        adapter = ArticleAdapter(this, mutableListOf())
        adapter.setClickSubject(clickedArticle)
        articleView.setAdapter(adapter)

        // View model.
        articleListViewModel = ArticleListViewModel(getDefaultArticle())

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
                articleListViewModel.generateNewArticle()
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                articleListViewModel.setSelectedArticle(null)
            }.addTo(disposableBag)

        // Clicked article.
        clickedArticle
            .subscribe { article ->
                articleListViewModel.setSelectedArticle(article)
            }.addTo(disposableBag)
    }

    private fun subscribeViewModel() {
        // Change of article list.
        articleListViewModel.articleListSubject
            .subscribe {
                onUpdate(it)
            }.addTo(disposableBag)

        // Change of viewing article.
        articleListViewModel.selectedArticleSubject
            .subscribe { optionalArticle ->
                articleView.showArticle(optionalArticle.value)
            }.addTo(disposableBag)

        // Back button's visibility.
        articleListViewModel.showArticleSubject
            .subscribe { showArticle ->
                back_btn.visibility =
                    if (showArticle) View.VISIBLE
                    else View.INVISIBLE
            }.addTo(disposableBag)
    }

    // An implementation that acquire all information related to selection
    // from the articleListSubject
    private fun subscribeViewModel2() {
        // Change of article list.
        articleListViewModel.articleListSubject
            .subscribe {
                onUpdate(it)
            }.addTo(disposableBag)

        // Change of viewing article.
        articleListViewModel.articleListSubject
            .map { articles ->
                articles.filter { it.selected }
            }
            .subscribe { articleList ->
                if (articleList.isEmpty()) {
                    articleView.showArticle(null)
                } else {
                    articleView.showArticle(articleList.first().article)
                }
            }.addTo(disposableBag)

        // Back button's visibility.
        articleListViewModel.articleListSubject
            .map { articles ->
                articles.filter { it.selected }
            }
            .subscribe { articleList ->
                back_btn.visibility =
                    if (articleList.isNotEmpty()) View.VISIBLE
                    else View.INVISIBLE
            }.addTo(disposableBag)
    }

    // View behavior.
    private fun onUpdate(articles: List<ArticleViewModel>) {
        val mapToArticle = articles.map { it.article }
        adapter.setData(mapToArticle)
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