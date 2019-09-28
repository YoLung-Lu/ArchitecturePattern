package com.cardinalblue.luyolung.mvvm.second.model

import com.cardinalblue.luyolung.mvvm.second.Optional
import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay

class SelectedArticleModel(defaultArticle: Optional<Article> = Optional(null)) {

    val articleSubject: Relay<Optional<Article>> = BehaviorRelay.create<Optional<Article>>().toSerialized()

    init {
        articleSubject.accept(defaultArticle)
    }

    fun setArticle(article: Article?) {
        articleSubject.accept(Optional(article))
    }

    fun clear() {
        val article = Optional<Article>(null)
        articleSubject.accept(article)
    }
}