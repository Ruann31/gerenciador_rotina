package com.example.gerenciadorderotina

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class DatabaseCopyHelper(private val context: Context) {
    private val dbName = "rotina.db"

    fun createDatabaseIfNotExists() {
        val dbPath = context.getDatabasePath(dbName)

        if (!dbPath.exists()) {
            dbPath.parentFile?.mkdirs()
            val input: InputStream = context.assets.open(dbName)
            val output: OutputStream = FileOutputStream(dbPath)

            val buffer = ByteArray(1024)
            var length: Int

            while (input.read(buffer).also { length = it } > 0) {
                output.write(buffer, 0, length)
            }

            output.flush()
            output.close()
            input.close()
        }
    }
}
