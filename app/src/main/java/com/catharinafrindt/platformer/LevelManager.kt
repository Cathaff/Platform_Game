package com.catharinafrindt.platformer

import android.content.Context
import android.graphics.PointF

val PLAYER_STARTING_HEALTH = 3

class LevelManager(data: LevelData, context: Context) {
    val entities = ArrayList<Entity>()
    var fixedTotalCoins = 0
    var levelHeight: Float = 0.0f
    var collectedCoins = 0
    var totalCoins = 0
    var playerHealth : Int = PLAYER_STARTING_HEALTH
    lateinit var player: Player
    val fixedCoinsToAddWhenRestart = ArrayList<PointF>()
    private val entitiesToAdd = ArrayList<Entity>()
    private val entitiesToRemove = ArrayList<Entity>()
    private var jukeBox = Jukebox(context.assets)
    private val INVULNERABILITY_DURATION = 3.0f
    private var isPlayerInvulnerable  = false
    private var invulnerabilityTimer  = 0.0f

    init {
        loadAssets(data)
    }

    fun update(dt: Float) {
        entities.forEach { it.update(dt) }
        if (isPlayerInvulnerable)
        {
            invulnerabilityTimer -= dt
        }
        if (invulnerabilityTimer <= 0)
        {
            isPlayerInvulnerable = false
        }
        checkCollisions()
        addAndRemoveEntities()
    }

    private fun checkCollisions() {
        for (e in entities) {
            if (e is Player) {
                continue
            }
            else if (e is Enemy) {
                if(isColliding(e, player)) {
                    handleCollision(player, e)
                }
            }
            else if (e is Coin) {
                if(isColliding(e, player)) {
                    handleCollectibleCollision(player, e)
                }
            }
            else if (e is Flag) {
                    continue
            }
            if (isColliding(e, player)) {
                e.onCollision(player)
                player.onCollision(e)
            }
        }
    }

    private fun handleCollision(player: Player, enemy: Enemy) {
        if (!isPlayerInvulnerable) {
            playerHealth--
            if (playerHealth < 0) { playerHealth = 0 }
                isPlayerInvulnerable = true
                invulnerabilityTimer = INVULNERABILITY_DURATION
        }
    }

    private fun handleCollectibleCollision(player: Player, coin: Coin) {
        removeEntity(coin)
        collectedCoins += 1
        totalCoins -= 1
        jukeBox.play(SFX.coins, 0)
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

    private fun removeEntity(e: Entity) {
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
            val enemy = Enemy(spriteName, x, y)
            addEntity(enemy)
        }
        else if (spriteName == COIN) {
            val coin = Coin(spriteName, x, y)
            totalCoins += 1
            fixedTotalCoins += 1
            addEntity(coin)
            fixedCoinsToAddWhenRestart.add(PointF(x,y))
        }
        else if (spriteName == "flag") {
            val flag = Flag(spriteName, x, y)
            addEntity(flag)
        }
        else {
            addEntity(StaticEntity(spriteName, x, y))
        }

    }

}
