package com.cardinalblue.luyolung.repository.database.sharepref

import com.cardinalblue.luyolung.repository.model.Article

class SharePrefRepository {

    private var articles: MutableList<Article> = mutableListOf()
    private val defaultArticles: MutableList<Article> = mutableListOf()

    fun setDefaultArticle(articles: List<Article>) {
        defaultArticles.addAll(articles)
    }

    fun mergeDefaultArticles() {
        articles.addAll(defaultArticles)
    }

    fun addArticle(article: Article) {
        articles.add(article)
    }

    fun getArticles(): MutableList<Article> = articles
}