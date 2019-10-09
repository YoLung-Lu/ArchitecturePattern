package com.cardinalblue.luyolung.mvvm.second.viewmodel

import com.cardinalblue.luyolung.mvvm.second.model.SelectedArticleModel
import com.jakewharton.rxrelay2.BehaviorRelay

class BackViewModel(private val selectedArticleModel: SelectedArticleModel) {

    val backSignal: BehaviorRelay<Unit> = BehaviorRelay.create()

    fun onBackTriggered() {
        if (!selectedArticleModel.articleSubject.value.isEmpty) {
            unSelectArticle()
        } else {
            back()
        }
    }

    private fun unSelectArticle() {
        selectedArticleModel.clear()
    }

    private fun back() {
        // Implement back to previous page.
        backSignal.accept(Unit)
    }
}