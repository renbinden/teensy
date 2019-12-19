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

package uk.co.renbinden.teensy.world

import javafx.scene.image.Image
import javafx.scene.image.WritableImage


class BitsyImageData(
    val data: MutableList<MutableList<Boolean>>
): BitsySerializable {

    override fun serialize(): String {
        val output = StringBuilder()
        for (row in data) {
            output.append(row.map { if (it) '1' else '0' }.joinToString("")).append('\n')
        }
        return output.toString()
    }

    fun toImage(color: BitsyColor, width: Int = data.size, height: Int = if (data.isNotEmpty()) data[0].size else 0): Image {
        val scaleX = width / data.size
        val scaleY = height / if (data.isNotEmpty()) data[0].size else 0
        val image = WritableImage(width, height)
        for ((y, row) in data.withIndex()) {
            for ((x, col) in row.withIndex()) {
                if (col) {
                    for (subX in (x * scaleX)..(((x + 1) * scaleX) - 1)) {
                        for (subY in (y * scaleY)..(((y + 1) * scaleY) - 1)) {
                            image.pixelWriter.setColor(subX, subY, color.toColor())
                        }
                    }
                }
            }
        }
        return image
    }

}