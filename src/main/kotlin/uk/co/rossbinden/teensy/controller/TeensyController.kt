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

package uk.co.rossbinden.teensy.controller

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.input.MouseButton.PRIMARY
import javafx.scene.input.MouseEvent
import javafx.stage.FileChooser
import uk.co.rossbinden.teensy.Teensy
import uk.co.rossbinden.teensy.controller.tab.*
import uk.co.rossbinden.teensy.exception.MissingResourceException
import uk.co.rossbinden.teensy.util.IdGenerator
import uk.co.rossbinden.teensy.world.*


class TeensyController {

    lateinit var teensy: Teensy

    val paletteIdGenerator = IdGenerator()
    val roomIdGenerator = IdGenerator()
    val endingIdGenerator = IdGenerator()
    val tileIdGenerator = IdGenerator()
    val spriteIdGenerator = IdGenerator()
    val itemIdGenerator = IdGenerator()
    val dialogIdGenerator = IdGenerator()

    @FXML lateinit var menuItemNew: MenuItem
    @FXML lateinit var menuItemOpen: MenuItem
    @FXML lateinit var menuItemSave: MenuItem

    @FXML lateinit var tree: TreeView<String>

    @FXML lateinit var palettes: TreeItem<String>
    @FXML lateinit var rooms: TreeItem<String>
    @FXML lateinit var endings: TreeItem<String>
    @FXML lateinit var tiles: TreeItem<String>
    @FXML lateinit var sprites: TreeItem<String>
    @FXML lateinit var items: TreeItem<String>
    @FXML lateinit var dialogs: TreeItem<String>
    @FXML lateinit var settings: TreeItem<String>

    @FXML lateinit var tabs: TabPane

    fun init(teensy: Teensy) {
        this.teensy = teensy
        initMenuItems()
        initContextMenus()
        initActions()
    }

    fun initMenuItems() {
        menuItemNew.onAction = EventHandler<ActionEvent> { event ->
            teensy.world = BitsyWorld(false)
        }
        menuItemOpen.onAction = EventHandler<ActionEvent> { event ->
            val fileChooser = FileChooser()
            fileChooser.title = "Open"
            val bitsyFilter = FileChooser.ExtensionFilter("Bitsy files (*.bitsy)", "*.bitsy")
            fileChooser.extensionFilters.add(bitsyFilter)
//            val htmlFilter = FileChooser.ExtensionFilter("HTML files (*.html)", "*.html")
//            fileChooser.extensionFilters.add(htmlFilter)
            val file = fileChooser.showOpenDialog(teensy.stage)
            if (file != null) {
                teensy.world = BitsyWorld.open(file)
            }
        }
        menuItemSave.onAction = EventHandler<ActionEvent> { event ->
            val fileChooser = FileChooser()
            fileChooser.title = "Save"
            val bitsyFilter = FileChooser.ExtensionFilter("Bitsy files (*.bitsy)", "*.bitsy")
            fileChooser.extensionFilters.add(bitsyFilter)
//            val htmlFilter = FileChooser.ExtensionFilter("HTML files (*.html)", "*.html")
//            fileChooser.extensionFilters.add(htmlFilter)
            val file = fileChooser.showSaveDialog(teensy.stage)
            if (file != null) {
                teensy.world.save(file)
            }
        }
    }

    fun initContextMenus() {
        val paletteContextMenu = createPaletteContextMenu()
        val roomContextMenu = createRoomContextMenu()
        val endingContextMenu = createEndingContextMenu()
        val tileContextMenu = createTileContextMenu()
        val spriteContextMenu = createSpriteContextMenu()
        val itemContextMenu = createItemContextMenu()
        val dialogsContextMenu = createDialogsContextMenu()

        val contextMenus = FXCollections.observableMap(mapOf(
            Pair(palettes, paletteContextMenu),
            Pair(rooms, roomContextMenu),
            Pair(endings, endingContextMenu),
            Pair(tiles, tileContextMenu),
            Pair(sprites, spriteContextMenu),
            Pair(items, itemContextMenu),
            Pair(dialogs, dialogsContextMenu)
        ))

        tree.contextMenuProperty().bind(
            Bindings.valueAt(
                contextMenus,
                tree.selectionModel.selectedItemProperty()
            )
        )
    }

