package com.ivieleague.steamapi

import com.google.common.collect.Maps
import com.pty4j.PtyProcess
import com.pty4j.WinSize
import java.io.PushbackReader
import java.io.StringReader
import java.util.*


fun main(vararg args: String) {

    SteamAPISession().use {
//        println(it.find("sitelicense"))
        it.login("UnknownJoe796", "song bean ever ants")

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