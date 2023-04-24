package com.example.tengrinews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tengrinews.R
import com.example.tengrinews.databinding.NewsItemBinding

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.newsHolder>() {

    var newsList = ArrayList<Article>()
    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setList(arr: ArrayList<Article>){
        this.newsList = arr
    }

    class newsHolder(item: View, listener: onItemClickListener): RecyclerView.ViewHolder(item){
        val binding = NewsItemBinding.bind(item)
        val name = binding.nameNews
        val title = binding.titleNews
        val time = binding.timeNews
        val img = binding.imageView

        init{
            item.setOnClickListener{
                listener.onItemClick(adapterPosition, )
            }
        }

        fun bindNews(news: Article){
            name.text = news.title
            title.text = news.source.name
            val day = news.publishedAt.subSequence(0, 10)

            val cl = news.publishedAt.subSequence(11,16)
            time.text = "${day}, ${cl}"

            val url = news.image
            Glide.with(img)
                .load(url)
                .placeholder(R.drawable.image)
                .error(R.drawable.image)
                .fallback(R.drawable.image)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): newsHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return newsHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: newsHolder, position: Int) {
        holder.bindNews(newsList[position])
    }

    override fun getItemCount(): Int {
        return newsList.size
    }
}