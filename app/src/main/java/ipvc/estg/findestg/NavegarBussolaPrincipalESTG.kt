package ipvc.estg.findestg

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView


class NavegarBussolaPrincipalESTG : AppCompatActivity() {

    private lateinit var bussola : Sensor
    private lateinit var acelerometro : Sensor
    private lateinit var sensorManager : SensorManager
    private lateinit var listener : SensorEventListener
    private lateinit var Proximidade: Sensor
    private lateinit var powerManager: PowerManager
    private lateinit var wakeLock: PowerManager.WakeLock




    private var ultimoGrau = 0f
    private var vlrsBussola = FloatArray(3)
    private var vlrsGravidade = FloatArray(3)
    private var angulosDeOrientacao = FloatArray(3)
    private var matrixDeRotacao = FloatArray(9)


    private lateinit var button_cantina: Button
    private lateinit var button_bar: Button
    private lateinit var button_academicos: Button
    private lateinit var button_todos: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegar_bussola_estg)

        var imageView: ImageView = findViewById(R.id.imageView)

        // -- CANTINA --

        button_cantina = findViewById(R.id.button_cantina)
        button_cantina.setOnClickListener {
            imageView.setImageResource(R.drawable.bussola_cantina_principal)
        }


        // -- BAR
        button_bar = findViewById(R.id.button_bar)
        button_bar.setOnClickListener {
            imageView.setImageResource(R.drawable.bussola_bar_principal)
        }


        // -- ACADEMICOS

        button_academicos = findViewById(R.id.button_sa)
        button_academicos.setOnClickListener {
            imageView.setImageResource(R.drawable.bussola_academicos_principal)
        }


        // -- TODOS

        button_todos = findViewById(R.id.button_all)
        button_todos.setOnClickListener {
            imageView.setImageResource(R.drawable.bussola_todos_principal)
        }





        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Proximidade = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        val sensores : List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensores.forEach{ sensor ->
            Log.i("SENSORES", sensor.toString())
        }

        acelerometro = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        bussola = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?){
                when(event?.sensor?.type){
                    Sensor.TYPE_ACCELEROMETER -> {
                        vlrsGravidade = event.values.clone()
                        var x = event.values[0]
                        var y = event.values[1]
                        var z = event.values[2]

                    }
                    Sensor.TYPE_MAGNETIC_FIELD -> {
                        vlrsBussola = event.values.clone()
                    }
                    Sensor.TYPE_PROXIMITY -> {
                        val valorProximidade = event.values[0]
                        val textViewProximidade: TextView = findViewById(R.id.textView2)
                        textViewProximidade.text = "Proximidade: $valorProximidade"

                    }
                }

                SensorManager.getRotationMatrix(matrixDeRotacao, null, vlrsGravidade, vlrsBussola)
                SensorManager.getOrientation(matrixDeRotacao, angulosDeOrientacao)

                val textView: TextView = findViewById(R.id.textView) //
                val textView2: TextView = findViewById(R.id.textView2) //

                val radiano: Float = angulosDeOrientacao[0]
                val grauAtual = (Math.toDegrees(radiano.toDouble()) + 360).toFloat() % 360
                val anguloAtual = (Math.toDegrees(angulosDeOrientacao[0].toDouble()) + 360).toFloat() % 360

                val pontoCardeal: String = when {
                    anguloAtual >= 337.5 || anguloAtual < 22.5 -> "Norte"
                    anguloAtual >= 22.5 && anguloAtual < 67.5 -> "Nordeste"
                    anguloAtual >= 67.5 && anguloAtual < 112.5 -> "Leste"
                    anguloAtual >= 112.5 && anguloAtual < 157.5 -> "Sudeste"
                    anguloAtual >= 157.5 && anguloAtual < 202.5 -> "Sul"
                    anguloAtual >= 202.5 && anguloAtual < 247.5 -> "Sudoeste"
                    anguloAtual >= 247.5 && anguloAtual < 292.5 -> "Oeste"
                    else -> "Noroeste"
                }

                textView2.text = String.format("%.1f°", grauAtual)
                textView.text = pontoCardeal

                var rotacionar = RotateAnimation(
                    ultimoGrau, -grauAtual,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
                )

                rotacionar.duration = 250
                rotacionar.fillAfter = true

                imageView.startAnimation(rotacionar)
                ultimoGrau = -grauAtual

            }


            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

            }
        }

        powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "MyApp::WakeLock")

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(listener, acelerometro, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, bussola, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(listener, Proximidade, SensorManager.SENSOR_DELAY_NORMAL)
        if (Proximidade != null) {
           // wakeLock.acquire()
        }
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(listener)

        if (wakeLock.isHeld) {
            wakeLock.release()
        }
    }

}