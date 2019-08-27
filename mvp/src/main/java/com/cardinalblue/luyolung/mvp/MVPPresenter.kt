package com.cardinalblue.luyolung.mvp

import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVPPresenter(private val repository: SharePrefRepository,
                   private val view: MVPContract.ArticleView): MVPContract.ArticlePresenter {

    init {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }

        view.onUpdate(repository.getArticles())
    }

    override fun createNewArticle(article: Article) {
        repository.addArticle(article)
        view.onUpdate(repository.getArticles())
    }
}