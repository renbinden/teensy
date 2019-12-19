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

class BitsyVariable(
    override var id: String,
    val value: String
): BitsySerializable, BitsyResource {

    override fun serialize(): String {
        val output = StringBuilder()
        output.append("VAR $id\n")
            .append("$value\n")
            .append('\n')
        return output.toString()
    }

    companion object {
        fun deserialize(lines: List<String>, i: Int): BitsyVariable {
            var j = i
            val id = lines[j].split(" ")[1]
            j++
            val value = lines[j]
            return BitsyVariable(id, value)
        }

        fun size(lines: List<String>, i: Int): Int {
            return 2
        }
    }

}
