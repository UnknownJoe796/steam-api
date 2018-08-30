package com.ivieleague.steamapi

import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Test

class SteamAPISessionTest {
    @Test
    fun testRunning(){
        SteamAPISession("C:\\Exit\\vrdesktop\\steamcmd\\steamcmd.exe").use {
            it.login("UnknownJoe796", "song bean ever ants")
            it.run(367520)
            Thread.sleep(4000L)
            println(it.currentlyRunning())
            Thread.sleep(4000L)
            it.stop(367520)
        }
    }
    @Test
    fun testLicenseCheck(){

        SteamAPISession("C:\\Exit\\vrdesktop\\steamcmd\\steamcmd.exe").use {
            it.login("UnknownJoe796", "song bean ever ants")
            repeat(100){ _ ->
                println("Licenses:" + it.licenses().filter{ it.info.contains("site") }.joinToString())
                Thread.sleep(1000L)
            }
        }
    }
    @Test
    fun printLicenses(){

        SteamAPISession("C:\\Exit\\vrdesktop\\steamcmd\\steamcmd.exe").use {
            it.login("UnknownJoe796", "song bean ever ants")
            try{ Thread.sleep(30_000L) } catch(e:Exception){
                e.printStackTrace()
            }
            println(it.command("licenses_print"))
            println("Licenses:" + it.licenses().filter{ it.info.contains("Site") }.joinToString(", ", "[", "]"))
        }
    }

}
