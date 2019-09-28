package com.cardinalblue.luyolung.mvvm.second

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cardinalblue.luyolung.mvvm.R
import com.cardinalblue.luyolung.mvvm.second.model.ArticlesModel
import com.cardinalblue.luyolung.mvvm.second.model.SelectedArticleModel
import com.cardinalblue.luyolung.mvvm.second.viewmodel.ArticleListRXViewModel
import com.cardinalblue.luyolung.mvvm.second.viewmodel.BackViewModel
import com.cardinalblue.luyolung.mvvm.second.viewmodel.SelectedArticleViewModel
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
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
    private lateinit var selectedArticleViewModel: SelectedArticleViewModel
    private lateinit var backViewModel: BackViewModel

    private val clickedArticle: PublishSubject<Article> = PublishSubject.create()

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)
        this.title = "MVVM2"

        // View.
        articleView = findViewById(R.id.article_view)

        adapter = ArticleAdapter(this, mutableListOf())
        adapter.setClickSubject(clickedArticle)
        articleView.setAdapter(adapter)

        // Model.
        val articlesModel = ArticlesModel()
        val selectedArticleModel = SelectedArticleModel()

        // View model.
        articleListViewModel = ArticleListRXViewModel(articlesModel)
        selectedArticleViewModel = SelectedArticleViewModel(selectedArticleModel)
        backViewModel = BackViewModel(selectedArticleModel)

        subscribeUseCases()
        subscribeViewModel()

        // Start.
        articlesModel.setDefaultData(getDefaultArticle())
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
                selectedArticleViewModel.clear()
            }.addTo(disposableBag)

        // Clicked article.
        clickedArticle
            .subscribe { article ->
                selectedArticleViewModel.setArticle(article)
            }.addTo(disposableBag)
    }

    private fun subscribeViewModel() {
        // Change of article list.
        articleListViewModel.articleListSubject
            .subscribe {
                onUpdate(it)
            }.addTo(disposableBag)

        // Change of viewing article.
        selectedArticleViewModel.articleSubject
            .subscribe { article ->
                articleView.showArticle(article.value)
            }.addTo(disposableBag)

        // Back button's visibility.
        backViewModel.visible
            .subscribe { visible ->
                back_btn.visibility =
                    if (visible) View.VISIBLE
                    else View.INVISIBLE
            }.addTo(disposableBag)
    }

    // View behavior.
    private fun onUpdate(articles: List<Article>) {
        adapter.setData(articles)
        adapter.notifyDataSetChanged()
    }

    // IO layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
