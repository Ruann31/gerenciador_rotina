package com.example.gerenciadorderotina

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.EditText
import androidx.appcompat.widget.AppCompatCheckBox


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

    @SuppressLint("RestrictedApi")
    private fun adicionarTarefa(descricao: String) {
        val tarefa = Tarefa(descricao)
        listaTarefas.add(tarefa)

        val checkbox = AppCompatCheckBox(this).apply {
            text = tarefa.descricao
            setTextColor(Color.BLACK) // Cor do texto
            supportButtonTintList = ColorStateList.valueOf(Color.BLUE)

            setOnCheckedChangeListener { buttonView, isChecked ->
                tarefa.feito = isChecked

                if (isChecked) {
                    AlertDialog.Builder(this@TasksMananger)
                        .setTitle("Concluir tarefa")
                        .setMessage("Deseja remover a tarefa concluída?")
                        .setPositiveButton("Sim") { _, _ ->
                            val sucesso = db.removerCompromisso(usuarioId, tarefa.descricao)
                            if (sucesso) {
                                layoutLista.removeView(buttonView)
                                Toast.makeText(this@TasksMananger, "Tarefa removida com sucesso!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@TasksMananger, "Erro ao remover tarefa", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("Não") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        layoutLista.addView(checkbox)
    }



}
