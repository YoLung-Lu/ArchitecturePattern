package com.cardinalblue.luyolung.mvp.second

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.ui.ArticleAdapter
import io.reactivex.disposables.CompositeDisposable
import com.cardinalblue.luyolung.mvp.R
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.cardinalblue.luyolung.ui.ArticleView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvp.add_article_btn
import kotlinx.android.synthetic.main.activity_mvp2.*

class MVPArticleViewActivity : AppCompatActivity(), MVPContract2.ArticleView, ArticleAdapter.ItemClickListener {

    private lateinit var articleView: ArticleView

    private lateinit var presenter: MVPPresenter2

    private lateinit var adapter: ArticleAdapter
    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp2)

        // View.
        articleView = findViewById(R.id.article_view)

        adapter  = ArticleAdapter(this, mutableListOf())
        adapter.setClickListener(this)
        articleView.setAdapter(adapter)

        // Repository.
        val repository = SharePrefRepository()
        repository.setDefaultArticle(getDefaultArticle())

        // Presenter and Use cases.
        presenter = MVPPresenter2(repository, this)
        subscribeUseCases(presenter)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

    private fun subscribeUseCases(presenter: MVPPresenter2) {
        // Add article.
        RxView.clicks(add_article_btn)
            .subscribe {
                val article = ArticleGenerator.randomArticle()
                presenter.createNewArticle(article)
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                presenter.onBackClicked()
            }.addTo(disposableBag)
    }

    // View behavior.
    override fun onUpdate(articles: List<Article>) {
        adapter.setData(articles)
        adapter.notifyDataSetChanged()
    }

    override fun showArticleContent(article: Article) {

        back_btn.visibility = View.VISIBLE

        articleView.showArticleContent(article)
    }

    override fun hideArticleContent() {

        back_btn.visibility = View.INVISIBLE

        articleView.hideArticleContent()
    }

    override fun onItemClick(view: View, article: Article) {
        presenter.onArticleClicked(article)
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
