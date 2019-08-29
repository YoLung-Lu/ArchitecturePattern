package com.cardinalblue.luyolung.mvvm.third

import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay

class ArticleListWrapRxViewModel(defaultArticle: List<Article>) {

    val articleListSubject: Relay<List<ArticleWrapRXViewModel>> = BehaviorRelay.create<List<ArticleWrapRXViewModel>>().toSerialized()

    private val articles = mutableListOf<ArticleWrapRXViewModel>()

    init {
        articles.addAll(
            defaultArticle.map { ArticleWrapRXViewModel(it) }
        )

        articles.isNotEmpty().let {
            articleListSubject.accept(articles)
        }
    }

    fun setSelectedArticle(article: Article?) {
        articles.forEach {
            it.selected = it.article == article
        }
        articleListSubject.accept(articles)
    }

    fun add(article: Article) {
        articles.add(ArticleWrapRXViewModel(article))
        articleListSubject.accept(articles)
    }

    fun clear() {
        articles.clear()
        articleListSubject.accept(articles)
    }
}