package com.cardinalblue.luyolung.mvvm.second

import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay

class ArticleListRXViewModel(defaultArticle: List<Article>) {

    val articleListSubject: Relay<List<Article>> = BehaviorRelay.create<List<Article>>().toSerialized()

    private val articles = mutableListOf<Article>()

    init {
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