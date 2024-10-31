package com.catharinafrindt.platformer

class TestLevel : LevelData() {
    init {
        tileToBitmap[NO_TILE] = "no_tile"
        tileToBitmap[1] = PLAYER
        tileToBitmap[2] = "ground_left"
        tileToBitmap[3] = "ground"
        tileToBitmap[4] = "ground_right"

        tiles = arrayOf(
            intArrayOf(2,3,4,0,1)
        )
        updateLevelDimension()
    }
}