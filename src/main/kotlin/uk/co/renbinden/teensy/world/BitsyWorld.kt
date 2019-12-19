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

import java.io.File


class BitsyWorld : BitsySerializable {

    val title: BitsyTitle
    val version: BitsyVersion
    val flags: MutableList<BitsyFlag>
    val font: BitsyFont
    val palettes: MutableList<BitsyPalette>
    val rooms: MutableList<BitsyRoom>
    val tiles: MutableList<BitsyTile>
    val sprites: MutableList<BitsySprite>
    val items: MutableList<BitsyItem>
    val drawings: MutableList<BitsyDrawing>
    val dialogs: MutableList<BitsyDialog>
    val endings: MutableList<BitsyEnding>
    val variables: MutableList<BitsyVariable>

    constructor(
        title: BitsyTitle,
        version: BitsyVersion,
        flags: MutableList<BitsyFlag>,
        font: BitsyFont,
        palettes: MutableList<BitsyPalette>,
        rooms: MutableList<BitsyRoom>,
        tiles: MutableList<BitsyTile>,
        sprites: MutableList<BitsySprite>,
        items: MutableList<BitsyItem>,
        drawings: MutableList<BitsyDrawing>,
        dialogs: MutableList<BitsyDialog>,
        endings: MutableList<BitsyEnding>,
        variables: MutableList<BitsyVariable>
    ) {
        this.title = title
        this.version = version
        this.flags = flags
        this.font = font
        this.palettes = palettes
        this.rooms = rooms
        this.tiles = tiles
        this.sprites = sprites
        this.items = items
        this.drawings = drawings
        this.dialogs = dialogs
        this.endings = endings
        this.variables = variables
    }

