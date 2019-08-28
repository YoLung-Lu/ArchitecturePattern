package com.cardinalblue.luyolung.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.cardinalblue.luyolung.repository.model.Article
import kotlinx.android.synthetic.main.article_content.view.*

class ArticleContentView: ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.article_content, this)
    }

    fun setArticle(article: Article) {
        article_content_display.text = article.content
        article_category.text = article.category
        article_push.text = article.pushNum.toString()
        article_title.text = article.title
        article_id.text = article.id.toString()
        article_author.text = article.author
        article_time.text = article.publishTime

        invalidate()
    }
}
