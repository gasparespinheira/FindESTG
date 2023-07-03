package ipvc.estg.findestg

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InfoAcademicos : AppCompatActivity() {

    private lateinit var textViewNome: TextView
    private lateinit var textViewHorario: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_academicos)

        MenuHelper.setupMenu(this)

        textViewNome = findViewById(R.id.textViewNome)
        textViewHorario = findViewById(R.id.textViewHorario)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Encerrar a atividade atual e voltar para a tela anterior
        }

        try{

            //criar base de dados
            val baseDados = openOrCreateDatabase("DB_SERVICOS", MODE_PRIVATE, null)

            //criar uma tabela
            baseDados.execSQL("CREATE TABLE IF NOT EXISTS academicos(nome VARCHAR, horario VARCHAR)")

            //Inserir dados numa tabela
            baseDados.execSQL("INSERT INTO academicos(nome, horario) VALUES ('Serviços Académicos', '9:00 - 16:00')")

            val consulta = "SELECT nome,horario FROM academicos"
            val cursor = baseDados.rawQuery(consulta,null)

            //Recuperar os indices da tabela
            val indiceNome = cursor.getColumnIndex("nome")
            val indiceHorario = cursor.getColumnIndex("horario")

            if (cursor.moveToFirst()) {
                val nome = cursor.getString(indiceNome)
                val horario = cursor.getString(indiceHorario)

                // Atualizar as TextViews com os valores recuperados
                textViewNome.text = "$nome"
                textViewHorario.text = "Horário de funcionamento: $horario"
            }

            cursor.close()

        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}
