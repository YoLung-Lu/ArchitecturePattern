package com.cardinalblue.luyolung.mvvm.first

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.cardinalblue.luyolung.mvvm.third.ArticleViewModel
import com.cardinalblue.luyolung.repository.database.room.ArticleRepository
import com.cardinalblue.luyolung.repository.database.room.ArticleRoomDatabase
import com.cardinalblue.luyolung.repository.model.Article
import com.cardinalblue.luyolung.repository.util.ArticleGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ArticleListViewModel(application: Application) : AndroidViewModel(application) {

    private var parentJob = Job()
    // By default all the coroutines launched in this scope should be using the Main dispatcher
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val repository: ArticleRepository
    // Using LiveData and caching what getAlphabetizedWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allArticles: LiveData<List<Article>>

    init {
        val articleDao = ArticleRoomDatabase.getDatabase(application, scope).articleDao()
        repository = ArticleRepository(articleDao)
        allArticles = repository.allArticles
    }

    fun generateNewArticle() {
        val article = ArticleGenerator.randomArticle()
        insert(article)
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(article: Article) = scope.launch(Dispatchers.IO) {
        repository.insert(article)
    }

    fun delete(article: Article) = scope.launch(Dispatchers.IO) {
        repository.delete(article)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}
