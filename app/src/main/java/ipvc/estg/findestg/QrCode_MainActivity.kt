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
    private var qrCodeContents: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_activity_main)

        MenuHelper.setupMenu(this)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Encerrar a atividade atual e voltar para a tela anterior
        }

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
            qrCodeContents = intentResult.contents
            if (qrCodeContents != null) {
                textView.setText(qrCodeContents)

                setButtonVisibility()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun setButtonVisibility() {
        val button_confirm: Button = findViewById(R.id.button_confirm)
        val button_cancel: Button = findViewById(R.id.button_cancel)
        val button_navegar_com_bussola: Button = findViewById(R.id.button_navegar_com_bussola)

        if (qrCodeContents != null) {
            button_confirm.visibility = View.VISIBLE
            button_cancel.visibility = View.VISIBLE
            button_navegar_com_bussola.visibility = View.VISIBLE
        } else {
            button_confirm.visibility = View.GONE
            button_cancel.visibility = View.GONE
            button_navegar_com_bussola.visibility = View.GONE
        }
    }

    fun navegar_bussola(view: View) {

        Toast.makeText(this, "Ponto de Partida: $qrCodeContents", Toast.LENGTH_SHORT).show()

        when (qrCodeContents) {
            "Entrada Lateral ESTG" -> {
                val intent = Intent(this, NavegarBussolaLateralESTG::class.java)
                startActivity(intent)
            }
            "Entrada Principal da ESTG" -> {
                val intent = Intent(this, NavegarBussolaLateralESTG::class.java)
                startActivity(intent)
            }
            "Entrada traseira ESTG" -> {
                val intent = Intent(this, NavegarBussolaLateralESTG::class.java)
                startActivity(intent)
            }
        }
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
