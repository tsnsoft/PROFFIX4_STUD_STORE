package kz.gvsx.tou.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kz.gvsx.tou.databinding.ListItemNotificationBinding
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import java.time.Year
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class NotificationsAdapter :
    ListAdapter<Notification, NotificationsAdapter.ViewHolder>(NotificationDiffCallback) {

    class ViewHolder(private val binding: ListItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: Notification) = with(binding) {
            textViewMain.text = notification.text
            textViewMain.movementMethod = BetterLinkMovementMethod.getInstance()
            val pattern: String =
                if (notification.date.year == Year.now(ZoneId.systemDefault()).value) "d MMMM" else "d MMMM yyyy"
            textViewDate.text = DateTimeFormatter.ofPattern(pattern).format(notification.date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItemNotificationBinding =
            ListItemNotificationBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(listItemNotificationBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean =
        oldItem.toString() == newItem.toString()

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean =
        oldItem == newItem
}