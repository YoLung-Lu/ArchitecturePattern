package com.cardinalblue.luyolung.mvvm.second.viewmodel

import com.cardinalblue.luyolung.mvvm.second.Optional
import com.cardinalblue.luyolung.mvvm.second.model.SelectedArticleModel
import com.cardinalblue.luyolung.repository.model.Article
import com.jakewharton.rxrelay2.Relay

class SelectedArticleViewModel(private val selectedArticleModel: SelectedArticleModel) {

    val articleSubject: Relay<Optional<Article>> = selectedArticleModel.articleSubject

    fun setArticle(article: Article) {
        selectedArticleModel.setArticle(article)
    }

    fun clear() {
        selectedArticleModel.clear()
    }
}