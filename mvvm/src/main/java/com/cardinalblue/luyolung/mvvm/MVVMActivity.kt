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
import com.cardinalblue.luyolung.repository.util.ArticleConverter
import com.cardinalblue.luyolung.ui.ArticleAdapter
import io.reactivex.disposables.CompositeDisposable
import androidx.recyclerview.widget.DividerItemDecoration
import com.cardinalblue.luyolung.mvvm.first.ArticleListViewModel
import com.cardinalblue.luyolung.ui.ArticleContentView
import kotlinx.android.synthetic.main.activity_mvvm.*


class MVVMActivity : AppCompatActivity(), ArticleAdapter.ItemClickListener {

    private lateinit var contentView: ArticleContentView
    private lateinit var articleListView: RecyclerView
    private lateinit var guideline: Guideline
    private lateinit var layout: ConstraintLayout

    private val viewData: MutableList<Article> = mutableListOf()
    private lateinit var adapter: ArticleAdapter

    private lateinit var articleListViewModel: ArticleListViewModel

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

        articleListViewModel = ViewModelProviders.of(this).get(ArticleListViewModel::class.java)

        adapter  = ArticleAdapter(this, viewData)
        adapter.setClickListener(this)
        articleListView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(articleListView.context, RecyclerView.VERTICAL)
        articleListView.addItemDecoration(dividerItemDecoration)

        articleListViewModel.allArticles.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let {
                adapter.setData(it)
                adapter.notifyDataSetChanged()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        disposableBag.dispose()
    }

//    private fun subscribeUseCases() {
//        // Add article.
//        RxView.clicks(add_article_btn)
//            .subscribe {
//                val article = Article(null, "2", "3", "廢文", -10, "4", "5")
//                controller.createNewArticle(article)
//            }.addTo(disposableBag)
//
//        // Back from article content.
//        RxView.clicks(back_btn)
//            .subscribe {
//                controller.backFromArticle()
//            }.addTo(disposableBag)
//    }

    // View behavior.
    fun onUpdate(articles: MutableList<Article>) {
        viewData.clear()
        viewData.addAll(articles)
        adapter.notifyDataSetChanged()
    }

    fun showArticleContent(article: Article) {

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

    fun hideArticleContent() {

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
        println("")
//        controller.selectArticle(article)
    }

    // Another UI layer behavior.
    private fun getDefaultArticle(): List<Article> {
        val resource = resources.openRawResource(R.raw.raw_data)
            .bufferedReader()
            .use { input -> input.readText() }

        return ArticleConverter.stringToArticleData(resource)
    }
}
