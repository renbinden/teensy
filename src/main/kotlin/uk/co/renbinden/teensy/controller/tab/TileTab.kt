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
import javafx.scene.control.CheckBox
import javafx.scene.control.Spinner
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.input.MouseButton.PRIMARY
import javafx.scene.input.MouseButton.SECONDARY
import javafx.scene.input.MouseEvent
import javafx.scene.layout.GridPane
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.component.TileGridSquare
import uk.co.renbinden.teensy.world.BitsyTile

class TileTab {

    lateinit var teensy: Teensy

    @FXML lateinit var id: TextField
    @FXML lateinit var grid: GridPane
    @FXML lateinit var name: TextField
    @FXML lateinit var wall: CheckBox
    @FXML lateinit var color: Spinner<Int>

    fun init(teensy: Teensy) {
        this.teensy = teensy

        id.textProperty().addListener { observable, oldValue, newValue ->
            val tile = teensy.world.getTile(oldValue)
            if (tile != null) {
                tile.id = newValue
                val tilesTreeItem = teensy.controller.tiles
                val oldTreeItem = tilesTreeItem.children.firstOrNull { treeItem -> treeItem.value == oldValue }
                val index = tilesTreeItem.children.indexOf(oldTreeItem)
                tilesTreeItem.children.remove(oldTreeItem)
                teensy.controller.tiles.children.add(index, TreeItem<String>(newValue))
            }
        }

        name.textProperty().addListener { observable, oldValue, newValue ->
            val tile = teensy.world.getTile(id.text)
            if (tile != null) {
                tile.name = if (newValue?.isNotBlank() == true) newValue else null
            }
        }

        wall.selectedProperty().addListener { observable, oldValue, newValue ->
            val tile = teensy.world.getTile(id.text)
            if (tile != null) {
                tile.isWall = newValue
            }
        }
    }

    fun loadTile(tile: BitsyTile) {
        id.text = tile.id
        for (x in 0..7) {
            for (y in 0..7) {
                val square = TileGridSquare()
                square.isPainted = tile.drawing.frames[0].data[y][x]
                grid.add(square, x, y)
                square.draw()
                square.onMouseClicked = EventHandler<MouseEvent> { event ->
                    if (event.button == PRIMARY) {
                        square.isPainted = true
                        square.draw()
                        tile.drawing.frames[0].data[y][x] = true
                    } else if (event.button == SECONDARY) {
                        square.isPainted = false
                        square.draw()
                        tile.drawing.frames[0].data[y][x] = false
                    }
                }
            }
        }
        name.text = tile.name
        wall.isSelected = tile.isWall
        color.valueFactory.value = tile.color ?: 1

        color.valueProperty().addListener { observable, oldValue, newValue ->
            tile.color = newValue
        }
    }

}
