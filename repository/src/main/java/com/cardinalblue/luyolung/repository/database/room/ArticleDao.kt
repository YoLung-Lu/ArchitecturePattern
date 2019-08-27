package com.cardinalblue.luyolung.repository.database.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cardinalblue.luyolung.repository.model.Article


@Dao
interface ArticleDao {

    // LiveData is a data holder class that can be observed within a given lifecycle.
    // Always holds/caches latest version of data. Notifies its active observers when the
    // data has changed. Since we are getting all the contents of the database,
    // we are notified whenever any of the database contents have changed.
    @Query("SELECT * from article_table ORDER BY id ASC")
    fun getAlphabetizedWords(): LiveData<List<Article>>

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert
    fun insert(word: Article)

    @Delete
    fun delete(word: Article)

    @Query("DELETE FROM article_table")
    fun deleteAll()
}
