package com.cardinalblue.luyolung.mvc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvc.*

class MVCActivity : AppCompatActivity(), ViewContract.ArticleView {

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)

        // Repository
        val repository = SharePrefRepository()
        repository.setDefaultArticle(getDefaultArticle())

        // Controller and Use cases
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
        text_view.text = articles.toString()
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
