package com.example.gerenciadorderotina

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BancoHelper(context: Context) : SQLiteOpenHelper(context, "genrenciador_rotina.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT UNIQUE, senha TEXT)")
        db.execSQL("CREATE TABLE IF NOT EXISTS compromissos (id INTEGER PRIMARY KEY AUTOINCREMENT, descricao TEXT, feito INTEGER, usuario_id INTEGER, FOREIGN KEY(usuario_id) REFERENCES usuarios(id))")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun criarUsuario(nome: String, senha: String): Boolean {
        val db = writableDatabase
        return try {
            val stmt = db.compileStatement("INSERT INTO usuarios (nome, senha) VALUES (?, ?)")
            stmt.bindString(1, nome)
            stmt.bindString(2, senha)
            stmt.executeInsert() > 0
        } catch (e: Exception) {
            false
        }
    }

    fun validarLogin(nome: String, senha: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM usuarios WHERE nome = ? AND senha = ?", arrayOf(nome, senha))
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            cursor.close()
            id
        } else {
            cursor.close()
            null
        }
    }

    fun salvarCompromisso(usuarioId: Int, descricao: String): Boolean {
        val db = writableDatabase
        val stmt = db.compileStatement("INSERT INTO compromissos (descricao, feito, usuario_id) VALUES (?, 0, ?)")
        stmt.bindString(1, descricao)
        stmt.bindLong(2, usuarioId.toLong())
        return stmt.executeInsert() > 0
    }

    fun listarCompromissos(usuarioId: Int): List<Tarefa> {
        val lista = mutableListOf<Tarefa>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT descricao, feito FROM compromissos WHERE usuario_id = ?", arrayOf(usuarioId.toString()))
        while (cursor.moveToNext()) {
            val descricao = cursor.getString(0)
            val feito = cursor.getInt(1) == 1
            lista.add(Tarefa(descricao, feito))
        }
        cursor.close()
        return lista
    }

    fun removerCompromisso(usuarioId: Int, descricao: String): Boolean {
        val db = writableDatabase
        return try {
            val stmt = db.compileStatement("DELETE FROM compromissos WHERE usuario_id = ? AND descricao = ?")
            stmt.bindLong(1, usuarioId.toLong())
            stmt.bindString(2, descricao)
            stmt.executeUpdateDelete() > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
