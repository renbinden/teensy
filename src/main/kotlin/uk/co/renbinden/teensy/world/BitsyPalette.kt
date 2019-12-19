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

class BitsyPalette(
    override var id: String,
    var name: String?,
    val colors: MutableList<BitsyColor>
): BitsySerializable, BitsyResource {

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("PAL $id\n")
        if (name != null) {
            output.append("NAME $name\n")
        }
        for (color in colors) {
            output.append(color.serialize())
        }
        output.append("\n")
        return output.toString()
    }

    companion object {

        fun deserialize(lines: List<String>, i: Int): BitsyPalette {
            var j = i
            val id = lines[j].split(" ")[1]
            j++
            val colors = mutableListOf<BitsyColor>()
            var name: String? = null
            while (j < lines.size && lines[j].isNotEmpty()) {
                val args = lines[j].split(" ")
                if (args[0] == "NAME") {
                    val nameMatchResult = Regex("\\s(.+)").find(lines[j])
                    name = nameMatchResult?.groupValues?.get(0)
                } else {
                    val colorParts = lines[j].split(",").map { part -> part.toInt() }
                    val color = BitsyColor(colorParts[0], colorParts[1], colorParts[2])
                    colors.add(color)
                }
                j++
            }
            return BitsyPalette(id, name, colors)
        }

        fun size(lines: List<String>, i: Int): Int {
            var j = i
            j++ // id
            while (j < lines.size && lines[j].isNotEmpty()) {
                j++ // name or color
            }
            return j - i
        }

    }

}
