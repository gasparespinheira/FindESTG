package ipvc.estg.findestg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InfoCantina : AppCompatActivity() {

    private lateinit var textViewNome: TextView
    private lateinit var textViewHorario: TextView
    private lateinit var textViewCadeiras: TextView
    private lateinit var textViewPratos: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_cantina)

        MenuHelper.setupMenu(this)

        textViewNome = findViewById(R.id.textViewNome)
        textViewHorario = findViewById(R.id.textViewHorario)
        textViewCadeiras = findViewById(R.id.textViewCadeiras)
        textViewPratos = findViewById(R.id.textViewComida)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Encerrar a atividade atual e voltar para a tela anterior
        }


        try{

            //criar base de dados
            val baseDados = openOrCreateDatabase("DB_CANTINA", MODE_PRIVATE, null)

            //criar uma tabela
            baseDados.execSQL("CREATE TABLE IF NOT EXISTS cantina(nome VARCHAR(255), horario VARCHAR(255), cadeiras VARCHAR(255), pratos VARCHAR(255))")

            //Inserir dados numa tabela
            baseDados.execSQL("INSERT INTO cantina(nome, horario, cadeiras, comida) VALUES ('Cantina', '12:00 - 14:30', '300', 'Prato do Dia, Sugestão do Dia, Grill')")

            val consulta = "SELECT nome,horario,cadeiras,prato FROM cantina"
            val cursor = baseDados.rawQuery(consulta,null)

            //Recuperar os indices da tabela
            val indiceNome = cursor.getColumnIndex("nome")
            val indiceHorario = cursor.getColumnIndex("horario")
            val indiceCadeiras = cursor.getColumnIndex("cadeiras")
            val indicePrato = cursor.getColumnIndex("prato")

            if (cursor.moveToFirst()) {
                val nome = cursor.getString(indiceNome)
                val horario = cursor.getString(indiceHorario)
                val cadeiras = cursor.getString(indiceCadeiras)
                val prato = cursor.getString(indicePrato)


                // Atualizar as TextViews com os valores recuperados
                textViewNome.text = "$nome"
                textViewHorario.text = "Horário de funcionamento: $horario"
                textViewCadeiras.text = "Número de cadeiras: $cadeiras"
                textViewPratos.text = "Pratos disponíveis: $prato"
            }

            cursor.close()

        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}