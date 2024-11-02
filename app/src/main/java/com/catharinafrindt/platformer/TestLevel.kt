package com.catharinafrindt.platformer

class TestLevel : LevelData() {
    init {
        tileToBitmap[NO_TILE] = "no_tile"
        tileToBitmap[1] = PLAYER
        tileToBitmap[2] = "zigzaggrass_2roundleft"
        tileToBitmap[3] = "zigzaggrass_squar"
        tileToBitmap[4] = "zigzaggrass_2roundright"

        tiles = arrayOf(
            intArrayOf(2,0,1,0,4),
            intArrayOf(2, 3, 0, 3, 4),
            intArrayOf(2, 3, 0, 3, 4),
            intArrayOf(2, 3, 0, 3, 4),
            intArrayOf(2, 3, 0, 3, 4),
            intArrayOf(2,3,3,3,4)
        )
    }
}
