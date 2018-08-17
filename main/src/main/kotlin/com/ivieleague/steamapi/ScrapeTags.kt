package com.ivieleague.steamapi

object ScrapeTags{
    fun scrapeTagUrl(tags:List<Long>) = "https://store.steampowered.com/search/?tags=${tags.joinToString(",")}"
    val regex = Regex("<div class=\"tab_filter_control \" data-param=\"tags\" data-value=\"([0-9]+)\" data-loc=\"([a-zA-Z0-9 '\\-]+)\">")
    val allTags = HashMap<Long, String>()
    fun parsePage(page:String):Map<Long, String>{
        val map = regex.findAll(page).associate { it.groups[1]!!.value.toLong() to it.groups[2]!!.value }
        allTags += map
        return allTags
    }
}