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

class BitsySprite(
    override var id: String,
    override var drawing: BitsyDrawing,
    var name: String?,
    var dialogId: String?,
    var roomId: String?,
    var x: Int?,
    var y: Int?,
    val inventory: LinkedHashMap<String, Int>,
    var color: Int?
): BitsySerializable, BitsyImageResource {

    constructor(id: String, drawing: BitsyDrawing, name: String?, dialog: BitsyDialog?, room: BitsyRoom?, x: Int?,
                y: Int?, inventory: LinkedHashMap<BitsyItem, Int>, color: Int?):
            this(id, drawing, name, dialog?.id, room?.id, x, y,
                LinkedHashMap(inventory.mapKeys { entry -> entry.key.id }), color)

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("SPR $id\n")
        output.append(drawing.serialize())
        if (name != null) {
            output.append("NAME $name\n")
        }
        if (dialogId != null) {
            output.append("DLG $dialogId\n")
        }
        if (roomId != null) {
            output.append("POS $roomId $x,$y\n")
        }
        for (item in inventory.keys) {
            output.append("ITM $item ${inventory[item]}\n")
        }
        if (color != null) {
            output.append("COL $color\n")
        }
        output.append('\n')
        return output.toString()
    }

    fun attachDrawing(lines: List<String>, i: Int, drawings: List<BitsyDrawing>) {
        var j = i
        j++
        val lineParts = lines[j].split(" ")
        if (lineParts[0] == "DRW") {
            val drawingId = lineParts[1]
            drawing = drawings.first { drawing -> drawing.id == drawingId }
            return
        }
    }

    companion object {

        fun deserialize(lines: List<String>, i: Int): BitsySprite {
            var j = i
            val id = lines[j].split(" ")[1]
            var drawing = BitsyDrawing("SPR_$id", mutableListOf())
            var name: String? = null
            var roomId: String? = null
            var x: Int? = null
            var y: Int? = null
            j++
            if (lines[j].split(" ")[0] == "DRW") {
                // Drawing already exists. Bitsy doesn't output sprites this way but is able to parse them.
                // We'll do these in the second pass.
                j++
            } else {
                val drawingId = "SPR_$id"
                drawing = BitsyDrawing.deserialize(lines, j, drawingId)
                j += BitsyDrawing.size(lines, j, drawingId)
            }

            var color = 2
            var dialogId: String? = null
            val inventory = LinkedHashMap<String, Int>()
            while (j < lines.size && lines[j].isNotEmpty()) {
                val lineParts = lines[j].split(" ")
                when (lineParts[0]) {
                    "COL" -> color = lineParts[1].toInt()
                    "POS" -> {
                        roomId = lineParts[1]
                        val position = lineParts[2].split(",")
                        x = position[0].toInt()
                        y = position[1].toInt()
                    }
                    "DLG" -> dialogId = lineParts[1]
                    "NAME" -> {
                        name = Regex("\\s(.+)").find(lines[j])?.value
                    }
                    "ITM" -> {
                        val itemId = lineParts[1]
                        val itemCount = lineParts[2].toFloat().toInt()
                        inventory[itemId] = itemCount
                    }
                }
                j++
            }

            return BitsySprite(
                id,
                drawing,
                name,
                dialogId,
                roomId,
                x,
                y,
                inventory,
                color
            )
        }

        fun size(lines: List<String>, i: Int): Int {
            var j = i
            j++ // id
            if (lines[j].split(" ")[0] == "DRW") {
                j++
            } else {
                j += BitsyDrawing.size(lines, j, "SPR_n")
            }
            while (j < lines.size && lines[j].isNotEmpty()) {
                j++
            }
            return j - i
        }

    }

}
