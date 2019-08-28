package com.cardinalblue.luyolung.mvc.second

import com.cardinalblue.luyolung.repository.database.sharepref.SharePrefRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVCController(private val repository: SharePrefRepository,
                    private val view: MVCActivity) {

    init {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }

        view.onUpdate(repository.getArticles())
    }

    fun createNewArticle(article: Article) {
        repository.addArticle(article)
        view.onUpdate(repository.getArticles())
    }

    fun selectArticle(article: Article) {
        view.showArticleContent(article)
    }

    fun backFromArticle() {
        view.hideArticleContent()
    }
}