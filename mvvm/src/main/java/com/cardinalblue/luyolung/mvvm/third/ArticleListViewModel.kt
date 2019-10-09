package com.cardinalblue.luyolung.mvvm.third

import com.cardinalblue.luyolung.mvvm.second.Optional
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.Observable

class ArticleListViewModel(defaultArticle: List<Article>) {

    val articleListSubject: Relay<List<ArticleUIModel>> = BehaviorRelay.create<List<ArticleUIModel>>().toSerialized()

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

    private val articles = mutableListOf<ArticleUIModel>()

    init {
        articles.addAll(
            defaultArticle.map { ArticleUIModel(it) }
        )

        articles.isNotEmpty().let {
            articleListSubject.accept(articles)
        }
    }

    fun selectArticle(article: Article?) {
        articles.forEach {
            it.selected = it.article == article
        }
        articleListSubject.accept(articles)
    }

    fun unSelectArticle() {
        selectArticle(null)
    }

    fun generateNewArticle() {
        val article = ArticleGenerator.randomArticle()
        articles.add(ArticleUIModel(article))
        articleListSubject.accept(articles)
    }

    fun clear() {
        articles.clear()
        articleListSubject.accept(articles)
    }
}