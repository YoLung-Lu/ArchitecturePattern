package com.cardinalblue.luyolung.mvp.first

import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVPPresenter(private val repository: RunTimeRepository,
                   private val view: MVPContract.ArticleView
): MVPContract.ArticlePresenter {

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