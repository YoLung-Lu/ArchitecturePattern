package com.cardinalblue.luyolung.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.RecyclerView
import com.cardinalblue.luyolung.repository.model.Article

class ArticleAdapter(context: Context, private val articleData: MutableList<Article>):
    RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    // State.
    private var showDetail = true

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mClickListener: ItemClickListener? = null
    private val holders = mutableListOf<ViewHolder>()

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.item_article, parent, false)
        val holder = ViewHolder(view)
        holders.add(holder)
        if (!showDetail) {
            holder.hideDetail()
        }
        return holder
    }

    // binds the data to the TextView in each row
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articleData[position]
        holder.categoryTextView.text = article.category
        holder.pushTextView.text = article.pushNum.toString()
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
        internal var guideLineView: Guideline = itemView.findViewById(R.id.guide_between_category_title)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (mClickListener != null) mClickListener!!.onItemClick(view, articleData[adapterPosition])
        }

        fun showDetail() {
            categoryTextView.visibility = View.VISIBLE
            pushTextView.visibility = View.VISIBLE
            idTextView.visibility = View.VISIBLE
            authorTextView.visibility = View.VISIBLE
            timeTextView.visibility = View.VISIBLE

            val params = guideLineView.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = 50
            guideLineView.layoutParams = params
        }

        fun hideDetail() {
            categoryTextView.visibility = View.GONE
            pushTextView.visibility = View.GONE
            idTextView.visibility = View.GONE
            authorTextView.visibility = View.GONE
            timeTextView.visibility = View.GONE

            val params = guideLineView.layoutParams as ConstraintLayout.LayoutParams
            params.guideBegin = 0
            guideLineView.layoutParams = params
        }
    }

    fun showDetail() {
        holders.forEach {
            it.showDetail()
        }
        showDetail = true
    }

    fun hideDetail() {
        holders.forEach {
            it.hideDetail()
        }
        showDetail = false
    }

    fun setData(newArticleData: List<Article>) {
        articleData.clear()
        articleData.addAll(newArticleData)
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