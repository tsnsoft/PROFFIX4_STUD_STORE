package kz.gvsx.tou.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor() : ViewModel() {

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    init {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            val doc = getDoc()
            val notificationsDiv: Element = doc.selectFirst("div.notification")

            val notifications = mutableListOf<Notification>()

            for (notification in notificationsDiv.children()) {
                val element: Element = notification.selectFirst("div.introtext")
                val date: String = notification.selectFirst("div.details > div > span").text()

                // Convert relative paths into absolute.
                for (url in element.select("a[href]")) {
                    url.attr("href", url.absUrl("href"))
                }

                notifications.add(Notification(element, LocalDate.parse(date)))
            }

            _notifications.postValue(notifications.toList())
        }
    }

    private suspend fun getDoc(): Document = withContext(Dispatchers.IO) {
        Jsoup.connect("https://tou.edu.kz/ru/component/notifications").timeout(5000).get()
    }
}