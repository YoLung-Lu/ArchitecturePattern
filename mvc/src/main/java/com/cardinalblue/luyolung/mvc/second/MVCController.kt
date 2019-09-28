package com.cardinalblue.luyolung.mvc.second

import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator

class MVCController(private val repository: RunTimeRepository,
                    private val view: MVCActivity) {

    init {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }
        view.onUpdate(repository.getArticles())
    }

    fun createNewArticle() {
        val article = ArticleGenerator.randomArticle()
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