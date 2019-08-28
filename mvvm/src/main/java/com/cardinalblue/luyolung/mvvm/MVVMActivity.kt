package com.cardinalblue.luyolung.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.ui.ArticleAdapter
import io.reactivex.disposables.CompositeDisposable
import androidx.recyclerview.widget.DividerItemDecoration
import com.cardinalblue.luyolung.mvvm.first.ArticleListViewModel
import com.cardinalblue.luyolung.mvvm.first.ArticleViewModel
import com.cardinalblue.luyolung.ui.ArticleContentView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvvm.*


class MVVMActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var contentView: ArticleContentView
    private lateinit var articleListView: RecyclerView
    private lateinit var guideline: Guideline
    private lateinit var layout: ConstraintLayout

    private val viewData: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter

    private lateinit var articleListViewModel: ArticleListViewModel
    private lateinit var articleViewModel: ArticleViewModel

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvvm)

        // View.
        contentView = findViewById(R.id.article_content)
        articleListView = findViewById(R.id.article_list)
        guideline = findViewById(R.id.guide_list)
        layout = findViewById(R.id.layout)
        articleListView.layoutManager = LinearLayoutManager(this)

        adapter  = ArticleAdapter(this, viewData)
        adapter.setClickListener(this)
        articleListView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(articleListView.context, RecyclerView.VERTICAL)
        articleListView.addItemDecoration(dividerItemDecoration)

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
                val article = Article(null, "2", "3", "廢文", -10, "4", "5")
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
        articleListViewModel.allArticles.observe(this, Observer { articles ->
            articles?.let {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })

        // Change of viewing article.
        articleViewModel.article().observe(this, Observer { article ->
            if (article != null) {
                showArticleContent(article)
            } else {
                // null -> hide article.
                hideArticleContent()
            }
        })
    }

    private fun showArticleContent(article: Article) {

        adapter.hideDetail()

        back_btn.visibility = View.VISIBLE

        contentView.setArticle(article)

        // Shift layout.
        val set = ConstraintSet()
        set.clone(layout)
        set.connect(
            R.id.article_list, ConstraintSet.RIGHT,
                    guideline.id, ConstraintSet.RIGHT)
        set.connect(
            R.id.article_content, ConstraintSet.LEFT,
                    guideline.id, ConstraintSet.RIGHT)
        set.applyTo(layout)

        // Animation.
        val transition = ChangeBounds()
        TransitionManager.beginDelayedTransition(layout, transition)

    }

    private fun hideArticleContent() {

        adapter.showDetail()

        back_btn.visibility = View.INVISIBLE

        // Shift layout.
        val set = ConstraintSet()
        set.clone(layout)
        set.connect(
            R.id.article_list, ConstraintSet.RIGHT,
            layout.id, ConstraintSet.RIGHT)
        set.connect(
            R.id.article_content, ConstraintSet.LEFT,
            layout.id, ConstraintSet.RIGHT)
        set.applyTo(layout)

        // Animation.
        val transition = ChangeBounds()
        TransitionManager.beginDelayedTransition(layout, transition)
    }

    override fun onItemClick(view: View, article: Article) {
        articleViewModel.setArticle(article)
    }
}
