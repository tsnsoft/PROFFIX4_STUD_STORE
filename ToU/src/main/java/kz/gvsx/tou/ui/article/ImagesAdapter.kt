package kz.gvsx.tou.ui.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import kz.gvsx.tou.databinding.ListItemImageBinding

class ImagesAdapter(private val clickListener: (String, View) -> Unit) :
    ListAdapter<String, ImagesAdapter.ViewHolder>(ImagesDiffCallback) {

    class ViewHolder(
        private val binding: ListItemImageBinding,
        clickAtPosition: (Int, View) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.imageThumb.setOnClickListener {
                val adapterPos = bindingAdapterPosition
                if (adapterPos != RecyclerView.NO_POSITION) clickAtPosition(adapterPos, it)
            }
        }

        fun bind(image: String) = with(binding) {
            imageThumb.load(image) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16F))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItemImageBinding = ListItemImageBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(listItemImageBinding) { position, view ->
            clickListener(getItem(position), view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object ImagesDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}