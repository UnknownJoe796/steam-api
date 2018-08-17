package com.ivieleague.steamapi

import java.io.IOException
import java.io.InputStream
import java.io.PrintStream

internal class StreamGobbler(private val `is`: InputStream, private val os: PrintStream) : Runnable {

    override fun run() {
        try {
            var c: Int
            while (true) {
                c = `is`.read()
                if (c != -1) {
                    os.print(c.toChar())
                } else {
                    break
                }
            }
        } catch (x: IOException) {
            x.printStackTrace()
        }

    }
}