package com.cardinalblue.luyolung.mvc.second

import com.cardinalblue.luyolung.repository.model.Article

interface ViewContract {
    interface ArticleView {
        fun onUpdate(articles: MutableList<Article>)
        fun showArticleContent(article: Article)
        fun hideArticleContent()
    }
}