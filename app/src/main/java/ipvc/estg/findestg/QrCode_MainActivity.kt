package ipvc.estg.findestg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.Nullable
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.Toast

class QrCode_MainActivity : AppCompatActivity() {

    private lateinit var scanBtn: Button
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_activity_main)

        scanBtn = findViewById(R.id.scanner)
        textView = findViewById(R.id.text)

        scanBtn.setOnClickListener(View.OnClickListener {
            val intentIntegrator = IntentIntegrator(this@QrCode_MainActivity)
            intentIntegrator.setPrompt("Scan a QR Code")
            intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            intentIntegrator.setBarcodeImageEnabled(false)
            intentIntegrator.initiateScan()

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        val intentResult: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult != null) {
            val contents: String? = intentResult.contents
            if (contents != null) {
                textView.text = intentResult.contents
                Toast.makeText(this@QrCode_MainActivity, "QR Code: $contents", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    fun navegar_bussola(view: View) {

        val intent = Intent(this, NavegarBussolaLateralESTG::class.java)
        startActivity(intent)

    }

    fun confirmarLocalizacao(view: View) {
        // Obtenha a localização atual da textView
        val localizacao = textView.text.toString()

        // Salve a localização no SharedPreferences
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("localizacao", localizacao)
        editor.apply()

        // Navegue para a próxima atividade (MapaActivity)
        val intent = Intent(this, MapaActivity::class.java)
        startActivity(intent)
    }

    fun cancelarLocalizacao(view: View) {
        // Reinicie a atividade atual (QrCode_MainActivity) para ler um novo QRCode
        val intent = intent
        startActivity(intent)
    }


}
