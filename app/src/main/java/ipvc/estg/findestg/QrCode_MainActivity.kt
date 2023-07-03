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
import android.widget.Toast

class QrCode_MainActivity : AppCompatActivity() {

    private lateinit var scanBtn: Button
    private lateinit var textView: TextView
    private var qrCodeContents: String? = null

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
            qrCodeContents = intentResult.contents
            if (qrCodeContents != null) {
                textView.setText(qrCodeContents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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
}
