/**
 * Copyright 2020 javakam
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ando.player.dialog

interface IPlayerCallBack {
    fun onItemClick()
    fun onListItemClick(item: String,position:Int)
    fun onSpeedItemClick(speedType: Int, speed: Float, name: String?)
    fun onDefinitionItemClick(definition: Int, name: String?, isSmallDefinitionSetChange: Boolean)
    fun onTimingItemClick(timing: Int, isSmallTimingSetChange: Boolean)
    fun showSmallTimingLayout()
    fun showSmallDefinitionLayout()
    fun showSmallSpreedLayout()
    fun showSmallRouteLayout()
}