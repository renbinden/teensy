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

package uk.co.rossbinden.teensy.world.room

import uk.co.rossbinden.teensy.world.BitsyRoom
import uk.co.rossbinden.teensy.world.BitsySerializable

class BitsyRoomExit(
    val x: Int,
    val y: Int,
    val destinationRoomId: String,
    val destinationX: Int,
    val destinationY: Int
): BitsySerializable {

    constructor(x: Int, y: Int, destinationRoom: BitsyRoom, destinationX: Int, destinationY: Int):
            this(x, y, destinationRoom.id, destinationX, destinationY)

    override fun serialize(): String {
        return "EXT $x,$y $destinationRoomId $destinationX,$destinationY\n"
    }

}
