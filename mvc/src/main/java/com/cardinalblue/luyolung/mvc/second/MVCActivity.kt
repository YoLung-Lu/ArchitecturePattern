package com.cardinalblue.luyolung.mvc.second

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.ui.ArticleAdapter
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_mvc.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.cardinalblue.luyolung.mvc.R
import com.cardinalblue.luyolung.ui.ArticleContentView


class MVCActivity : AppCompatActivity(), ViewContract.ArticleView, ArticleAdapter.ItemClickListener {

    private lateinit var contentView: ArticleContentView
    private lateinit var articleListView: RecyclerView
    private lateinit var guideline: Guideline
    private lateinit var layout: ConstraintLayout

    private val viewData: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter

    private lateinit var controller: MVCController

    private val disposableBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvc)

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

        // Repository.
        val repository = SharePrefRepository()
        repository.setDefaultArticle(getDefaultArticle())

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
                val article = Article(null, "2", "3", "廢文", -10, "4", "5")
                controller.createNewArticle(article)
            }.addTo(disposableBag)

        // Back from article content.
        RxView.clicks(back_btn)
            .subscribe {
                controller.backFromArticle()
            }.addTo(disposableBag)
    }

    // View behavior.
    override fun onUpdate(articles: MutableList<Article>) {
        viewData.clear()
        viewData.addAll(articles)
        adapter.notifyDataSetChanged()
    }

    override fun showArticleContent(article: Article) {

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

    override fun hideArticleContent() {

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
