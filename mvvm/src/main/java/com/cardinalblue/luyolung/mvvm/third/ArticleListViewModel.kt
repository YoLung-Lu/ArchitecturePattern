package com.cardinalblue.luyolung.mvvm.third

import com.cardinalblue.luyolung.mvvm.second.Optional
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class ArticleListViewModel(defaultArticle: List<Article>) {

    val articleListSubject: Relay<List<ArticleViewModel>> = BehaviorRelay.create<List<ArticleViewModel>>().toSerialized()

    val selectedArticleSubject: Observable<Optional<Article>> =
        articleListSubject.map { articles ->
            articles.firstOrNull { it.selected }?.let { Optional(it.article) }
                ?: Optional<Article>(null)
            }


    val showArticleSubject: Observable<Boolean> =
        articleListSubject.map { articles ->
            articles.firstOrNull { it.selected }?.let { true }
                ?: false
        }

    private val articles = mutableListOf<ArticleViewModel>()

    init {
        articles.addAll(
            defaultArticle.map { ArticleViewModel(it) }
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

    fun generateNewArticle() {
        val article = ArticleGenerator.randomArticle()
        articles.add(ArticleViewModel(article))
    }

    fun clear() {
        articles.clear()
        articleListSubject.accept(articles)
    }
}