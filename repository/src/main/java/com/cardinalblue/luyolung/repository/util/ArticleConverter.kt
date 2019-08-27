package com.cardinalblue.luyolung.repository.util

import androidx.room.TypeConverter
import com.cardinalblue.luyolung.repository.model.Article
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ArticleConverter {
    private val gson = Gson()
    private val type = object : TypeToken<List<Article>>() {

    }.type

    @TypeConverter
    fun stringToArticleData(json: String): List<Article> {
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun articleToString(articleData: List<Article>): String {
        return gson.toJson(articleData, type)
    }
}