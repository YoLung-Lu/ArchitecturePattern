package com.cardinalblue.luyolung.repository.database.room

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.cardinalblue.luyolung.repository.model.Article

class ArticleRepository(private val articleDao: ArticleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allArticles: LiveData<List<Article>> = articleDao.getAlphabetizedWords()

    // You must call this on a non-UI thread or your app will crash. So we're making this a
    // suspend function so the caller methods know this.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(article: Article) {
        articleDao.insert(article)
    }

    @WorkerThread
    suspend fun delete(article: Article) {
        articleDao.delete(article)
    }
}