    private fun createPaletteContextMenu(): ContextMenu {
        val paletteContextMenu = ContextMenu(
            createCreatePaletteMenuItem()
        )
        return paletteContextMenu
    }

    private fun createCreatePaletteMenuItem(): MenuItem {
        val createPalette = MenuItem("Create palette")
        createPalette.onAction = EventHandler<ActionEvent> { event ->
            var paletteId = paletteIdGenerator.nextId
            while (teensy.world.getPalette(paletteId) != null) {
                paletteId = paletteIdGenerator.nextId
            }
            val newPalette = BitsyPalette(
                paletteId,
                null,
                mutableListOf(
                    BitsyColor(255, 255, 255),
                    BitsyColor(255, 255, 255),
                    BitsyColor(255, 255, 255)
                )
            )
            teensy.world.palettes.add(newPalette)
            palettes.children.add(TreeItem<String>(newPalette.id))
            palettes.isExpanded = true
        }
        return createPalette
    }

    private fun createRoomContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateRoomMenuItem()
        )
    }

    private fun createCreateRoomMenuItem(): MenuItem {
        val createRoom = MenuItem("Create room")
        createRoom.onAction = EventHandler<ActionEvent> { event ->
            var roomId = roomIdGenerator.nextId
            while (teensy.world.getRoom(roomId) != null) {
                roomId = roomIdGenerator.nextId
            }
            val newRoom = BitsyRoom(
                roomId,
                null,
                mutableListOf(
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
                    mutableListOf<BitsyTile?>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
                ),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                teensy.world.palettes[0]
            )
            teensy.world.rooms.add(newRoom)
            rooms.children.add(TreeItem<String>(newRoom.id))
            rooms.isExpanded = true
        }
        return createRoom
    }

    private fun createEndingContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateEndingMenuItem()
        )
    }

    private fun createCreateEndingMenuItem(): MenuItem {
        val createEnding = MenuItem("Create ending")
        createEnding.onAction = EventHandler<ActionEvent> { event ->
            var endingId = endingIdGenerator.nextId
            while (teensy.world.getEnding(endingId) != null) {
                endingId = endingIdGenerator.nextId
            }
            val newEnding = BitsyEnding(
                endingId,
                ""
            )
            teensy.world.endings.add(newEnding)
            endings.children.add(TreeItem<String>(endingId))
            endings.isExpanded = true
        }
        return createEnding
    }

    private fun createTileContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateTileMenuItem()
        )
    }

    private fun createCreateTileMenuItem(): MenuItem {
        val createTile = MenuItem("Create tile")
        createTile.onAction = EventHandler<ActionEvent> { event ->
            var tileId = tileIdGenerator.nextId
            while (teensy.world.getTile(tileId) != null) {
                tileId = tileIdGenerator.nextId
            }
            val newTile = BitsyTile(
                tileId,
                BitsyDrawing(
                    "TIL_$tileId",
                    mutableListOf(
                        BitsyImageData(
                            mutableListOf(
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false)
                            )
                        )
                    )
                ),
                null,
                false,
                null
            )
            teensy.world.tiles.add(newTile)
            tiles.children.add(TreeItem<String>(tileId))
            tiles.isExpanded = true
        }
        return createTile
    }

    private fun createSpriteContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateSpriteMenuItem()
        )
    }

    private fun createCreateSpriteMenuItem(): MenuItem {
        val createSprite = MenuItem("Create sprite")
        createSprite.onAction = EventHandler<ActionEvent> { event ->
            var spriteId = spriteIdGenerator.nextId
            while (teensy.world.getSprite(spriteId) != null) {
                spriteId = spriteIdGenerator.nextId
            }
            val newSprite = BitsySprite(
                spriteId,
                BitsyDrawing(
                    "SPR_$spriteId",
                    mutableListOf(
                        BitsyImageData(
                            mutableListOf(
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false)
                            )
                        )
                    )
                ),
                null,
                null,
                null,
                null,
                null,
                linkedMapOf<BitsyItem, Int>(),
                null
            )
            teensy.world.sprites.add(newSprite)
            sprites.children.add(TreeItem<String>(spriteId))
            sprites.isExpanded = true
        }
        return createSprite
    }

    private fun createItemContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateItemMenuItem()
        )
    }

    private fun createCreateItemMenuItem(): MenuItem {
        val createItem = MenuItem("Create item")
        createItem.onAction = EventHandler<ActionEvent> { event ->
            var itemId = itemIdGenerator.nextId
            while (teensy.world.getItem(itemId) != null) {
                itemId = itemIdGenerator.nextId
            }
            val newItem = BitsyItem(
                itemId,
                BitsyDrawing(
                    "ITM_$itemId",
                    mutableListOf(
                        BitsyImageData(
                            mutableListOf(
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false),
                                mutableListOf(false, false, false, false, false, false, false, false)
                            )
                        )
                    )
                ),
                null,
                null,
                null
            )
            teensy.world.items.add(newItem)
            items.children.add(TreeItem<String>(itemId))
            items.isExpanded = true
        }
        return createItem
    }

    private fun createDialogsContextMenu(): ContextMenu {
        return ContextMenu(
            createCreateDialogMenuItem()
        )
    }

    private fun createCreateDialogMenuItem(): MenuItem {
        val createDialog = MenuItem("Create dialog")
        createDialog.onAction = EventHandler<ActionEvent> { event ->
            var dialogId = dialogIdGenerator.nextId
            while (teensy.world.getDialog(dialogId) != null) {
                dialogId = dialogIdGenerator.nextId
            }
            val newDialog = BitsyDialog(dialogId, "")
            teensy.world.dialogs.add(newDialog)
            dialogs.children.add(TreeItem<String>(newDialog.id))
            dialogs.isExpanded = true
        }
        return createDialog
    }

    private fun initActions() {
        tree.onMouseClicked = EventHandler<MouseEvent> { event ->
            if (event.clickCount == 2 && event.button == PRIMARY) {
                val item = tree.selectionModel.selectedItem
                if (item != null) {
                    when {
                        item.parent == palettes -> {
                            val paletteTab = loadPaletteTab(item.value)
                            tabs.tabs.add(paletteTab)
                            tabs.selectionModel.select(paletteTab)
                        }
                        item.parent == rooms -> {
                            val roomTab = loadRoomTab(item.value)
                            tabs.tabs.add(roomTab)
                            tabs.selectionModel.select(roomTab)
                        }
                        item.parent == endings -> {
                            val endingTab = loadEndingTab(item.value)
                            tabs.tabs.add(endingTab)
                            tabs.selectionModel.select(endingTab)
                        }
                        item.parent == tiles -> {
                            val itemTab = loadTileTab(item.value)
                            tabs.tabs.add(itemTab)
                            tabs.selectionModel.select(itemTab)
                        }
                        item.parent == sprites -> {
                            val spriteTab = loadSpriteTab(item.value)
                            tabs.tabs.add(spriteTab)
                            tabs.selectionModel.select(spriteTab)
                        }
                        item.parent == items -> {
                            val itemTab = loadItemTab(item.value)
                            tabs.tabs.add(itemTab)
                            tabs.selectionModel.select(itemTab)
                        }
                        item.parent == dialogs -> {
                            val dialogTab = loadDialogTab(item.value)
                            tabs.tabs.add(dialogTab)
                            tabs.selectionModel.select(dialogTab)
                        }
                        item == settings -> {
                            val settingsTab = loadSettingsTab()
                            tabs.tabs.add(settingsTab)
                            tabs.selectionModel.select(settingsTab)
                        }
                    }
                }
            }
        }
    }

    private fun loadPaletteTab(id: String): Tab {
        val palette = teensy.world.getPalette(id) ?: throw MissingResourceException("Palette", id)
        val loader = FXMLLoader(javaClass.getResource("/tabs/palette.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Palette $id"
        val paletteTab = loader.getController<PaletteTab>()
        paletteTab.init(teensy)
        paletteTab.loadPalette(palette)
        return tab
    }

    private fun loadRoomTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/room.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Room $id"
        val roomTab = loader.getController<RoomTab>()
        roomTab.init(teensy)
        roomTab.loadRoom(teensy.world.getRoom(id) ?: throw MissingResourceException("Room", id))
        return tab
    }

    private fun loadEndingTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/ending.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Ending $id"
        val endingTab = loader.getController<EndingTab>()
        endingTab.init(teensy)
        endingTab.loadEnding(teensy.world.getEnding(id) ?: throw MissingResourceException("Ending", id))
        return tab
    }

    private fun loadTileTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/tile.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Tile $id"
        val tileTab = loader.getController<TileTab>()
        tileTab.init(teensy)
        tileTab.loadTile(teensy.world.getTile(id) ?: throw MissingResourceException("Tile", id))
        return tab
    }

    private fun loadSpriteTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/sprite.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Sprite $id"
        val spriteTab = loader.getController<SpriteTab>()
        spriteTab.init(teensy)
        spriteTab.loadSprite(teensy.world.getSprite(id) ?: throw MissingResourceException("Sprite", id))
        return tab
    }

    private fun loadItemTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/item.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Item $id"
        val itemTab = loader.getController<ItemTab>()
        itemTab.init(teensy)
        itemTab.loadItem(teensy.world.getItem(id) ?: throw MissingResourceException("Item", id))
        return tab
    }

    private fun loadDialogTab(id: String): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/dialog.fxml"))
        val tab = loader.load<Tab>()
        tab.text = "Dialog $id"
        val dialogTab = loader.getController<DialogTab>()
        dialogTab.init(teensy)
        dialogTab.loadDialog(teensy.world.getDialog(id) ?: throw MissingResourceException("Dialog", id))
        return tab
    }

    private fun loadSettingsTab(): Tab {
        val loader = FXMLLoader(javaClass.getResource("/tabs/settings.fxml"))
        val tab = loader.load<Tab>()
        val settingsTab = loader.getController<SettingsTab>()
        settingsTab.title.text = teensy.world.title.text
        settingsTab.bitsyVersion.text = teensy.world.version.version
        return tab
    }

    fun reloadWorld() {
        tabs.tabs.clear()
        palettes.isExpanded = false
        rooms.isExpanded = false
        endings.isExpanded = false
        tiles.isExpanded = false
        sprites.isExpanded = false
        items.isExpanded = false
        dialogs.isExpanded = false
        palettes.children.clear()
        rooms.children.clear()
        endings.children.clear()
        tiles.children.clear()
        sprites.children.clear()
        items.children.clear()
        dialogs.children.clear()
        teensy.world.palettes.forEach { palette -> palettes.children.add(TreeItem<String>(palette.id)) }
        teensy.world.rooms.forEach { room -> rooms.children.add(TreeItem<String>(room.id)) }
        teensy.world.endings.forEach { ending -> endings.children.add(TreeItem<String>(ending.id)) }
        teensy.world.tiles.forEach { tile -> tiles.children.add(TreeItem<String>(tile.id)) }
        teensy.world.sprites.forEach { sprite -> sprites.children.add(TreeItem<String>(sprite.id)) }
        teensy.world.items.forEach { item -> items.children.add(TreeItem<String>(item.id)) }
        teensy.world.dialogs.forEach { dialog -> dialogs.children.add(TreeItem<String>(dialog.id)) }
        paletteIdGenerator.nextIdInt = 0
        roomIdGenerator.nextIdInt = 0
        endingIdGenerator.nextIdInt = 0
        tileIdGenerator.nextIdInt = 0
        spriteIdGenerator.nextIdInt = 0
        itemIdGenerator.nextIdInt = 0
        dialogIdGenerator.nextIdInt = 0
    }

}