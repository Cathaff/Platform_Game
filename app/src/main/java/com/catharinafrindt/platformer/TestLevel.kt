package com.catharinafrindt.platformer

class TestLevel : LevelData() {
    init {
        tileToBitmap[NO_TILE] = "no_tile"
        tileToBitmap[1] = PLAYER
        tileToBitmap[2] = "zigzaggrass_2roundleft"
        tileToBitmap[3] = "zigzaggrass_squar"
        tileToBitmap[4] = "zigzaggrass_2roundright"
        tileToBitmap[5] = "zigzagyellow_mud_round"
        tileToBitmap[6] = "zigzagyellow_mud_downroundright"
        tileToBitmap[7] = "zigzagyellow_mud_downroundleft"
        tileToBitmap[8] = ENEMY
        tileToBitmap[9] = "lifehearth_full"
        tileToBitmap[10] = COIN

        tiles = arrayOf(
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 5, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 10, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 2, 3, 4, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 3, 0, 8, 0, 3),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0),

        )
    }
}