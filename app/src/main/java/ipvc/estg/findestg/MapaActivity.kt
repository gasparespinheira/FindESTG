package ipvc.estg.findestg

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.ImageView
class MapaActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_mapa)

            imageView = findViewById(R.id.mapImageView)

            // Ler a localização do SharedPreferences
            val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this)
            val localizacao: String? = sharedPreferences.getString("localizacao", "")
            val descricao_localizacao: String? =
                sharedPreferences.getString("descricao_localizacao", "")

            Log.d("MapaActivity", "localizacao: $localizacao")

            val imagemResource = when (descricao_localizacao) {
                "Bar" -> when (localizacao) {
                    "Entrada Principal da ESTG" -> R.drawable.principal_bar
                    "Entrada traseira ESTG" -> R.drawable.secundaria_bar
                    "Entrada Lateral ESTG" -> R.drawable.lateral_bar
                    else -> R.drawable.default_image
                }
                "Cantina" -> when (localizacao) {
                    "Entrada Principal da ESTG" -> R.drawable.principal_cantina
                    "Entrada traseira ESTG" -> R.drawable.secundaria_cantina
                    "Entrada Lateral ESTG" -> R.drawable.lateral_cantina
                    else -> R.drawable.default_image
                }
                "Serviços Académicos" -> when (localizacao) {
                    "Entrada Principal da ESTG" -> R.drawable.principal_aca
                    "Entrada traseira ESTG" -> R.drawable.secundaria_aca
                    "Entrada Lateral ESTG" -> R.drawable.lateral_aca
                    else -> R.drawable.default_image
                }
                else -> R.drawable.default_image
            }

            imageView.setImageResource(imagemResource)
        }
}

