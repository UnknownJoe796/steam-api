package com.ivieleague.steamapi

import com.ivieleague.generic.*
import java.io.PushbackReader

data class License(
        val id: Long,
        val state: State,
        val info: String,
        val apps: List<Long>,
        val depots: List<Long>
){
    enum class State{ Active, Inactive }

    companion object {
        val stateMap = State.values().associate { it.name.toLowerCase() to it }
        fun read(lines:List<String>):License?{
            val headerLine = lines.lastOrNull { it.contains("package") } ?: return null
            val stateLine = lines.lastOrNull { it.contains("State") } ?: return null
            val appsLine = lines.lastOrNull { it.contains("Apps") } ?: return null
            val depotsLine = lines.lastOrNull { it.contains("Depots") } ?: ""
            val id = headerLine.filter { it.isDigit() }.toLongOrNull() ?: return null
            val state = stateLine.substringAfter(':').substringBefore('(').trim()
            val appInfo = stateLine.substringAfter(") - ")
            val apps = appsLine.substringAfter(':').substringBeforeLast('(').split(", ")
                    .mapNotNull {
                        it.filter { it.isDigit() }
                                .takeIf { it.isNotBlank() }
                                ?.toLong()
                    }
            val depots = depotsLine.substringAfter(':').substringBeforeLast('(').split(", ")
                    .mapNotNull {
                        it.filter { it.isDigit() }
                                .takeIf { it.isNotBlank() }
                                ?.toLong()
                    }
            return License(
                    id = id,
                    state = stateMap[state.toLowerCase()] ?: return null,
                    info = appInfo,
                    apps = apps,
                    depots = depots
            )
        }
    }
}