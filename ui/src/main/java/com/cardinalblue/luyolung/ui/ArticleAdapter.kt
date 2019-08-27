package com.cardinalblue.luyolung.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.model.Article

class ArticleAdapter(context: Context, private val articleData: List<Article>):
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleData[position]
        // TODO:
//        holder.categoryTextView.text = "test"
//        holder.pushTextView.text = "test"
        holder.titleTextView.text = article.title
        holder.idTextView.text = article.id.toString()
        holder.authorTextView.text = article.author
        holder.timeTextView.text = article.publishTime
    }

    override fun getItemCount(): Int {
        return articleData.size
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        internal var categoryTextView: TextView = itemView.findViewById(R.id.article_category)
        internal var pushTextView: TextView = itemView.findViewById(R.id.article_push)
        internal var titleTextView: TextView = itemView.findViewById(R.id.article_title)
        internal var idTextView: TextView = itemView.findViewById(R.id.article_id)
        internal var authorTextView: TextView = itemView.findViewById(R.id.article_author)
        internal var timeTextView: TextView = itemView.findViewById(R.id.article_time)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, articleData[adapterPosition])
        }
    }

    internal fun getItem(id: Int): Article {
        return articleData[id]
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }

    interface ItemClickListener {
        fun onItemClick(view: View, article: Article)
    }
}