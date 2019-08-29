package com.cardinalblue.luyolung.mvvm.first

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.cardinalblue.luyolung.mvvm.R
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.cardinalblue.luyolung.ui.ArticleAdapter
import com.cardinalblue.luyolung.ui.ArticleView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvvm.*


class MVVMActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var articleView: ArticleView

    private lateinit var adapter: ArticleAdapter

    private lateinit var articleListViewModel: ArticleListViewModel
    private lateinit var articleViewModel: ArticleViewModel

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)
        this.title = "MVVM1"

        // View.
        articleView = findViewById(R.id.article_view)

        adapter = ArticleAdapter(this, mutableListOf())
        adapter.setClickListener(this)
        articleView.setAdapter(adapter)

        // View model.
        articleListViewModel = ViewModelProviders.of(this).get(ArticleListViewModel::class.java)
        articleViewModel = ViewModelProviders.of(this).get(ArticleViewModel::class.java)

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
                articleListViewModel.insert(article)
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                articleViewModel.setArticle(null)
            }.addTo(disposableBag)
    }

    private fun subscribeViewModel() {
        // Change of article list.
        articleListViewModel.allArticles
            .observe(this, Observer { articles ->
                articles?.let {
                    adapter.setData(it)
                    adapter.notifyDataSetChanged()
                }
            })

        // Change of viewing article.
        articleViewModel.article()
            .observe(this, Observer { article ->
                articleView.showArticle(article)
                back_btn.visibility =
                    if (article != null) View.VISIBLE
                    else View.INVISIBLE
            })
    }

    override fun onItemClick(view: View, article: Article) {
        articleViewModel.setArticle(article)
    }
}
