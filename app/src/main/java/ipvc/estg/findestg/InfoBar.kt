package ipvc.estg.findestg


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InfoBar : AppCompatActivity() {

    private lateinit var textViewNome: TextView
    private lateinit var textViewHorario: TextView
    private lateinit var textViewCadeiras: TextView
    private lateinit var textViewComida: TextView
    private lateinit var textViewBebida: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_bar)

        textViewNome = findViewById(R.id.textViewNome)
        textViewHorario = findViewById(R.id.textViewHorario)
        textViewCadeiras = findViewById(R.id.textViewCadeiras)
        textViewComida = findViewById(R.id.textViewComida)
        textViewBebida = findViewById(R.id.textViewBebida)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Encerrar a atividade atual e voltar para a tela anterior
        }


        try{

            //criar base de dados
            val baseDados = openOrCreateDatabase("DB_BAR", MODE_PRIVATE, null)

            //criar uma tabela
            baseDados.execSQL("CREATE TABLE IF NOT EXISTS bar(nome VARCHAR(255), horario VARCHAR(255), cadeiras VARCHAR(255), comida VARCHAR(255), bebida VARCHAR(255))")

            //Inserir dados numa tabela
            baseDados.execSQL("INSERT INTO bar(nome, horario, cadeiras, comida, bebida) VALUES ('Bar', '8:00 - 19:00', '50', 'Pão, Croissant, Chocolates.','Água, Café, Sumo')")

            val consulta = "SELECT nome,horario,cadeiras,comida,bebida FROM bar"
            val cursor = baseDados.rawQuery(consulta,null)

            //Recuperar os indices da tabela
            val indiceNome = cursor.getColumnIndex("nome")
            val indiceHorario = cursor.getColumnIndex("horario")
            val indiceCadeiras = cursor.getColumnIndex("cadeiras")
            val indiceComida = cursor.getColumnIndex("comida")
            val indiceBebida = cursor.getColumnIndex("bebida")

            if (cursor.moveToFirst()) {
                val nome = cursor.getString(indiceNome)
                val horario = cursor.getString(indiceHorario)
                val cadeiras = cursor.getString(indiceCadeiras)
                val comida = cursor.getString(indiceComida)
                val bebida = cursor.getString(indiceBebida)

                // Atualizar as TextViews com os valores recuperados
                textViewNome.text = "$nome"
                textViewHorario.text = "Horário de funcionamento: $horario"
                textViewCadeiras.text = "Número de cadeiras: $cadeiras"
                textViewComida.text = "Comida: $comida"
                textViewBebida.text = "Bebidas: $bebida"
            }

            cursor.close()

        }catch(e: Exception){
            e.printStackTrace()
        }
    }
}