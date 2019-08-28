package com.cardinalblue.luyolung.repository.util

import com.cardinalblue.luyolung.repository.model.Article
import kotlin.random.Random

const val CONTENT_LINE_NUMBER = 10
const val CONTENT_LINE_LENGTH = 10
const val TITLE_LENGTH = 7
const val PUSH_RANGE = 150

val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
val authorList: List<String> = listOf("Allen", "Boy", "Tester01", "NamingIsHard")
val categoryList: List<String> = listOf("廢文", "自介", "水桶", "爆卦")

/**
 * Garbage article generator.
 */
class ArticleGenerator {
    companion object {

        fun randomArticle(): Article =
            Article(null, randomStringTitle(), randomAuthor(), randomCategory(), randomPush(), randomStringContent(), "27-08-2019")


        private fun randomCategory(): String = categoryList.random()
        private fun randomAuthor(): String = authorList.random()
        private fun randomPush(): Int = Random.nextInt(-1 * PUSH_RANGE, PUSH_RANGE)

        private fun randomStringTitle(): String =
            (1..TITLE_LENGTH)
                .map { Random.nextInt(0, charPool.size) }
                .map(charPool::get)
                .joinToString("")

        private fun randomStringContent(): String =
            (1..CONTENT_LINE_NUMBER).joinToString("\n") {
                (1..CONTENT_LINE_LENGTH)
                    .map { Random.nextInt(0, charPool.size) }
                    .map(charPool::get)
                    .joinToString("")
            }
    }
}