package com.cardinalblue.luyolung.mvp.second

import com.cardinalblue.luyolung.repository.model.Article

interface MVPContract2 {
    interface ArticlePresenter {
        fun createNewArticle()
        fun onArticleClicked(article: Article)
        fun onBackClicked()
    }

    interface ArticleView {
        fun onUpdate(articles: List<Article>)
        fun showArticleContent(article: Article)
        fun hideArticleContent()
    }
}