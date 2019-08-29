package com.cardinalblue.luyolung.mvc.second

import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeActiveRepository
import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVCController(private val repository: RunTimeActiveRepository,
                    private val view: MVCActivity) {

    init {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }
    }

    fun createNewArticle(article: Article) {
        repository.addArticle(article)
    }

    fun selectArticle(article: Article) {
        repository.selectArticle(article)
//        view.showArticleContent(article)
    }

    fun backFromArticle() {
        repository.selectArticle(null)
//        view.hideArticleContent()
    }
}