    constructor(empty: Boolean = true) {
        if (empty) {
            this.title = BitsyTitle("")
            this.version = BitsyVersion("5.4")
            this.flags = mutableListOf()
            this.font = BitsyFont("ascii_small")
            this.palettes = mutableListOf()
            this.rooms = mutableListOf()
            this.tiles = mutableListOf()
            this.sprites = mutableListOf()
            this.items = mutableListOf()
            this.drawings = mutableListOf()
            this.dialogs = mutableListOf()
            this.endings = mutableListOf()
            this.variables = mutableListOf()
        } else {
            this.title = BitsyTitle("")
            this.version = BitsyVersion("5.4")
            this.flags = mutableListOf(
                BitsyFlag("ROOM_FORMAT", 1)
            )
            this.font = BitsyFont("ascii_small")
            this.palettes = mutableListOf(
                BitsyPalette(
                    "0",
                    null,
                    mutableListOf(
                        BitsyColor(0, 82, 204),
                        BitsyColor(128, 159, 255),
                        BitsyColor(255, 255, 255)
                    )
                )
            )
            this.tiles = mutableListOf(
                BitsyTile(
                    "a",
                    BitsyDrawing(
                        "TIL_a",
                        mutableListOf(
                            BitsyImageData(
                                mutableListOf(
                                    mutableListOf(true, true,  true,  true,  true,  true,  true,  true),
                                    mutableListOf(true, false, false, false, false, false, false, true),
                                    mutableListOf(true, false, false, false, false, false, false, true),
                                    mutableListOf(true, false, false, true,  true,  false, false, true),
                                    mutableListOf(true, false, false, true,  true,  false, false, true),
                                    mutableListOf(true, false, false, false, false, false, false, true),
                                    mutableListOf(true, false, false, false, false, false, false, true),
                                    mutableListOf(true, true,  true,  true,  true,  true,  true,  true)
                                )
                            )
                        )
                    ),
                    null,
                    false,
                    null
                )
            )
            this.rooms = mutableListOf(
                BitsyRoom(
                    "0",
                    null,
                    mutableListOf(
                        mutableListOf<BitsyTile?>(null, null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null),
                        mutableListOf<BitsyTile?>(null, tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     tiles[0], null),
                        mutableListOf<BitsyTile?>(null, tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], tiles[0], null),
                        mutableListOf<BitsyTile?>(null, null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null,     null)
                    ),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    palettes[0]
                )
            )
            this.dialogs = mutableListOf(
                BitsyDialog("SPR_0", "I'm a cat"),
                BitsyDialog("ITM_0", "You found a nice warm cup of tea")
            )
            this.sprites = mutableListOf(
                BitsySprite(
                    "A",
                    BitsyDrawing(
                        "SPR_A",
                        mutableListOf(
                            BitsyImageData(
                                mutableListOf(
                                    mutableListOf(false, false, false, true,  true,  false, false, false),
                                    mutableListOf(false, false, false, true,  true,  false, false, false),
                                    mutableListOf(false, false, false, true,  true,  false, false, false),
                                    mutableListOf(false, false, true,  true,  true,  true,  false, false),
                                    mutableListOf(false, true,  true,  true,  true,  true,  true,  false),
                                    mutableListOf(true,  false, true,  true,  true,  true,  false, true),
                                    mutableListOf(false, false, true,  false, false, true,  false, false),
                                    mutableListOf(false, false, true,  false, false, true,  false, false)
                                )
                            )
                        )
                    ),
                    null,
                    null,
                    rooms[0],
                    4,
                    4,
                    linkedMapOf(),
                    null
                ),
                BitsySprite(
                    "a",
                    BitsyDrawing(
                        "SPR_a",
                        mutableListOf(
                            BitsyImageData(
                                mutableListOf(
                                    mutableListOf(false, false, false, false, false, false, false, false),
                                    mutableListOf(false, false, false, false, false, false, false, false),
                                    mutableListOf(false, true,  false, true,  false, false, false, true ),
                                    mutableListOf(false, true,  true,  true,  false, false, false, true ),
                                    mutableListOf(false, true,  true,  true,  false, false, true,  false),
                                    mutableListOf(false, true,  true,  true,  true,  true,  false, false),
                                    mutableListOf(false, false, true,  true,  true,  true,  false, false),
                                    mutableListOf(false, false, true,  false, false, true,  false, false)
                                )
                            )
                        )
                    ),
                    null,
                    dialogs[0],
                    rooms[0],
                    8,
                    12,
                    linkedMapOf(),
                    null
                )
            )
            this.items = mutableListOf(
                BitsyItem(
                    "0",
                    BitsyDrawing(
                        "ITM_0",
                        mutableListOf(
                            BitsyImageData(
                                mutableListOf(
                                    mutableListOf(false, false, false, false, false, false, false, false),
                                    mutableListOf(false, false, false, false, false, false, false, false),
                                    mutableListOf(false, false, false, false, false, false, false, false),
                                    mutableListOf(false, false, true,  true,  true,  true,  false, false),
                                    mutableListOf(false, true,  true,  false, false, true,  false, false),
                                    mutableListOf(false, false, true,  false, false, true,  false, false),
                                    mutableListOf(false, false, false, true,  true,  false, false, false),
                                    mutableListOf(false, false, false, false, false, false, false, false)
                                )
                            )
                        )
                    ),
                    "tea",
                    dialogs[1],
                    null
                )
            )
            this.drawings = mutableListOf()
            this.endings = mutableListOf()
            this.variables = mutableListOf(
                BitsyVariable("a", "42")
            )
        }
    }

    fun getFlag(name: String): BitsyFlag? {
        return flags.firstOrNull { flag -> flag.name == name }
    }

    fun getFlagValue(name: String): Int? {
        return getFlag(name)?.value
    }

    fun setFlagValue(name: String, value: Int) {
        var flag = getFlag(name)
        if (flag == null) {
            flag = BitsyFlag(name, value)
            flags.add(flag)
        } else {
            flag.value = value
        }
    }

    fun getPalette(id: String): BitsyPalette? {
        return palettes.firstOrNull { palette -> palette.id == id }
    }

    fun getRoom(id: String): BitsyRoom? {
        return rooms.firstOrNull { room -> room.id == id }
    }

    fun getTile(id: String): BitsyTile? {
        return tiles.firstOrNull { tile -> tile.id == id }
    }

    fun getSprite(id: String): BitsySprite? {
        return sprites.firstOrNull { sprite -> sprite.id == id}
    }

    fun getItem(id: String): BitsyItem? {
        return items.firstOrNull { item -> item.id == id }
    }

    fun getDialog(id: String): BitsyDialog? {
        return dialogs.firstOrNull { dialog -> dialog.id == id }
    }

    fun getEnding(id: String): BitsyEnding? {
        return endings.firstOrNull { ending -> ending.id == id }
    }

    fun getVariable(id: String): BitsyVariable? {
        return variables.firstOrNull { variable -> variable.id == id }
    }

    override fun serialize(): String {
        val output = StringBuilder()
        output.append(title.serialize()).append("\n")
            .append(version.serialize()).append("\n")
            .append(flags.joinToString(transform = BitsyFlag::serialize, separator = "")).append("\n")
            .append(font.serialize()).append("\n")
            .append(palettes.joinToString(transform = BitsyPalette::serialize, separator = ""))
            .append(rooms.joinToString(transform = BitsyRoom::serialize, separator = ""))
            .append(tiles.joinToString(transform = BitsyTile::serialize, separator = ""))
            .append(sprites.joinToString(transform = BitsySprite::serialize, separator = ""))
            .append(items.joinToString(transform = BitsyItem::serialize, separator = ""))
            .append(dialogs.joinToString(transform = BitsyDialog::serialize, separator = ""))
            .append(endings.joinToString(transform = BitsyEnding::serialize, separator = ""))
            .append(variables.joinToString(transform = BitsyVariable::serialize, separator = ""))
        return output.toString()
    }

    fun save(file: File) {
        file.writeText(serialize())
    }

    companion object {

        fun open(file: File): BitsyWorld {
            val input = file.readText()
            val lines = input.split("\n")
            var i = 0

            var title = BitsyTitle("")
            var version = BitsyVersion("0")
            val flags = mutableListOf<BitsyFlag>()
            var font = BitsyFont("")
            val palettes = mutableListOf<BitsyPalette>()
            val rooms = mutableListOf<BitsyRoom>()
            val tiles = mutableListOf<BitsyTile>()
            val sprites = mutableListOf<BitsySprite>()
            val items = mutableListOf<BitsyItem>()
            val drawings = mutableListOf<BitsyDrawing>()
            val dialog = mutableListOf<BitsyDialog>()
            val endings = mutableListOf<BitsyEnding>()
            val variables = mutableListOf<BitsyVariable>()

            while (i < lines.size) {
                val line = lines[i]

                if (i == 0) {
                    title = BitsyTitle.deserialize(lines, i)
                    i++
                } else if (line.isEmpty() || line[0] == '#') {
                    if (line.startsWith("# BITSY VERSION ")) {
                        version = BitsyVersion.deserialize(lines, i)
                    }
                    i++
                } else when (line.split(" ")[0]) {
                    "PAL" -> {
                        palettes.add(BitsyPalette.deserialize(lines, i))
                        i += BitsyPalette.size(lines, i)
                    }
                    "ROOM", "SET" -> {
                        rooms.add(
                            BitsyRoom.deserialize(
                                lines,
                                i,
                                flags.firstOrNull { flag -> flag.name == "ROOM_FORMAT" }?.value ?: 1
                            )
                        )
                        i += BitsyRoom.size(lines, i)
                    }
                    "TIL" -> {
                        tiles.add(BitsyTile.deserialize(lines, i))
                        i += BitsyTile.size(lines, i)
                    }
                    "SPR" -> {
                        sprites.add(BitsySprite.deserialize(lines, i))
                        i += BitsySprite.size(lines, i)
                    }
                    "ITM" -> {
                        items.add(BitsyItem.deserialize(lines, i))
                        i += BitsyItem.size(lines, i)
                    }
                    "DRW" -> {
                        drawings.add(BitsyDrawing.deserialize(lines, i))
                        i += BitsyDrawing.size(lines, i)
                    }
                    "DLG" -> {
                        dialog.add(BitsyDialog.deserialize(lines, i))
                        i += BitsyDialog.size(lines, i)
                    }
                    "END" -> {
                        endings.add(BitsyEnding.deserialize(lines, i))
                        i += BitsyEnding.size(lines, i)
                    }
                    "VAR" -> {
                        variables.add(BitsyVariable.deserialize(lines, i))
                        i += BitsyVariable.size(lines, i)
                    }
                    "DEFAULT_FONT" -> {
                        font = BitsyFont.deserialize(lines, i)
                        i += BitsyFont.size(lines, i)
                    }
                    "TEXT_DIRECTION" -> {
                    }
                    "FONT" -> {
                    }
                    "!" -> {
                        flags.add(BitsyFlag.deserialize(lines, i))
                        i++
                    }
                    else -> i++
                }
            }
            // Second pass
            i = 0
            while (i < lines.size) {
                val line = lines[i]
                if (i == 0) {
                    i++
                } else if (line.isEmpty() || line[0] == '#') {
                    i++
                } else when (line.split(" ")[0]) {
                    "PAL" -> i += BitsyPalette.size(lines, i)
                    "ROOM", "SET" -> {
                        val roomId = lines[i].split(" ")[1]
                        rooms.first { room -> room.id == roomId }.placeSprites(lines, i, flags.firstOrNull { flag -> flag.name == "ROOM_FORMAT" }?.value ?: 1, sprites)
                        i += BitsyRoom.size(lines, i)
                    }
                    "TIL" -> {
                        val tileId = lines[i].split(" ")[1]
                        tiles.first { tile -> tile.id == tileId }.attachDrawing(lines, i, drawings)
                        i += BitsyTile.size(lines, i)
                    }
                    "SPR" -> {
                        val spriteId = lines[i].split(" ")[1]
                        sprites.first { sprite -> sprite.id == spriteId }.attachDrawing(lines, i, drawings)
                        i += BitsySprite.size(lines, i)
                    }
                    "ITM" -> {
                        val itemId = lines[i].split(" ")[1]
                        items.first { item -> item.id == itemId }.attachDrawing(lines, i, drawings)
                        i += BitsyItem.size(lines, i)
                    }
                    "DRW" -> {
                        i += BitsyDrawing.size(lines, i)
                    }
                    "DLG" -> {
                        i += BitsyDialog.size(lines, i)
                    }
                    "END" -> {
                        i += BitsyEnding.size(lines, i)
                    }
                    "VAR" -> {
                        i += BitsyVariable.size(lines, i)
                    }
                    "DEFAULT_FONT" -> {
                        i += BitsyFont.size(lines, i)
                    }
                    "TEXT_DIRECTION" -> {
                    }
                    "FONT" -> {
                    }
                    "!" -> i++
                    else -> i++
                }
            }
            // Place sprites after all of them have been created
            // Attach drawings to sprites, items and tiles that have existing drawings
            return BitsyWorld(
                title,
                version,
                flags,
                font,
                palettes,
                rooms,
                tiles,
                sprites,
                items,
                drawings,
                dialog,
                endings,
                variables
            )
        }

    }

}