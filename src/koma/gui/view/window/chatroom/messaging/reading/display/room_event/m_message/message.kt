package koma.gui.view.window.chatroom.messaging.reading.display.room_event.m_message

import javafx.geometry.Pos
import javafx.scene.control.MenuItem
import javafx.scene.input.Clipboard
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import koma.gui.element.icon.AvatarAlways
import koma.gui.view.window.chatroom.messaging.reading.display.ViewNode
import koma.gui.view.window.chatroom.messaging.reading.display.room_event.m_message.content.render_node
import koma.gui.view.window.chatroom.messaging.reading.display.room_event.util.showDatetime
import koma.matrix.event.room_message.MRoomMessage
import koma.storage.users.UserStore
import tornadofx.*

class MRoomMessageViewNode(val item: MRoomMessage): ViewNode {
    override val node = StackPane()
    override val menuItems: List<MenuItem>

    init {

        val sus = UserStore.getOrCreateUserId(item.sender)
        val sender = sus.displayName
        val color = sus.color

        val mcontent = item.render_node()
        val items = mcontent?.menuItems ?: listOf()
        val mi = MenuItem("Copy text")
        with(mi){
            action { copyText() }
        }
        menuItems = items + mi

        with(node) {
            paddingAll = 2.0
            hbox {
                minWidth = 1.0
                prefWidth = 1.0
                style {
                    alignment = Pos.CENTER_LEFT
                    paddingAll = 2.0
                    backgroundColor = multi(Color.WHITE)
                }
                add(AvatarAlways(sus.avatarURL, sus.displayName, sus.color))

                vbox(spacing = 2.0) {
                    hgrow = Priority.ALWAYS
                    hbox(spacing = 10.0) {
                        hgrow = Priority.ALWAYS
                        text(sender) {
                            fill = color
                        }

                        showDatetime(this, item.origin_server_ts)
                    }
                    hbox(spacing = 5.0) {
                        val c = mcontent?.node ?: Region()
                        add(c)
                    }
                }
            }
        }
    }

    fun copyText() {
        item.content?.body?.let {
            Clipboard.getSystemClipboard().putString(it)
        }
    }
}

