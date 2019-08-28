package com.cardinalblue.luyolung.mvc

import com.cardinalblue.luyolung.repository.model.Article

interface ViewContract {
    interface ArticleView {
        fun onUpdate(articles: MutableList<Article>)
        fun showArticleContent(article: Article)
        fun hideArticleContent()
    }
}