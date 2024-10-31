package com.catharinafrindt.platformer

const val PLAYER = "blue_left1"
const val NULLSPRITE = "nullsprite"
const val NO_TILE = 0


abstract class LevelData {
    var tiles: Array<IntArray> = emptyArray()
    var tileToBitmap = HashMap<Int, String>()
    var height = 0
    var width = 0

    fun getRow(y: Int) : IntArray {
        return tiles[y]
    }
    fun getTile(x: Int, y: Int) : Int {
        return getRow(y)[x];
    }
    fun getSpriteName(tileType: Int) : String {
        val fileName = tileToBitmap[tileType]
        return fileName ?: NULLSPRITE
    }
    fun updateLevelDimension() {
        height = tiles.size
        width = getRow(0).size

    }
}