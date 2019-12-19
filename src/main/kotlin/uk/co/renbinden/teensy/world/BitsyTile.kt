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

class BitsyTile(
    override var id: String,
    override var drawing: BitsyDrawing,
    var name: String?,
    var isWall: Boolean,
    var color: Int?
): BitsySerializable, BitsyMutableImageResource {

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("TIL $id\n")
        output.append(drawing.serialize())
        if (name != null) {
            output.append("NAME $name\n")
        }
        if (isWall) {
            output.append("WAL $isWall\n")
        }
        if (color != null) {
            output.append("COL $color\n")
        }
        output.append('\n')
        return output.toString()
    }

    companion object {

        fun deserialize(lines: List<String>, i: Int): BitsyTile {
            var j = i
            val id = lines[j++].split(" ")[1]
            var drawing = BitsyDrawing("TIL_$id", mutableListOf())
            var name: String? = null
            val lineParts = lines[j].split(" ")
            if (lineParts[0] == "DRW") {
                // Drawing already exists. Bitsy doesn't output sprites this way but is able to parse them.
                // We'll do these in the second pass.
                j++
            } else {
                val drawingId = "TIL_$id"
                drawing = BitsyDrawing.deserialize(lines, j, drawingId)
                j += BitsyDrawing.size(lines, j)
            }
            var color = 1
            var isWall = false
            while (j < lines.size && lines[j].isNotEmpty()) {
                val lineParts = lines[j].split(" ")
                when (lineParts[0]) {
                    "COL" -> {
                        color = lineParts[1].toInt()
                    }
                    "NAME" -> {
                        name = Regex("\\s(.+)").find(lines[j])?.value
                    }
                    "WAL" -> {
                        isWall = lineParts[1].toBoolean()
                    }
                }
                j++
            }
            return BitsyTile(
                id,
                drawing,
                name,
                isWall,
                color
            )
        }

        fun size(lines: List<String>, i: Int): Int {
            var j = i
            j++ // id
            val lineParts = lines[j].split(" ")
            if (lineParts[0] == "DRW") {
                j++
            } else {
                j += BitsyDrawing.size(lines, j, "TIL_n")
            }
            while (j < lines.size && lines[j].isNotEmpty()) {
                j++ // color, name, wall
            }
            return j - i
        }

    }

}
