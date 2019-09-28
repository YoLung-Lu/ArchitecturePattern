package com.cardinalblue.luyolung.mvvm.second.model

import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay

class ArticlesModel {

    val articleListSubject: Relay<List<Article>> = BehaviorRelay.create<List<Article>>().toSerialized()

    private val articles = mutableListOf<Article>()

    fun setDefaultData(defaultArticle: List<Article>) {
        articles.addAll(defaultArticle)
        articles.isNotEmpty().let {
            articleListSubject.accept(articles)
        }
    }

    fun add(article: Article) {
        articles.add(article)
        articleListSubject.accept(articles)
    }

    fun clear() {
        articles.clear()
        articleListSubject.accept(articles)
    }
}