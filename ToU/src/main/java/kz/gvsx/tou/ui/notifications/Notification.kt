package kz.gvsx.tou.ui.notifications

import android.text.Spanned
import androidx.core.text.HtmlCompat
import org.jsoup.nodes.Element
import java.time.LocalDate

data class Notification(
    val element: Element,
    val date: LocalDate
)

val Notification.text: Spanned
    get() = HtmlCompat.fromHtml(element.html(), HtmlCompat.FROM_HTML_MODE_LEGACY)