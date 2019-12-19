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

package uk.co.renbinden.teensy.controller.tab

import javafx.fxml.FXML
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import uk.co.renbinden.teensy.Teensy
import uk.co.renbinden.teensy.world.BitsyDialog

class DialogTab {

    @FXML lateinit var id: TextField
    @FXML lateinit var dialog: TextArea

    fun init(teensy: Teensy) {
        id.textProperty().addListener { observable, oldValue, newValue ->
            val dialog = teensy.world.getDialog(oldValue)
            if (dialog != null) {
                dialog.id = newValue
                val dialogTreeItem = teensy.controller.dialogs
                val oldTreeItem = dialogTreeItem.children.firstOrNull { treeItem -> treeItem.value == oldValue }
                val index = dialogTreeItem.children.indexOf(oldTreeItem)
                dialogTreeItem.children.remove(oldTreeItem)
                dialogTreeItem.children.add(index, TreeItem<String>(newValue))
            }
        }

        dialog.textProperty().addListener { observable, oldValue, newValue ->
            val dialog = teensy.world.getDialog(id.text)
            if (dialog != null) {
                dialog.dialog = newValue
            }
        }
    }

    fun loadDialog(dialog: BitsyDialog) {
        id.text = dialog.id
        this.dialog.text = dialog.dialog
    }

}
