package com.cardinalblue.luyolung.mvvm.second.viewmodel

import com.cardinalblue.luyolung.mvvm.second.model.SelectedArticleModel
import io.reactivex.Observable

class BackViewModel(selectedArticleModel: SelectedArticleModel) {

    val visible: Observable<Boolean> = selectedArticleModel.articleSubject.map { !it.isEmpty }

}