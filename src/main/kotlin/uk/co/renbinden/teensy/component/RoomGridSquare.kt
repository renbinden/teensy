/*
 *    Copyright 2019 Ross Binden
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

package uk.co.renbinden.teensy.component

import javafx.scene.canvas.Canvas
import uk.co.renbinden.teensy.world.*


class RoomGridSquare(val world: BitsyWorld): Canvas(32.0, 32.0) {

    lateinit var room: BitsyRoom
    var tile: BitsyTile? = null
    val sprites = mutableListOf<BitsySprite>()
    val items = mutableListOf<BitsyItem>()
    val endings = mutableListOf<BitsyEnding>()

    fun draw() {
        val palette = world.getPalette(room.paletteId) ?: return
        val graphics = graphicsContext2D
        graphics.fill = palette.colors[0].toColor()
        graphics.fillRect(0.0, 0.0, 32.0, 32.0)
        if (tile != null) {
            graphics.drawImage(
                tile?.drawing?.toImage(palette.colors[tile?.color ?: 1], width.toInt(), height.toInt()),
                0.0,
                0.0,
                width,
                height
            )
        }
        for (sprite in sprites) {
            graphics.drawImage(
                sprite.drawing.toImage(palette.colors[sprite.color ?: 2], width.toInt(), height.toInt()),
                0.0,
                0.0,
                width,
                height
            )
        }
        for (item in items) {
            graphics.drawImage(
                item.drawing.toImage(palette.colors[item.color ?: 2], width.toInt(), height.toInt()),
                0.0,
                0.0,
                width,
                height
            )
        }
    }

}