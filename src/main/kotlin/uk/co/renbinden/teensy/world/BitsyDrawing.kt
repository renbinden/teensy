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

package uk.co.renbinden.teensy.world

import javafx.scene.image.Image


class BitsyDrawing(
    override var id: String, val frames: List<BitsyImageData>
) : BitsyImageResource, BitsySerializable {

    override val drawing: BitsyDrawing = this

    fun toImage(color: BitsyColor, width: Int? = null, height: Int? = null): Image {
        return if (width != null) {
            if (height != null) {
                frames[0].toImage(color, width, height)
            } else {
                frames[0].toImage(color, width = width)
            }
        } else {
            if (height != null) {
                frames[0].toImage(color, height = height)
            } else {
                frames[0].toImage(color)
            }
        }
    }

    override fun serialize(): String {
        return frames.joinToString("\n>\n") { frame -> frame.serialize() }
    }

    companion object {
        fun deserialize(lines: List<String>, i: Int, id: String? = null): BitsyDrawing {
            var j = i
            val id1 = id ?: lines[j++].split(" ")[1]
            val frames = mutableListOf<BitsyImageData>()
            var y = 0
            var rows = mutableListOf<MutableList<Boolean>>()
            while (y < 8) {
                val line = lines[j + y]
                val row = mutableListOf<Boolean>()
                for (x in 0..7) {
                    row.add(line[x] == '1')
                }
                rows.add(row)
                y++
                if (y == 8) {
                    frames.add(BitsyImageData(rows))
                    j += y
                    if (j < lines.size && lines[j].isNotEmpty() && lines[j][0] == '>') {
                        rows = mutableListOf()
                        j++
                        y = 0
                    }
                }
            }
            return BitsyDrawing(id1, frames)
        }

        fun size(lines: List<String>, i: Int, id: String? = null): Int {
            var j = i
            if (id == null) j++ // id
            var y = 0
            while (y < 8) {
                y++
                if (y == 8) {
                    j += y
                    if (j < lines.size && lines[j].isNotEmpty() && lines[j][0] == '>') { // new frame
                        j++
                        y = 0
                    }
                }
            }
            return j - i
        }
    }


}