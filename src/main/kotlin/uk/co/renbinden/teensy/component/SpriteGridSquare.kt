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
import javafx.scene.paint.Color.BLACK
import javafx.scene.paint.Color.WHITE
import uk.co.renbinden.teensy.world.BitsyItem


class SpriteGridSquare: Canvas(32.0, 32.0) {

    lateinit var item: BitsyItem
    var isPainted: Boolean = false

    fun draw() {
        val graphics = graphicsContext2D
        graphics.fill = if (isPainted) WHITE else BLACK
        graphics.fillRect(0.0, 0.0, 32.0, 32.0)
    }

}