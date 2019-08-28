package com.cardinalblue.luyolung.mvp.second

import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVPPresenter2(private val repository: SharePrefRepository,
                    private val view: MVPContract2.ArticleView): MVPContract2.ArticlePresenter {

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

    override fun onArticleClicked(article: Article) {
        view.showArticleContent(article)
    }

    override fun onBackClicked() {
        view.hideArticleContent()
    }
}