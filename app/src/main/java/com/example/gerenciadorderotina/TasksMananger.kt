package com.example.gerenciadorderotina

import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText

class TasksMananger : AppCompatActivity() {

    private val listaTarefas = mutableListOf<Tarefa>()
    private lateinit var layoutLista: LinearLayout
    private lateinit var db: BancoHelper
    private var usuarioId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manger_task)

        usuarioId = intent.getIntExtra("usuario_id", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "Erro: usuário não identificado", Toast.LENGTH_SHORT).show()
            finish()
        }

        db = BancoHelper(this)

        layoutLista = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val scroll = findViewById<ScrollView>(R.id.scrollView)
        scroll.addView(layoutLista)

        val fab = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fab.setOnClickListener {
            mostrarDialogoAdicionarTarefa()
        }

        val compromissos = db.listarCompromissos(usuarioId)
        for (compromisso in compromissos) {
            adicionarTarefa(compromisso.descricao)
        }
    }

    private fun mostrarDialogoAdicionarTarefa() {
        val input = EditText(this)
        input.hint = "Descreva sua tarefa"

        AlertDialog.Builder(this)
            .setTitle("Nova tarefa")
            .setView(input)
            .setPositiveButton("Adicionar") { _, _ ->
                val descricao = input.text.toString()
                if (descricao.isNotBlank()) {
                    adicionarTarefa(descricao)
                    db.salvarCompromisso(usuarioId, descricao)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun adicionarTarefa(descricao: String) {
        val tarefa = Tarefa(descricao)
        listaTarefas.add(tarefa)

        val checkbox = CheckBox(this).apply {
            text = tarefa.descricao
            setOnCheckedChangeListener { _, isChecked ->
                tarefa.feito = isChecked
            }
        }

        layoutLista.addView(checkbox)
    }
}
