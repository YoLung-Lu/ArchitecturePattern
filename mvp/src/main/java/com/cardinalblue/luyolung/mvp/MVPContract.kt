package com.cardinalblue.luyolung.mvp

import com.cardinalblue.luyolung.repository.model.Article

interface MVPContract {
    interface ArticlePresenter {
        fun createNewArticle(article: Article)
    }

    interface ArticleView {
        fun onUpdate(articles: List<Article>)
    }
}