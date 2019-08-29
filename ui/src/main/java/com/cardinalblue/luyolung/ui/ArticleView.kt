package com.cardinalblue.luyolung.ui

import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.model.Article

class ArticleView: ConstraintLayout {

    private lateinit var adapter: ArticleAdapter
    private var contentView: ArticleContentView
    private var articleListView: RecyclerView
    private var guideline: Guideline
    private var layout: ConstraintLayout

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.article_view, this)

        // View.
        contentView = findViewById(R.id.article_content)
        articleListView = findViewById(R.id.article_list)
        guideline = findViewById(R.id.guide_list)
        layout = findViewById(R.id.article_view_layout)

        articleListView.layoutManager = LinearLayoutManager(context)

        val dividerItemDecoration = DividerItemDecoration(articleListView.context, RecyclerView.VERTICAL)
        articleListView.addItemDecoration(dividerItemDecoration)
    }

    fun setAdapter(adapter: ArticleAdapter) {
        this.adapter = adapter

        articleListView.adapter = adapter
    }

    fun showArticle(article: Article?) {
        article?.let {
            showArticleContent(it)
        } ?:run {
            hideArticleContent()
        }
    }

    private fun showArticleContent(article: Article) {

        adapter.hideDetail()

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
}
