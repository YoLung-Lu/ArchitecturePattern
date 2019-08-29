package com.cardinalblue.luyolung.mvc.first

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
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.cardinalblue.luyolung.ui.ArticleView


class AllInOneActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var articleView: ArticleView

    private val viewData: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter

    private val repository = RunTimeRepository()

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)
        this.title = "MVC1"

        // View.
        articleView = findViewById(R.id.article_view)

        adapter  = ArticleAdapter(this, viewData)
        adapter.setClickListener(this)
        articleView.setAdapter(adapter)

        // Repository.
        repository.setDefaultArticle(getDefaultArticle())

        subscribeUseCases()

        setInitState()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

    // Control behavior.
    private fun subscribeUseCases() {
        // Add article.
        RxView.clicks(add_article_btn)
            .subscribe {
                val article = ArticleGenerator.randomArticle()
                repository.addArticle(article)
                onUpdate()
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                hideArticleContent()
            }.addTo(disposableBag)
    }

    // Control behavior.
    private fun setInitState() {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }

        onUpdate()
    }

    // View behavior.
    // Model being passively.
    private fun onUpdate() {
        viewData.clear()
        viewData.addAll(repository.getArticles())
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

    // Control behavior.
    override fun onItemClick(view: View, article: Article) {
        showArticleContent(article)
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
