@file:Suppress("NOTHING_TO_INLINE")

package com.ivieleague.generic

import java.io.PushbackReader
import java.io.Reader

/**
 * Created by josep on 4/27/2016.
 */

fun Int.isReaderEnd() = this == -1 || this == 0xFFFF

fun Reader.readString(length: Int): String {
    val builder = StringBuilder()
    kotlin.repeat(length) {
        builder.append(readChar())
    }
    return builder.toString()
}

fun Reader.readChar(): Char {
    return read().toChar()
}

fun PushbackReader.peekChar(): Char {
    val intChar = read()
    unread(intChar)
    return intChar.toChar()
}

fun PushbackReader.isAtEnd(): Boolean {
    val intChar = read()
    unread(intChar)
    return intChar.isReaderEnd()
}

fun PushbackReader.peekString(size: Int): String {
    val array = CharArray(size)
    read(array)
    unread(array)
    return String(array)
}

fun PushbackReader.skipWhile(predicate: (Char) -> Boolean) = skipUntil { !predicate(it) }
fun PushbackReader.skipUntil(predicate: (Char) -> Boolean) {
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd() || predicate(intChar.toChar())) {
            unread(intChar)
            return
        }
    }
}

fun PushbackReader.readWhile(predicate: (Char) -> Boolean) = readUntil { !predicate(it) }
fun PushbackReader.readUntil(predicate: (Char) -> Boolean): String {
    val builder: StringBuilder = StringBuilder()
    builder.setLength(0)
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd() || predicate(intChar.toChar())) {
            unread(intChar)
            return builder.toString()
        } else {
            builder.append(intChar.toChar())
        }
    }
}

fun PushbackReader.peekWhile(predicate: (Char) -> Boolean) = peekUntil { !predicate(it) }
fun PushbackReader.peekUntil(predicate: (Char) -> Boolean): String {
    val builder: StringBuilder = StringBuilder()
    builder.setLength(0)
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd() || predicate(intChar.toChar())) {
            unread(intChar)
            unread(builder.toString().toCharArray())
            return builder.toString()
        } else {
            builder.append(intChar.toChar())
        }
    }
}

fun PushbackReader.readCheck(data: String): Boolean {
    val builder: StringBuilder = StringBuilder()
    builder.setLength(0)
    for (char in data) {
        val intChar = read()
        if (intChar.isReaderEnd() || intChar.toChar() != char) {
            unread(intChar)
            unread(builder.toString().toCharArray())
            return false
        } else {
            builder.append(intChar.toChar())
        }
    }
    return true
}

fun Reader.skipUntilSkipFinal(predicate: (Char) -> Boolean) {
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd() || predicate(intChar.toChar())) {
            return
        }
    }
}

fun Reader.readUntilSkipFinal(predicate: (Char) -> Boolean): String {
    val builder: StringBuilder = StringBuilder()
    builder.setLength(0)
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd() || predicate(intChar.toChar())) {
            return builder.toString()
        } else {
            builder.append(intChar.toChar())
        }
    }
}

fun PushbackReader.skipWhitespace() = skipUntil { !it.isWhitespace() && it >= ' ' }
fun PushbackReader.skipWhitespaceAndComments() {
    var inLineComment = false
    var inBlockComment = false
    while (true) {
        val intChar = read()
        if (intChar.isReaderEnd()) {
            unread(intChar)
            return
        }
        val char = intChar.toChar()
        if (char == '/' && peekChar() == '/') {
            inLineComment = true
            skip(1)
            continue
        }
        if (inLineComment && char == '\n') {
            inLineComment = false
            continue
        }
        if (char == '/' && peekChar() == '*') {
            inBlockComment = true
            skip(1)
            continue
        }
        if (inBlockComment && char == '*' && peekChar() == '/') {
            inBlockComment = false
            skip(1)
            continue
        }
        if (!inBlockComment && !inLineComment && !char.isWhitespace()) {
            unread(intChar)
            return
        }
    }
}

fun PushbackReader.skipUntil(char: Char) = skipUntil { it == char }
fun PushbackReader.readUntil(char: Char) = readUntil { it == char }
fun PushbackReader.readUntilEscapable(escapeChar: Char, char: Char): String {
    var prevChar = ' '
    return readUntil {
        val result = it == char && prevChar != escapeChar
        prevChar = it
        result
    }
}

fun PushbackReader.readUntilEscapable(escapeChars: Collection<Char>, char: Char): String {
    var prevChar = ' '
    return readUntil {
        val result = it == char && prevChar !in escapeChars
        prevChar = it
        result
    }
}

fun PushbackReader.readWord() = readUntil { it !in 'a'..'z' || it !in 'A'..'Z' }

fun PushbackReader.readEscaped(escapeChar: Char, endChar: Char): String {
    val builder: StringBuilder = StringBuilder()
    builder.setLength(0)
    var prevChar = ' '
    while (true) {
        val intChar = read()
        val char = intChar.toChar()
        if (intChar.isReaderEnd()) {
            unread(intChar)
            return builder.toString()
        } else if (char == endChar) {
            if (prevChar == escapeChar) {
                builder.append(endChar)
            } else {
                unread(intChar)
                return builder.toString()
            }
        } else {
            builder.append(char)
        }
    }
}

/**
 * Reads a whole delimited section, accounting for subsections.  Not recommended, but it can do it.  The first character it reads MUST be the start char.
 */
fun Reader.readDelimited(startChar: Char, endChar: Char): String {
    var count = 0
    return readUntilSkipFinal {
        if (it == startChar) count++
        else if (it == endChar) {
            count--
            if (count == 0) {
                return@readUntilSkipFinal true
            }
        }
        false
    }
}