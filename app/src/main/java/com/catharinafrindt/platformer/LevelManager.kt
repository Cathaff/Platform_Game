package com.catharinafrindt.platformer

class LevelManager(data: LevelData) {
    val entities = ArrayList<Entity>()
    private val entitiesToAdd = ArrayList<Entity>()
    private val entitiesToRemove = ArrayList<Entity>()

    init {
        loadAssets(data)
    }

    fun update(dt: Float) {
        entities.forEach { it.update(dt) }
        //collision check
        addAndRemoveEntities()
    }

    private fun addAndRemoveEntities() {
        entities.removeAll(entitiesToRemove)
        entitiesToRemove.clear()

        entities.addAll(entitiesToAdd)
        entitiesToAdd.clear()
    }

    fun addEntity(e: Entity) {
        entitiesToAdd.add(e)
    }

    fun removeEntity(e: Entity) {
        entitiesToRemove.add(e)
    }

    private fun loadAssets(data: LevelData) {
        for (y in 0 until data.height()) {
            val row = data.getRow(y)
            for (x in 0 until row.size) {
                val tileId = row[x]
                if (tileId != NO_TILE) {
                    val spriteName = data.getSpriteName(tileId)
                    createEntity(spriteName, x.toFloat(), y.toFloat())
                }
            }
        }
        addAndRemoveEntities()
    }

    private fun createEntity(spriteName: String, x: Float, y: Float) {
        val entity: Entity = StaticEntity(spriteName, x, y)
/*when (spriteName) {
            PLAYER -> // player type
            else -> // some static entity
        }*/

        addEntity(entity)

    }

}
