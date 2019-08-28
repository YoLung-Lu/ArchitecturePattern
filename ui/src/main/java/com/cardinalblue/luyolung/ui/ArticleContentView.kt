package com.cardinalblue.luyolung.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cardinalblue.luyolung.repository.model.Article
import kotlinx.android.synthetic.main.article_content.view.*

class ArticleContentView: ConstraintLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        inflate(context, R.layout.article_content, this)

        contentView = article_content_display
    }

    private var contentView: TextView

    fun setArticle(article: Article) {
        contentView.text = article.content

        invalidate()
    }
}
