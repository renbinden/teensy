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

package uk.co.renbinden.teensy.world

class BitsyFont(
    val fontName: String
): BitsySerializable {

    override fun serialize(): String {
        return "DEFAULT_FONT $fontName\n"
    }

    companion object {
        fun deserialize(lines: List<String>, i: Int): BitsyFont {
            val fontName = lines[i].split(" ")[1]
            return BitsyFont(fontName)
        }

        fun size(lines: List<String>, i: Int): Int {
            return 1
        }
    }

}
