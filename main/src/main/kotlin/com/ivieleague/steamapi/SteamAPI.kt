package com.ivieleague.steamapi

import com.google.common.collect.Maps
import com.pty4j.PtyProcess
import com.pty4j.WinSize
import java.io.Closeable
import java.io.PushbackReader
import java.io.Reader
import java.io.StringReader

class SteamAPISession(): Closeable {
    private val process = PtyProcess.exec(
            arrayOf("C:\\Program Files (x86)\\Steam\\steamcmd.exe"),
            Maps.newHashMap(System.getenv())
    ).apply {
        winSize = WinSize(1000, 50)
    }

    private var kill = false
    private val toProcess = process.outputStream.bufferedWriter()
    private val fromProcess = process.inputStream.bufferedReader(Charsets.US_ASCII)
    private val fromProcessError = Thread(StreamGobbler(process.errorStream, System.out)).start()

    companion object {
        val ANSI_START = "\u001B["
        val ANSI_PARAM = ""
        val ANSI_INT = " !\"#\$%&'()*+,-./"
        val ANSI_END = "@A–Z[\\]^_`a–z{|}~"
        val ANSI_REGEX = Regex("\\u001B\\[([0123456789:;<=>?])*([ !\"#\$%&'()*+,-./])*([qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM\\\\@\\[\\]^_`{|}~])")
    }

    init {
        val result = fromProcess.readUntilPrompt()
        println("Setup result: $result")
//        println("Skip: " + fromProcess.readUntilPrompt())
    }

    private fun Reader.readUntilPrompt() = readUntil("Steam>")

    private fun Reader.readUntil(string: String): String {
        val builder = StringBuilder()

        while (builder.substring((builder.length - string.length).coerceAtLeast(0)) != string && !kill) {
            val intResult = read()
            if (intResult == -1) continue
            val charResult = intResult.toChar()
            builder.append(charResult)
        }

        return builder.toString().removeSuffix(string).replace(ANSI_REGEX, "")
    }

    //General command

    fun command(input: String): String {
        toProcess.write(input)
        toProcess.newLine()
        toProcess.flush()
        println("Input: $input")
        println("Skipping input: " + fromProcess.readUntil(input))
        val result = fromProcess.readUntilPrompt()
        return result
    }

    //Specific commands

    fun login(username:String, password:String) = command("login \"$username\" \"$password\"")
    fun logout() = command("logout")

    fun licenses():List<License>{
        val raw = command("licenses_print").trim().split("License")
        return raw.mapNotNull { License.read(it.lines()) }.toList()
    }

    fun appsLicensed():List<Long> = licenses().flatMap { it.apps }

    fun find(query:String) = command("find $query")

    fun run(id:Long) = command("app_run $id")
    fun update(id:Long) = command("app_update $id")
    fun stop(id:Long) = command("app_stop $id")
    fun info(id:Long) = command("app_info_print $id").substringAfter('{').let {
        AppInfo.fromSteamMap(PushbackReader(StringReader(it), 100).readSteamMap())
    }

    fun updateAllInfo() = command("app_info_update 1")

    fun currentlyRunning() = RunningApp.fromText(command("apps_running"))

    override fun close() {
        kill = true
        toProcess.write("exit")
        toProcess.newLine()
        toProcess.flush()
        try {
            if (process.isAlive) {
                process.destroyForcibly()
            }
            fromProcess.close()
            toProcess.close()
        } catch(e:Exception){
            e.printStackTrace()
        }
    }
}