package com.cardinalblue.luyolung.mvp.second

import com.cardinalblue.luyolung.repository.model.Article

interface ArticleContract {
    interface ArticlePresenter {
        fun onAddArticle()
        fun onArticleSelected(article: Article)
        fun onBack()
    }

    interface ArticleView {
        fun showArticles(articles: List<Article>)
        fun showArticleContent(article: Article)
        fun hideArticleContent()
    }
}