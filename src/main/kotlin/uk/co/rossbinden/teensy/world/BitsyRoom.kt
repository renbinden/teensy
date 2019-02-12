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

package uk.co.rossbinden.teensy.world

import uk.co.rossbinden.teensy.world.room.BitsyRoomEnding
import uk.co.rossbinden.teensy.world.room.BitsyRoomExit
import uk.co.rossbinden.teensy.world.room.BitsyRoomItem

class BitsyRoom(
    override var id: String,
    var name: String?,
    val tileIds: MutableList<MutableList<String?>>,
    val wallIds: MutableList<String>,
    val items: MutableList<BitsyRoomItem>,
    val exits: MutableList<BitsyRoomExit>,
    val endings: MutableList<BitsyRoomEnding>,
    var paletteId: String
): BitsySerializable, BitsyResource {

    constructor(id: String, name: String?, tiles: MutableList<MutableList<BitsyTile?>>, walls: MutableList<BitsyTile>,
                items: MutableList<BitsyRoomItem>, exits: MutableList<BitsyRoomExit>,
                endings: MutableList<BitsyRoomEnding>, palette: BitsyPalette):
            this(id, name, tiles.map { row -> row.map { tile -> tile?.id }.toMutableList() }.toMutableList(),
                walls.map { wall -> wall.id }.toMutableList(), items, exits, endings, palette.id)

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("ROOM $id\n")
        for (row in tileIds) {
            output.append(row.joinToString(",") { tile -> tile ?: "0" }).append('\n')
        }
        if (name != null) {
            output.append("NAME $name\n")
        }
        if (wallIds.isNotEmpty()) {
            output.append("WAL ").append(wallIds.joinToString(",")).append('\n')
        }
        for (item in items) {
            output.append(item.serialize())
        }
        for (exit in exits) {
            output.append(exit.serialize())
        }
        for (ending in endings) {
            output.append(ending.serialize())
        }
        output.append("PAL $paletteId\n")
        output.append('\n')
        return output.toString()
    }

    fun placeSprites(
        lines: List<String>,
        i: Int,
        roomFormat: Int,
        sprites: List<BitsySprite>
    ) {
        var j = i
        j++
        j += 16
        while (j < lines.size && lines[j].isNotEmpty()) {
            val lineParts = lines[j].split(" ")
            if (lineParts[0] == "SPR") {
                if (!lineParts[1].contains(",") && lineParts.size >= 3) {
                    val spriteId = lineParts[1]
                    val position = lineParts[2].split(",")
                    val x = position[0].toInt()
                    val y = position[1].toInt()
                    sprites.filter { sprite -> sprite.id == spriteId }.forEach { sprite ->
                        sprite.roomId = id
                        sprite.x = x
                        sprite.y = y
                    }
                } else if (roomFormat == 0) {
                    val spriteIdList = lineParts[1].split(",")
                    for ((k, row) in tileIds.withIndex()) {
                        for (spriteId in spriteIdList) {
                            val col = row.indexOf(spriteId)
                            if (col != -1) {
                                row[col] = "0"
                                sprites.filter { sprite -> sprite.id == spriteId }.forEach { sprite ->
                                    sprite.roomId = id
                                    sprite.x = col
                                    sprite.y = k
                                }
                            }
                        }
                    }
                }
            }
            j++
        }
    }

    companion object {

        fun deserialize(
            lines: List<String>,
            i: Int,
            roomFormat: Int
        ): BitsyRoom {
            var j = i
            val id = lines[j++].split(" ")[1]
            var name: String? = null
            val tiles = mutableListOf<MutableList<String?>>()
            val walls = mutableListOf<String>()
            val items = mutableListOf<BitsyRoomItem>()
            val exits = mutableListOf<BitsyRoomExit>()
            val endings = mutableListOf<BitsyRoomEnding>()
            var paletteId = "0"

            val sprites = mutableListOf<String>()
            if (roomFormat == 0) {
                val end = j + 16
                var y = 0
                while (j < end) {
                    tiles[y] = mutableListOf()
                    for (x in 0..15) {
                        tiles[y].add(lines[j][x].toString())
                    }
                    y++
                    j++
                }
            } else if (roomFormat == 1) {
                val end = j + 16
                var y = 0
                while (j < end) {
                    tiles.add(y, mutableListOf())
                    val lineParts = lines[j].split(",")
                    for (x in 0..15) {
                        tiles[y].add(x, lineParts[x])
                    }
                    y++
                    j++
                }
            }

            while (j < lines.size && lines[j].isNotEmpty()) {
                val lineParts = lines[j].split(" ")
                val type = lineParts[0]
                if (type == "SPR") {
                    if (!lineParts[1].contains(",") && lineParts.size >= 3) {
                        sprites.add(lineParts[1])
                    } else if (roomFormat == 0) {
                        val spriteIdList = lineParts[1].split(",")
                        for (row in tiles) {
                            for (spriteId in spriteIdList) {
                                val col = row.indexOf(spriteId)
                                if (col != -1) {
                                    sprites.add(spriteId)
                                }
                            }
                        }
                    }
                } else if (type == "ITM") {
                    val itemId = lineParts[1]
                    val itemPosition = lineParts[2].split(",")
                    items.add(BitsyRoomItem(itemId, itemPosition[0].toInt(), itemPosition[1].toInt()))
                } else if (type == "WAL") {
                    walls.addAll(lineParts[1].split(","))
                } else if (type == "EXT") {
                    val exitPosition = lineParts[1].split(",")
                    val destRoomId = lineParts[2]
                    val destPosition = lineParts[3].split(",")
                    val exit = BitsyRoomExit(
                        exitPosition[0].toInt(),
                        exitPosition[1].toInt(),
                        destRoomId,
                        destPosition[0].toInt(),
                        destPosition[1].toInt()
                    )
                    exits.add(exit)
                } else if (type == "END") {
                    val endId = lineParts[1]
                    val endPosition = lineParts[2]
                    val roomEnding = BitsyRoomEnding(
                        endId,
                        endPosition[0].toInt(),
                        endPosition[1].toInt()
                    )
                    endings.add(roomEnding)
                } else if (type == "PAL") {
                    paletteId = lineParts[1]
                } else if (type == "NAME") {
                    name = Regex("\\s(.+)").find(lines[j])?.toString() ?: ""
                }
                j++
            }

            return BitsyRoom(
                id,
                name,
                tiles,
                walls,
                items,
                exits,
                endings,
                paletteId
            )
        }

        fun size(lines: List<String>, i: Int): Int {
            var j = i
            j++ // id
            j += 16 // tiles
            while (j < lines.size && lines[j].isNotEmpty()) {
                j++ // sprites, items, walls, exits, endings, palette, name
            }
            return j - i
        }

    }

}
