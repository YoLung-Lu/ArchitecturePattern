package com.cardinalblue.luyolung.repository.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_table")
class User(@PrimaryKey @ColumnInfo(name = "id") val id: Long,
              val name: String)