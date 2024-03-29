package com.cardinalblue.luyolung.mvp.first

import com.cardinalblue.luyolung.repository.model.Article

interface MVPContract {
    interface ArticlePresenter {
        fun createNewArticle()
    }

    interface ArticleView {
        fun onUpdate(articles: List<Article>)
    }
}