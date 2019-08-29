package com.cardinalblue.luyolung.mvvm.third

import com.cardinalblue.luyolung.mvvm.second.Optional
import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class ArticleListWrapRxViewModel(defaultArticle: List<Article>) {

    val articleListSubject: Relay<List<ArticleWrapRXViewModel>> = BehaviorRelay.create<List<ArticleWrapRXViewModel>>().toSerialized()

    val selectedArticleSubject: Observable<Optional<Article>> =
        articleListSubject.map { articles ->
            articles.filter { it.selected }.firstOrNull()?.let { Optional(it.article) }?: Optional<Article>(null)
            }


    val showArticleSubject: Observable<Boolean> =
        articleListSubject.map { articles ->
            articles.filter { it.selected }.firstOrNull()?.let { true }?: false
        }

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