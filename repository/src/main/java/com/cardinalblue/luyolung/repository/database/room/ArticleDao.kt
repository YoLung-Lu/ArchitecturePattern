package com.cardinalblue.luyolung.repository.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cardinalblue.luyolung.repository.model.Article

@Dao
interface ArticleDao {
    @Query("SELECT * from article_table ORDER BY id ASC")
    fun getAlphabetizedWords(): LiveData<List<Article>>

    @Insert
    fun insert(article: Article)

    @Delete
    fun delete(article: Article)

    @Query("DELETE FROM article_table")
    fun deleteAll()
}
