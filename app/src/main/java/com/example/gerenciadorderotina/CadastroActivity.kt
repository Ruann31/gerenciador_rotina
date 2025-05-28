    package com.example.gerenciadorderotina

    import android.content.Intent
    import android.os.Bundle
    import android.widget.*
    import androidx.appcompat.app.AppCompatActivity

    class CadastroActivity : AppCompatActivity() {
        lateinit var db: BancoHelper

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_cadastro)

            db = BancoHelper(this)

            val nome = findViewById<EditText>(R.id.nomeCadastro)
            val senha = findViewById<EditText>(R.id.senhaCadastro)
            val btnCadastrar = findViewById<Button>(R.id.botaoCadastrar)

            btnCadastrar.setOnClickListener {
                val nomeTxt = nome.text.toString()
                val senhaTxt = senha.text.toString()

                if (nomeTxt.isNotBlank() && senhaTxt.isNotBlank()) {
                    db.criarUsuario(nomeTxt, senhaTxt)
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
