package com.cardinalblue.luyolung.mvvm.first

import android.app.Application
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cardinalblue.luyolung.repository.database.room.ArticleRepository
import com.cardinalblue.luyolung.repository.database.room.ArticleRoomDatabase
import com.cardinalblue.luyolung.repository.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val articleLiveData: MutableLiveData<Article?> = MutableLiveData()

    fun setArticle(article: Article?) {
        articleLiveData.value = article
    }

    // Expose the Immutable as interface.
    fun article(): LiveData<Article?> = articleLiveData
}