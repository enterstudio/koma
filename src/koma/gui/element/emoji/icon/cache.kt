package koma.gui.element.emoji.icon

import javafx.beans.value.ObservableValue
import javafx.scene.image.Image
import koma.network.media.ImgCacheProc
import koma.network.media.MHUrl
import koma.storage.config.settings.AppSettings
import okhttp3.HttpUrl
import java.io.InputStream
import kotlin.streams.toList

object EmojiCache: ImgCacheProc({ i -> processEmoji(i) }) {
    fun getEmoji(emoji: String): ObservableValue<Image> {
        val code = getEmojiCode(emoji)
        val url = getCdnEmojiUrl(code)
        return getProcImg(MHUrl.Http(url,365 * 86400))
    }
}

fun getEmojiCode(emoji: String): String {
    val points = emoji.codePoints().filter {
        it != 0xfe0f && it != 0x200d
        && it != 0x2640 && it != 0x2640
    }.toList()
    return points.map { String.format("%x", it) }.joinToString("-")
}

private fun getCdnEmojiUrl(code: String): HttpUrl {
    return  HttpUrl.parse("https://cdnjs.cloudflare.com/ajax/libs/emojione/2.2.7/assets/png/$code.png")!!
}

private fun processEmoji(bytes: InputStream): Image {
    val size = AppSettings.fontSize
    val im = Image(bytes, size, size, true , true)
    return im
}
