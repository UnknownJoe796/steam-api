package com.ivieleague.steamapi

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Test

class SteamAPISessionTest {
    @Test
    fun testInstalled(){

        SteamAPISession().use {
            it.login("UnknownJoe796", "song bean ever ants")
            println(it.installedAndLicensed())

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
