package kz.gvsx.tou.ui.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import kz.gvsx.tou.databinding.ListItemNewsBinding
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NewsAdapter(private val clickListener: (News, View) -> Unit) :
    ListAdapter<News, NewsAdapter.ViewHolder>(NewsDiffCallback) {

    class ViewHolder(
        private val binding: ListItemNewsBinding,
        clickAtPosition: (Int, View) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.newsContainer.setOnClickListener {
                val adapterPos = bindingAdapterPosition
                if (adapterPos != RecyclerView.NO_POSITION) clickAtPosition(adapterPos, it)
            }
        }

        fun bind(news: News) = with(binding) {
            imageView.load(news.imageUrl) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16F))
            }
            textViewTitle.text = news.title
            val pattern: String =
                if (news.dateTime.year == Year.now(ZoneId.systemDefault()).value) "d MMMM" else "d MMMM yyyy"
            textViewDate.text = DateTimeFormatter.ofPattern(pattern).format(news.dateTime)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItemNewsBinding = ListItemNewsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(listItemNewsBinding) { position, view ->
            clickListener(getItem(position), view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
        oldItem.toString() == newItem.toString()

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
        oldItem == newItem
}
