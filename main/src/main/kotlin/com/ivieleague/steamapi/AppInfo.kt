package com.ivieleague.steamapi

import com.ivieleague.generic.*
import java.io.PushbackReader

data class AppInfo(
        val name:String = "",
        val type: String = "",
        val osList: List<String> = listOf(),
        val languages: List<String> = listOf(),
        val metacriticName: String = "",
        val controllerSupport: String = "",
        val metacriticScore: String = "",
        val metacriticFullUrl:String = "",
        val gameId: Long = 0L,
        val tags:List<Long> = listOf(),
        val developer: String = "",
        val publisher: String = "",
        val homepage: String = ""
){
    val imageUrl: String get() = "https://steamcdn-a.akamaihd.net/steam/apps/$gameId/header.jpg"

    companion object {
        fun fromSteamMap(steamMap:Map<String, Any?>):AppInfo{
            return AppInfo(
                    name = steamMap.getPath("common", "name") as? String ?: "",
                    type = steamMap.getPath("common", "type") as? String ?: "",
                    osList = (steamMap.getPath("common", "oslist") as? String)?.split(',') ?: listOf(),
                    languages = (steamMap.getPath("common", "languages") as? Map<String, Any?>)
                            ?.keys?.mapNotNull { it as? String } ?: listOf(),
                    metacriticName = steamMap.getPath("common", "metacritic_name") as? String ?: "",
                    controllerSupport = steamMap.getPath("common", "controller_support") as? String ?: "",
                    metacriticScore = steamMap.getPath("common", "metacritic_score") as? String ?: "",
                    metacriticFullUrl = steamMap.getPath("common", "metacritic_fullurl") as? String ?: "",
                    gameId = (steamMap.getPath("common", "gameid") as? String)?.toLongOrNull() ?: 0,
                    tags = (steamMap.getPath("common", "store_tags") as? Map<String, Any?>)
                            ?.values?.mapNotNull { (it as? String)?.toLongOrNull() } ?: listOf(),
                    developer = steamMap.getPath("extended", "developer") as? String ?: "",
                    publisher = steamMap.getPath("extended", "publisher") as? String ?: "",
                    homepage = steamMap.getPath("extended", "homepage") as? String ?: ""
            )
        }
    }
}