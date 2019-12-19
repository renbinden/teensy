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
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseButton.PRIMARY
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.util.Callback
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.component.RoomGridSquare
import uk.co.renbinden.teensy.world.*
import uk.co.renbinden.teensy.world.room.BitsyRoomEnding
import uk.co.renbinden.teensy.world.room.BitsyRoomItem


class RoomTab : ResourceTab<BitsyRoom>() {

    override lateinit var teensy: Teensy

    @FXML override lateinit var id: TextField
    @FXML lateinit var name: TextField
    @FXML lateinit var palette: ComboBox<String>

    @FXML lateinit var walls: TreeView<String>

    @FXML lateinit var grid: GridPane

    @FXML lateinit var tree: TreeView<String>
    @FXML lateinit var tiles: TreeItem<String>
    @FXML lateinit var sprites: TreeItem<String>
    @FXML lateinit var items: TreeItem<String>
    @FXML lateinit var endings: TreeItem<String>

    val radioGroup = ToggleGroup()
    val resourceButtons = mutableMapOf<BitsyResource, RadioButton>()
    val buttonResources = mutableMapOf<RadioButton, BitsyResource>()
    val wallCheckBoxes = mutableMapOf<BitsyTile, CheckBox>()

    fun init(teensy: Teensy) {
        this.teensy = teensy

        id.textProperty().addListener { observable, oldValue, newValue ->
            val room = teensy.world.getRoom(oldValue)
            if (room != null) {
                updateId(room, teensy.controller.rooms, oldValue, newValue)
            }
        }

        name.textProperty().addListener { observable, oldValue, newValue ->
            val room = teensy.world.getRoom(id.text)
            if (room != null) {
                room.name = if (newValue.isNotBlank()) newValue else null
            }
        }

        palette.items.addAll(teensy.world.palettes.map { palette -> palette.id })
        if (palette.items.isNotEmpty()) {
            palette.selectionModel.select(0)
        }
    }

