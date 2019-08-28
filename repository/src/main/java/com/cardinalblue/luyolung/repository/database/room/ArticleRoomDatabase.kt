package com.cardinalblue.luyolung.repository.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cardinalblue.luyolung.repository.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Article::class], version = 1)
abstract class ArticleRoomDatabase: RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: ArticleRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ArticleRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticleRoomDatabase::class.java,
                    "article_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(
                        ArticleDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class ArticleDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(
                            database.articleDao()
                        )
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         */
        fun populateDatabase(articleDao: ArticleDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            articleDao.deleteAll()

            val article1 = Article(null, "Hello", "Allen", "廢文", 999, "This is the content", "25-08-2019")
            articleDao.insert(article1)
            val article2 = Article(null, "Testing", "Beta", "廢文", -50, "~~~~~~~", "25-08-2019")
            articleDao.insert(article2)
        }
    }

}
