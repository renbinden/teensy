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

class BitsyItem(
    override var id: String,
    override var drawing: BitsyDrawing,
    var name: String?,
    var dialogId: String?,
    var color: Int?
): BitsySerializable, BitsyMutableImageResource {

    constructor(id: String, drawing: BitsyDrawing, name: String, dialog: BitsyDialog, color: Int?):
            this(id, drawing, name, dialog.id, color)

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("ITM $id\n")
        output.append(drawing.serialize())
        if (name != null) {
            output.append("NAME $name\n")
        }
        if (dialogId != null) {
            output.append("DLG $dialogId\n")
        }
        if (color != null) {
            output.append("COL $color\n")
        }
        output.append('\n')
        return output.toString()
    }

    companion object {
        fun deserialize(lines: List<String>, i: Int): BitsyItem {
            var j = i
            val id = lines[j].split(" ")[1]
            var drawing = BitsyDrawing("DRW_$id", mutableListOf())
            var name: String? = null
            j++
            if (lines[j].split(" ")[0] == "DRW") {
                // Drawing already exists. Bitsy doesn't output items this way but is able to parse them.
                // We'll do these in the second pass.
                j++
            } else {
                val drawingId = "ITM_$id"
                drawing = BitsyDrawing.deserialize(lines, j, drawingId)
            }
            var color = 2
            var dialogId: String? = null
            while (j < lines.size && lines[j].isNotEmpty()) {
                val lineParts = lines[j].split(" ")
                when (lineParts[0]) {
                    "COL" -> color = lineParts[1].toInt()
                    "DLG" -> dialogId = lineParts[1]
                    "NAME" -> {
                        name = Regex("\\s(.+)").find(lines[j])?.value
                    }
                }
                j++
            }
            return BitsyItem(
                id,
                drawing,
                name,
                dialogId,
                color
            )
        }

        fun size(lines: List<String>, i: Int): Int {
            var j = i
            j++ // id
            if (lines[j].split(" ")[0] == "DRW") {
                j++
            } else {
                j += BitsyDrawing.size(lines, j, "ITM_n")
            }
            while (j < lines.size && lines[j].isNotEmpty()) {
                j++
            }
            return j - i
        }
    }

}
