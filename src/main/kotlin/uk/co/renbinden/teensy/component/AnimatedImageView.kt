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

package uk.co.renbinden.teensy.component

import javafx.scene.image.Image
import javafx.scene.image.ImageView


class AnimatedImageView(val frames: List<Image>, val frameDuration: Float = 0.5f) : ImageView(frames[0]) {

    var frameTime = frameDuration
    var frameIndex = 0

    fun update(delta: Float) {
        frameTime -= delta
        while (frameTime < 0) {
            nextFrame()
            frameTime += frameDuration
        }
    }

    private fun nextFrame() {
        frameIndex = if (frameIndex < frames.size - 1) {
            frameIndex + 1
        } else {
            0
        }
        image = frames[frameIndex]
    }

}