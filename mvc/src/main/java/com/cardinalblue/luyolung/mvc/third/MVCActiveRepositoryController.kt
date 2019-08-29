package com.cardinalblue.luyolung.mvc.third

import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeActiveRepository
import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article

class MVCActiveRepositoryController(private val repository: RunTimeActiveRepository) {

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
    }

    fun backFromArticle() {
        repository.selectArticle(null)
    }
}