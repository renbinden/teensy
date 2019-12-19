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

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton.PRIMARY
import javafx.scene.input.MouseButton.SECONDARY
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.Callback
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.component.SpriteGridSquare
import uk.co.renbinden.teensy.world.BitsyColor
import uk.co.renbinden.teensy.world.BitsyItem
import uk.co.renbinden.teensy.world.BitsySprite

class SpriteTab : ResourceTab<BitsySprite>() {

    override lateinit var teensy: Teensy

    @FXML override lateinit var id: TextField
    @FXML lateinit var grid: GridPane
    @FXML lateinit var name: TextField
    @FXML lateinit var dialog: ComboBox<String>
    @FXML lateinit var color: Spinner<Int>
    @FXML lateinit var inventory: VBox
    @FXML lateinit var addItemButton: Button

    fun init(teensy: Teensy) {
        this.teensy = teensy

        id.textProperty().addListener { observable, oldValue, newValue ->
            val sprite = teensy.world.getSprite(oldValue)
            if (sprite != null) {
                updateId(sprite, teensy.controller.sprites, oldValue, newValue)
            }
        }

        name.textProperty().addListener { observable, oldValue, newValue ->
            val sprite = teensy.world.getSprite(id.text)
            if (sprite != null) {
                sprite.name = if (newValue.isNotBlank()) newValue else null
            }
        }

        dialog.items.addAll(teensy.world.dialogs.map { dialog -> dialog.id })
        dialog.items.add("NONE")
        dialog.selectionModel.select(0)
    }

    fun loadSprite(sprite: BitsySprite) {
        load(sprite)
        for (x in 0..7) {
            for (y in 0..7) {
                val square = SpriteGridSquare()
                square.isPainted = sprite.drawing.frames[0].data[y][x]
                grid.add(square, x, y)
                square.draw()
                square.onMouseClicked = EventHandler<MouseEvent> { event ->
                    if (event.button == PRIMARY) {
                        square.isPainted = true
                        square.draw()
                        sprite.drawing.frames[0].data[y][x] = true
                    } else if (event.button == SECONDARY) {
                        square.isPainted = false
                        square.draw()
                        sprite.drawing.frames[0].data[y][x] = false
                    }
                }
            }
        }
        name.text = sprite.name ?: ""
        val spriteDialogId = sprite.dialogId
        if (spriteDialogId != null && teensy.world.getDialog(spriteDialogId) != null) {
            dialog.selectionModel.select(spriteDialogId)
        } else {
            dialog.selectionModel.select("NONE")
        }
        val spriteColor = sprite.color
        if (spriteColor != null) {
            color.valueFactory.value = spriteColor
        }
        sprite.inventory.entries.forEach { entry ->
            val item = teensy.world.getItem(entry.key)
            if (item != null) {
                addInventoryItem(item, entry.value)
            }
        }

        dialog.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            val dialog = if (newValue == "NONE") null else teensy.world.getDialog(newValue)
            sprite.dialogId = dialog?.id
        }

        color.valueProperty().addListener { observable, oldValue, newValue ->
            sprite.color = newValue
        }

        addItemButton.onAction = EventHandler<ActionEvent> { event ->
            val hbox = HBox()
            val itemComboBox = ComboBox<String>()
            itemComboBox.cellFactory = Callback<ListView<String>, ListCell<String>> { listView -> ItemListCell() }
            itemComboBox.items.addAll(teensy.world.items.map { item -> item.id })
            if (itemComboBox.items.isNotEmpty()) {
                itemComboBox.selectionModel.select(0)
            }
            val quantitySpinner = Spinner<Int>(SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE))
            quantitySpinner.valueProperty().addListener { observable, oldValue, newValue ->
                val item = teensy.world.getItem(itemComboBox.value)
                if (item != null) {
                    sprite.inventory[item.id] = newValue
                }
            }
            itemComboBox.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
                val oldItem = teensy.world.getItem(oldValue)
                val newItem = teensy.world.getItem(newValue)
                sprite.inventory.remove(oldItem?.id)
                if (newItem != null) {
                    sprite.inventory[newItem.id] = quantitySpinner.value
                }
            }
            val removeButton = Button("-")
            removeButton.onAction = EventHandler<ActionEvent> { event ->
                inventory.children.remove(hbox)
            }
            hbox.children.add(itemComboBox)
            hbox.children.add(quantitySpinner)
            hbox.children.add(removeButton)
            HBox.setMargin(itemComboBox, Insets(8.0))
            HBox.setMargin(quantitySpinner, Insets(8.0))
            HBox.setMargin(removeButton, Insets(8.0))
            inventory.children.remove(addItemButton)
            inventory.children.add(hbox)
            inventory.children.add(addItemButton)
            val item = teensy.world.getItem(itemComboBox.value)
            if (item != null) {
                sprite.inventory[item.id] = quantitySpinner.value
            }
        }
    }

    fun addInventoryItem(item: BitsyItem, quantity: Int) {
        val sprite = teensy.world.getSprite(id.text)
        if (sprite != null) {
            val hbox = HBox()
            val itemComboBox = ComboBox<String>()
            itemComboBox.cellFactory = Callback<ListView<String>, ListCell<String>> { listView -> ItemListCell() }
            itemComboBox.items.addAll(teensy.world.items.map { item -> item.id })
            if (itemComboBox.items.isNotEmpty()) {
                itemComboBox.selectionModel.select(item.id)
            }
            val quantitySpinner = Spinner<Int>(SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, quantity))
            quantitySpinner.valueProperty().addListener { observable, oldValue, newValue ->
                val item = teensy.world.getItem(itemComboBox.value)
                if (item != null) {
                    sprite.inventory[item.id] = newValue
                }
            }
            itemComboBox.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
                val oldItem = teensy.world.getItem(oldValue)
                val newItem = teensy.world.getItem(newValue)
                sprite.inventory.remove(oldItem?.id)
                if (newItem != null) {
                    sprite.inventory[newItem.id] = quantitySpinner.value
                }
            }
            val removeButton = Button("-")
            removeButton.onAction = EventHandler<ActionEvent> { event ->
                inventory.children.remove(hbox)
            }
            hbox.children.add(itemComboBox)
            hbox.children.add(quantitySpinner)
            hbox.children.add(removeButton)
            HBox.setMargin(itemComboBox, Insets(8.0))
            HBox.setMargin(quantitySpinner, Insets(8.0))
            HBox.setMargin(removeButton, Insets(8.0))
            inventory.children.remove(addItemButton)
            inventory.children.add(hbox)
            inventory.children.add(addItemButton)
            val item = teensy.world.getItem(itemComboBox.value)
            if (item != null) {
                sprite.inventory[item.id] = quantitySpinner.value
            }
        }
    }

    inner class ItemListCell: ListCell<String>() {

        override fun updateItem(item: String?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty || item == null) {
                graphic = null
                text = null
                return
            }
            val bitsyItem = teensy.world.getItem(item)
            if (bitsyItem != null) {
                graphic = ImageView(bitsyItem.drawing.toImage(BitsyColor(255, 255, 255), 16, 16))
            }
            text = item
        }

    }

}
