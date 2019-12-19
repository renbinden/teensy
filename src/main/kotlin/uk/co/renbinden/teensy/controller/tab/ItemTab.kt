/*
 *    Copyright 2019 Ren Binden
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package uk.co.renbinden.teensy.controller.tab

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseButton.PRIMARY
import javafx.scene.input.MouseButton.SECONDARY
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.component.ItemGridSquare
import uk.co.renbinden.teensy.world.BitsyItem

class ItemTab {

    lateinit var teensy: Teensy

    @FXML lateinit var id: TextField
    @FXML lateinit var grid: GridPane
    @FXML lateinit var name: TextField
    @FXML lateinit var dialog: ComboBox<String>
    @FXML lateinit var color: Spinner<Int>

    fun init(teensy: Teensy) {
        this.teensy = teensy

        id.textProperty().addListener { observable, oldValue, newValue ->
            val item = teensy.world.getItem(oldValue)
            if (item != null) {
                item.id = newValue
                val itemsTreeItem = teensy.controller.items
                val oldTreeItem = itemsTreeItem.children.firstOrNull { treeItem -> treeItem.value == oldValue }
                val index = itemsTreeItem.children.indexOf(oldTreeItem)
                itemsTreeItem.children.remove(oldTreeItem)
                teensy.controller.items.children.add(index, TreeItem<String>(newValue))
            }
        }
        
        name.textProperty().addListener { observable, oldValue, newValue ->
            val item = teensy.world.getItem(id.text)
            if (item != null) {
                item.name = if (newValue.isNotBlank()) newValue else null
            }
        }

        dialog.items.addAll(teensy.world.dialogs.map { dialog -> dialog.id })
        dialog.items.add("NONE")
        dialog.selectionModel.select(0)
    }

    fun loadItem(item: BitsyItem) {
        id.text = item.id
        for (x in 0..7) {
            for (y in 0..7) {
                val square = ItemGridSquare()
                square.isPainted = item.drawing.frames[0].data[y][x]
                grid.add(square, x, y)
                square.draw()
                square.onMouseClicked = EventHandler<MouseEvent> { event ->
                    if (event.button == PRIMARY) {
                        square.isPainted = true
                        square.draw()
                        item.drawing.frames[0].data[y][x] = true
                    } else if (event.button == SECONDARY) {
                        square.isPainted = false
                        square.draw()
                        item.drawing.frames[0].data[y][x] = false
                    }
                }
            }
        }
        name.text = item.name
        val itemDialogId = item.dialogId
        if (itemDialogId != null && teensy.world.getDialog(itemDialogId) != null) {
            dialog.selectionModel.select(itemDialogId)
        } else {
            dialog.selectionModel.select("NONE")
        }
        color.valueFactory.value = item.color ?: 2

        dialog.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            val dialog = if (newValue == "NONE") null else teensy.world.getDialog(newValue)
            item.dialogId = dialog?.id
        }

        color.valueProperty().addListener { observable, oldValue, newValue ->
            item.color = newValue
        }
    }

}
