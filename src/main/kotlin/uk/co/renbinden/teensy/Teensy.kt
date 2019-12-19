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

package uk.co.renbinden.teensy

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import uk.co.renbinden.teensy.controller.TeensyController
import uk.co.renbinden.teensy.world.BitsyWorld

class Teensy: Application() {

    lateinit var stage: Stage
    lateinit var controller: TeensyController

    var world = BitsyWorld(false)
        set(value) {
            field = value
            controller.reloadWorld()
        }

    override fun start(stage: Stage) {
        this.stage = stage
        val loader = FXMLLoader(javaClass.getResource("/teensy.fxml"))
        val root = loader.load<BorderPane>()
        controller = loader.getController<TeensyController>()
        controller.init(this)
        controller.reloadWorld()
        val scene = Scene(root)
        scene.stylesheets.add(javaClass.getResource("/css/styles.css").toExternalForm())
        stage.scene = scene
        stage.title = "Teensy"
        stage.minWidth = 1280.0
        stage.minHeight = 720.0
        stage.show()
    }

}