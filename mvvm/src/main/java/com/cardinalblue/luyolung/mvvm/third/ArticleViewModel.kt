package com.cardinalblue.luyolung.mvvm.third

import com.cardinalblue.luyolung.repository.model.Article

class ArticleViewModel(val article: Article) {
    // Add a field to article representing the view state.
    var selected: Boolean = false
}