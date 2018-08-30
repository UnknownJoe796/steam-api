package com.ivieleague.steamapi

data class RunningApp(
        val appId: Long,
        val pids: List<Long>
){
    companion object {
        fun fromText(string: String):RunningApp?{
            if(!string.contains("AppID")) return null
            if(!string.contains("GameID")) return null
            return RunningApp(
                    appId = string.substringAfter("AppID").substringBefore(',').filter { it.isDigit() }.toLongOrNull() ?: return null,
                    pids = string.substringAfter("associated PIDs (").substringBefore(')').split(",").mapNotNull {
                        it.trim().takeUnless { it.isBlank() }?.filter { it.isDigit() }?.toLongOrNull()
                    }
            )
        }
    }
}