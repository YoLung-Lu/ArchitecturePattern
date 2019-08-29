package com.cardinalblue.luyolung.repository.database.sharepref

import com.cardinalblue.luyolung.repository.model.Article
import io.reactivex.subjects.Subject

class RunTimeActiveRepository {

    private val articleChangedSubscribers: MutableList<Subject<Unit>> = mutableListOf()
    private val articleListChangedSubscribers: MutableList<Subject<Unit>> = mutableListOf()

    private var articles: MutableList<Article> = mutableListOf()
    private var selectedArticle: Article? = null
    private val defaultArticles: MutableList<Article> = mutableListOf()

    fun setDefaultArticle(articles: List<Article>) {
        defaultArticles.addAll(articles)
    }

    fun mergeDefaultArticles() {
        articles.addAll(defaultArticles)
        notifyArticleListChange()
    }

    fun addArticle(article: Article) {
        articles.add(article)
        notifyArticleListChange()
    }

    fun getArticles(): MutableList<Article> = articles

    fun selectArticle(article: Article?) {
        this.selectedArticle = article
        notifyArticleChange()
    }

    fun getSelectedArticle(): Article? = selectedArticle


    // Active notify related functions. //
    fun addArticleChangedSubscriber(subscribe: Subject<Unit>) {
        articleChangedSubscribers.add(subscribe)
    }

    fun addArticleListChangedSubscriber(subscribe: Subject<Unit>) {
        articleListChangedSubscribers.add(subscribe)
    }

    private fun notifyArticleChange() {
        articleChangedSubscribers.forEach {
            it.onNext(Unit)
        }
    }

    private fun notifyArticleListChange() {
        articleListChangedSubscribers.forEach {
            it.onNext(Unit)
        }
    }
}