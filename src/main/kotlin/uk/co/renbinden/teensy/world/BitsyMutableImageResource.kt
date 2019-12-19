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

interface BitsyMutableImageResource : BitsyImageResource {

    override var drawing: BitsyDrawing

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

}