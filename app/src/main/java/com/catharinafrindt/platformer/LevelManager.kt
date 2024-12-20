package com.catharinafrindt.platformer

val PLAYER_STARTING_HEALTH = 3

class LevelManager(data: LevelData) {
    val entities = ArrayList<Entity>()
    var levelHeight: Float = 0.0f
    var  playerHealth : Int = PLAYER_STARTING_HEALTH
    lateinit var player: Player
    lateinit var enemy: Enemy
    private val entitiesToAdd = ArrayList<Entity>()
    private val entitiesToRemove = ArrayList<Entity>()

    init {
        loadAssets(data)
    }

    fun update(dt: Float) {
        entities.forEach { it.update(dt) }
        checkCollisions()
        addAndRemoveEntities()
    }

    private fun checkCollisions() {
        for (e in entities) {
            if (e == player) {
                continue
            }
            else if (e == enemy) {
                if(isColliding(e, player)) {
                    handleCollision(player, enemy)
                }
            }
            if (isColliding(e, player)) {
                e.onCollision(player)
                player.onCollision(e)
            }
        }
    }

    private fun handleCollision(player: Player, enemy: Enemy) {
            playerHealth--
            if (playerHealth < 0) { playerHealth = 0 }
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
        levelHeight = data.height().toFloat()
        addAndRemoveEntities()
    }

    private fun createEntity(spriteName: String, x: Float, y: Float) {
        if (spriteName == PLAYER) {
            player = Player(spriteName, x, y)
            addEntity(player)
        }
        else if (spriteName == ENEMY) {
            enemy = Enemy(spriteName, x, y)
            addEntity(enemy)
        }
        else {
            addEntity(StaticEntity(spriteName, x, y))
        }

    }

}
