package com.ivieleague.steamapi

import com.ivieleague.generic.peekChar
import com.ivieleague.generic.readCheck
import com.ivieleague.generic.readEscaped
import com.ivieleague.generic.skipWhitespace
import java.io.PushbackReader


fun Map<String, Any?>.getPath(vararg segments: String): Any?{
    var current:Map<String, Any?> = this
    for(seg in segments.copyOfRange(0, segments.lastIndex)){
        current = current[seg] as?Map<String, Any?> ?: return null
    }
    return current[segments.last()]
}

fun PushbackReader.readSteamMap():Map<String, Any?>{
    val result = HashMap<String, Any?>()

    assert(readCheck("{"))
    skipWhitespace()
    while(peekChar() != '}'){
        //read key
        assert(readCheck("\""))
        val key = readEscaped('\\', '"')
        assert(readCheck("\""))

        skipWhitespace()

        val value: Any? = if(peekChar() == '{'){
            readSteamMap()
        } else {
            //read string value
            assert(readCheck("\""))
            val res = readEscaped('\\', '"')
            assert(readCheck("\""))
            res
        }

        skipWhitespace()

        //add entry
        result[key] = value
    }
    assert(readCheck("}"))
    return result
}