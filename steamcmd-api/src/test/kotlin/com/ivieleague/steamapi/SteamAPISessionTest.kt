package com.ivieleague.steamapi

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Test

class SteamAPISessionTest {
    @Test
    fun testInstalled(){

        SteamAPISession().use {
            it.login("UnknownJoe796", "song bean ever ants")
            repeat(100){ _ ->
                println(it.command("licenses_print").lines().filter { it.contains("site", true) })
                Thread.sleep(1000L)
            }

//        println(it.updateAllInfo())

//        val games = it.appsLicensed()
//        println(games.joinToString("\n"))
//        println("${games.size} games total")

//        val hollowKnight = 367520L
//        it.run(hollowKnight)
//        Thread.sleep(5_000L)
//        println(it.currentlyRunning())
//        Thread.sleep(5_000L)
//        it.stop(hollowKnight)
//        println(it.currentlyRunning())
//
//        println(it.info(hollowKnight))
        }
    }

}
