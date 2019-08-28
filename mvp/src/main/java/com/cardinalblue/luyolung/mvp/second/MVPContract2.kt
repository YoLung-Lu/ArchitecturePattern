package com.cardinalblue.luyolung.mvp.second

import com.cardinalblue.luyolung.repository.model.Article

interface MVPContract2 {
    interface ArticlePresenter {
        fun createNewArticle(article: Article)
        fun onArticleClicked(article: Article)
        fun onBackClicked()
    }

    interface ArticleView {
        fun onUpdate(articles: List<Article>)
        fun showArticleContent(article: Article)
        fun hideArticleContent()
    }
}