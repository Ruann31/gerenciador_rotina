package com.example.gerenciadorderotina

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var db: BancoHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = BancoHelper(this)

        val nome = findViewById<EditText>(R.id.nomeLogin)
        val senha = findViewById<EditText>(R.id.senhaLogin)
        val login = findViewById<Button>(R.id.login)
        val criarConta = findViewById<Button>(R.id.criarConta)

        login.setOnClickListener {
            val nomeTxt = nome.text.toString()
            val senhaTxt = senha.text.toString()

            val usuarioId = db.validarLogin(nomeTxt, senhaTxt)
            if (usuarioId != null) {
                val intent = Intent(this, TasksMananger::class.java)
                intent.putExtra("usuario_id", usuarioId)
                startActivity(intent)
                finish()
            }
        }

        criarConta.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }
    }
}
