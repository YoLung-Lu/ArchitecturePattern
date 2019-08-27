package com.cardinalblue.luyolung.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article_table")
data class Article(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long? = null,
    val title: String,
    // TODO:
//    val author: User,
    val author: String,
    val category: String,
    val pushNum: Int,
    val content: String,
    val publishTime: String
)