    inner class ResourceTreeCell(val room: BitsyRoom): TreeCell<String>() {

        val hbox = HBox()

        override fun updateItem(item: String?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty || item == null || treeItem == null) {
                graphic = null
                text = null
                return
            }
            if (treeItem.parent == tree.root) {
                text = item
                return
            }
            hbox.children.clear()
            val palette = teensy.world.getPalette(room.paletteId)
            if (palette != null) {
                when (treeItem.parent.value) {
                    "Tiles" -> {
                        val tile = teensy.world.getTile(item)
                        if (tile != null) {
                            hbox.children.add(resourceButtons[tile])
                            hbox.children.add(
                                ImageView(
                                    tile.drawing.toImage(
                                        palette.colors[tile.color ?: 1],
                                        16,
                                        16
                                    )
                                )
                            )
                        }
                    }
                    "Sprites" -> {
                        val sprite = teensy.world.getSprite(item)
                        if (sprite != null) {
                            hbox.children.add(resourceButtons[sprite])
                            hbox.children.add(
                                ImageView(
                                    sprite.drawing.toImage(
                                        palette.colors[sprite.color ?: 2],
                                        16,
                                        16
                                    )
                                )
                            )
                        }
                    }
                    "Items" -> {
                        val item = teensy.world.getItem(item)
                        if (item != null) {
                            hbox.children.add(resourceButtons[item])
                            hbox.children.add(
                                ImageView(
                                    item.drawing.toImage(
                                        palette.colors[item.color ?: 2],
                                        16,
                                        16
                                    )
                                )
                            )
                        }
                    }
                    "Endings" -> {
                        val ending = teensy.world.getEnding(item)
                        if (ending != null) {
                            hbox.children.add(resourceButtons[ending])
                        }
                    }
                }
            }
            text = item
            graphic = hbox
        }
    }

    inner class WallTreeCell(val room: BitsyRoom): TreeCell<String>() {

        val hbox = HBox()

        override fun updateItem(item: String?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty || item == null || treeItem == null) {
                graphic = null
                text = null
                return
            }
            hbox.children.clear()
            val tile = teensy.world.getTile(item)
            if (tile != null) {
                hbox.children.add(wallCheckBoxes[tile])
                val palette = teensy.world.getPalette(room.paletteId)
                if (palette != null) {
                    hbox.children.add(
                        ImageView(
                            tile.drawing.toImage(
                                palette.colors[tile.color ?: 1],
                                16,
                                16
                            )
                        )
                    )
                }
            }
            text = item
            graphic = hbox
        }

    }

    fun loadRoom(room: BitsyRoom) {
        load(room)
        for (x in 0..15) {
            for (y in 0..15) {
                val square = RoomGridSquare(teensy.world)
                square.room = room
                val tileId = room.tileIds[y][x]
                if (tileId == null) {
                    square.tile = null
                } else {
                    square.tile = teensy.world.getTile(tileId)
                }
                square.sprites.addAll(teensy.world.sprites.filter { sprite -> sprite.roomId == room.id && sprite.x == x && sprite.y == y })
                square.items.addAll(
                    room.items.filter { item -> item.x == x && item.y == y }
                        .mapNotNull { item -> teensy.world.getItem(item.itemId) }
                )
                grid.add(square, x, y)
                square.draw()
                square.onMouseClicked = EventHandler<MouseEvent> { event ->
                    if (event.button == PRIMARY) {
                        val resource = buttonResources[radioGroup.selectedToggle]
                        when (resource) {
                            is BitsyTile -> {
                                square.tile = resource
                                room.tileIds[y][x] = resource.id
                            }
                            is BitsySprite -> {
                                if (!square.sprites.contains(resource)) {
                                    if (resource.roomId == room.id) {
                                        grid.children
                                            .mapNotNull { child -> child as? RoomGridSquare }
                                            .filter { square -> square.sprites.contains(resource) }
                                            .forEach { square ->
                                                square.sprites.remove(resource)
                                                square.draw()
                                            }
                                    }
                                    square.sprites.add(resource)
                                    resource.roomId = room.id
                                    resource.x = x
                                    resource.y = y
                                }
                            }
                            is BitsyItem -> {
                                if (room.items.none { roomItem -> roomItem.x == x && roomItem.y == y && roomItem.itemId == resource.id }) {
                                    square.items.add(resource)
                                    room.items.add(BitsyRoomItem(resource, x, y))
                                }
                            }
                            is BitsyEnding -> {
                                if (room.endings.none { roomEnding -> roomEnding.x == x && roomEnding.y == y && roomEnding.endingId == resource.id }) {
                                    square.endings.add(resource)
                                    room.endings.add(BitsyRoomEnding(resource, x, y))
                                }
                            }
                        }
                        square.draw()
                    } else if (event.button == MouseButton.SECONDARY) {
                        val resource = buttonResources[radioGroup.selectedToggle]
                        when (resource) {
                            is BitsyTile -> {
                                square.tile = null
                                room.tileIds[y][x] = null
                            }
                            is BitsySprite -> {
                                square.sprites.remove(resource)
                                resource.roomId = null
                                resource.x = null
                                resource.y = null
                            }
                            is BitsyItem -> {
                                square.items.remove(resource)
                                room.items.removeIf { roomItem -> resource.id == roomItem.itemId && roomItem.x == x && roomItem.y == y }
                            }
                            is BitsyEnding -> {
                                square.endings.remove(resource)
                                room.endings.removeIf { roomEnding -> resource.id == roomEnding.endingId  && roomEnding.x == x && roomEnding.y == y}
                            }
                        }
                        square.draw()
                    }
                }
            }
        }

        teensy.world.tiles.forEach { tile ->
            val radioButton = RadioButton()
            radioButton.toggleGroup = radioGroup
            resourceButtons[tile] = radioButton
            buttonResources[radioButton] = tile
        }
        teensy.world.sprites.forEach { sprite ->
            val radioButton = RadioButton()
            radioButton.toggleGroup = radioGroup
            resourceButtons[sprite] = radioButton
            buttonResources[radioButton] = sprite
        }
        teensy.world.items.forEach { item ->
            val radioButton = RadioButton()
            radioButton.toggleGroup = radioGroup
            resourceButtons[item] = radioButton
            buttonResources[radioButton] = item
        }
        teensy.world.endings.forEach { item ->
            val radioButton = RadioButton()
            radioButton.toggleGroup = radioGroup
            resourceButtons[item] = radioButton
            buttonResources[radioButton] = item
        }

        tree.cellFactory = Callback<TreeView<String>, TreeCell<String>> { treeView -> ResourceTreeCell(room) }
        tiles.children.addAll(teensy.world.tiles.map { tile -> TreeItem<String>(tile.id) })
        tiles.isExpanded = true
        sprites.children.addAll(teensy.world.sprites.map { sprite -> TreeItem<String>(sprite.id) })
        sprites.isExpanded = true
        items.children.addAll(teensy.world.items.map { item -> TreeItem<String>(item.id) })
        items.isExpanded = true
        endings.children.addAll(teensy.world.endings.map { ending -> TreeItem<String>(ending.id) })
        endings.isExpanded = true

        walls.cellFactory = Callback<TreeView<String>, TreeCell<String>> { treeView -> WallTreeCell(room) }
        teensy.world.tiles.forEach { tile ->
            val checkBox = CheckBox()
            if (tile.isWall) {
                checkBox.isSelected = true
                checkBox.isDisable = true
            } else {
                checkBox.isSelected = room.wallIds.contains(tile.id)
            }
            checkBox.selectedProperty().addListener { observable, oldValue, newValue ->
                if (newValue) {
                    room.wallIds.add(tile.id)
                } else {
                    room.wallIds.remove(tile.id)
                }
            }
            wallCheckBoxes[tile] = checkBox
            walls.root.children.add(TreeItem<String>(tile.id))
        }

        palette.selectionModel.selectedItemProperty().addListener { observable, oldValue, newValue ->
            val palette = teensy.world.getPalette(newValue)
            if (palette != null) {
                room.paletteId = palette.id
                grid.children.mapNotNull { child -> child as? RoomGridSquare }
                        .forEach { child -> child.draw() }
                tree.root.children.forEach { child ->
                    child.isExpanded = false
                    child.isExpanded = true
                }
                walls.root.isExpanded = false
                walls.root.isExpanded = true
            }
        }
    }

}