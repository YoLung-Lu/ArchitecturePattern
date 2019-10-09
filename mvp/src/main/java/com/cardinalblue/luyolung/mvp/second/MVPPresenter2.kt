package com.cardinalblue.luyolung.mvp.second

import com.cardinalblue.luyolung.repository.database.sharepref.RunTimeRepository
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator

class MVPPresenter2(private val repository: RunTimeRepository,
                    private val view: ArticleContract.ArticleView): ArticleContract.ArticlePresenter {

    init {
        if (repository.getArticles().size == 0) {
            repository.mergeDefaultArticles()
        }

        view.showArticles(repository.getArticles())
    }

    override fun onAddArticle() {
        val article = ArticleGenerator.randomArticle()
        repository.addArticle(article)
        view.showArticles(repository.getArticles())
    }

    override fun onArticleSelected(article: Article) {
        view.showArticleContent(article)
    }

    override fun onBack() {
        view.hideArticleContent()
    }
}