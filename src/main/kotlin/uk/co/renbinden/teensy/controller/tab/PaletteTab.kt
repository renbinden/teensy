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
import javafx.scene.control.Button
import javafx.scene.control.ColorPicker
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.world.BitsyColor
import uk.co.renbinden.teensy.world.BitsyPalette
import kotlin.math.roundToInt


class PaletteTab {

    lateinit var teensy: Teensy

    @FXML lateinit var id: TextField
    @FXML lateinit var name: TextField
    @FXML lateinit var colors: VBox
    @FXML lateinit var addColorButton: Button

    val colorPickers = mutableListOf<ColorPicker>()

    fun init(teensy: Teensy) {
        this.teensy = teensy

        id.textProperty().addListener { observable, oldValue, newValue ->
            val palette = teensy.world.getPalette(oldValue)
            if (palette != null) {
                palette.id = newValue
                val palettesTreeItem = teensy.controller.palettes
                val oldTreeItem = palettesTreeItem.children.firstOrNull { treeItem -> treeItem.value == oldValue }
                val index = palettesTreeItem.children.indexOf(oldTreeItem)
                palettesTreeItem.children.remove(oldTreeItem)
                teensy.controller.palettes.children.add(index, TreeItem<String>(newValue))
            }
        }

        name.textProperty().addListener { observable, oldValue, newValue ->
            val palette = teensy.world.getPalette(id.text)
            if (palette != null) {
                palette.name = if (newValue.isNotBlank()) newValue else null
            }
        }

        addColorButton.onAction = EventHandler<ActionEvent> { event ->
            addColor(Color.color(1.0, 1.0, 1.0))
        }
    }

    fun addColor(color: BitsyColor) {
        addColor(color.toColor())
    }

    fun addColor(color: Color) {
        colors.children.remove(addColorButton)
        val hbox = HBox()
        val colorPicker = ColorPicker(color)
        colorPicker.onAction = EventHandler<ActionEvent> { event ->
            val palette = teensy.world.getPalette(id.text)
            if (palette != null) {
                val colors = palette.colors
                colors.clear()
                colors.addAll(getColors())
            }
        }
        colorPickers.add(colorPicker)
        hbox.children.add(colorPicker)
        HBox.setMargin(colorPicker, Insets(8.0))
        val removeButton = Button("-")
        removeButton.onAction = EventHandler<ActionEvent> { event ->
            colors.children.remove(hbox)
        }
        hbox.children.add(removeButton)
        HBox.setMargin(removeButton, Insets(8.0))
        colors.children.add(hbox)
        colors.children.add(addColorButton)
    }

    fun setColors(vararg colors: BitsyColor) {
        setColors(*colors.map { color -> color.toColor() }.toTypedArray())
    }

    fun setColors(vararg colors: Color) {
        colorPickers.forEach { colorPicker -> this.colors.children.remove(colorPicker.parent) }
        colors.forEach { color -> addColor(color) }
    }

    fun getColors(): List<BitsyColor> {
        return colorPickers
            .map { colorPicker -> colorPicker.value }
            .map { color -> BitsyColor((color.red * 255).roundToInt(), (color.green * 255).roundToInt(), (color.blue * 255).roundToInt()) }
    }

    fun loadPalette(palette: BitsyPalette) {
        id.text = palette.id
        name.text = palette.name ?: ""
        setColors(*palette.colors.toTypedArray())
    }

}