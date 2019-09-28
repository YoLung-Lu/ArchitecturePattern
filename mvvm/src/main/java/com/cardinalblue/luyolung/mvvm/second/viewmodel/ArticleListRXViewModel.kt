package com.cardinalblue.luyolung.mvvm.second.viewmodel

import com.cardinalblue.luyolung.mvvm.second.model.ArticlesModel
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.jakewharton.rxrelay2.Relay

class ArticleListRXViewModel(private val articles: ArticlesModel) {

    val articleListSubject: Relay<List<Article>> = articles.articleListSubject

    fun generateNewArticle() {
        val article = ArticleGenerator.randomArticle()
        articles.add(article)
    }

    fun clear() {
        articles.clear()
    }
}