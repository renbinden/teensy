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
import uk.co.renbinden.teensy.world.BitsyEnding

class EndingTab {
    
    @FXML lateinit var id: TextField
    @FXML lateinit var ending: TextArea
    
    fun init(teensy: Teensy) {
        id.textProperty().addListener { observable, oldValue, newValue ->
            val ending = teensy.world.getEnding(oldValue)
            if (ending != null) {
                ending.id = newValue
                val endingTreeItem = teensy.controller.endings
                val oldTreeItem = endingTreeItem.children.firstOrNull { treeItem -> treeItem.value == oldValue }
                val index = endingTreeItem.children.indexOf(oldTreeItem)
                endingTreeItem.children.remove(oldTreeItem)
                teensy.controller.palettes.children.add(index, TreeItem<String>(newValue))
            }
        }

        ending.textProperty().addListener { observable, oldValue, newValue ->
            val ending = teensy.world.getEnding(id.text)
            if (ending != null) {
                ending.ending = newValue
            }
        }
    }

    fun loadEnding(ending: BitsyEnding) {
        id.text = ending.id
        this.ending.text = ending.ending
    }

}
