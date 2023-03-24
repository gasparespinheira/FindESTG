package ipvc.estg.findestg

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.vishnusivadas.advanced_httpurlconnection.PutData


class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        //Start ProgressBar first (Set visibility VISIBLE)
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            //Starting Write and Read data with URL
            //Creating array for parameters
            val field = arrayOfNulls<String>(4)
            field[0] = "fullname"
            field[1] = "username"
            field[2] = "password"
            field[3] = "email"
            //Creating array for data
            val data = arrayOfNulls<String>(2)
            data[0] = "data-1"
            data[1] = "data-2"
            val putData = PutData(
                "https://projects.vishnusivadas.com/AdvancedHttpURLConnection/putDataTest.php",
                "POST",
                field,
                data
            )
            if (putData.startPut()) {
                if (putData.onComplete()) {
                    val result = putData.result
                    //End ProgressBar (Set visibility to GONE)
                    //Log.i("PutData", result)
                }
            }
            //End Write and Read data with URL
        }


    }


}