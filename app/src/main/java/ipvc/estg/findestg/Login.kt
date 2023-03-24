package ipvc.estg.findestg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData

class Login : AppCompatActivity() {

    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewSignUp: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewSignUp = findViewById(R.id.signUpText)
        progressBar = findViewById(R.id.progress)





        buttonLogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val username = textInputEditTextUsername.text.toString()
                val password = textInputEditTextPassword.text.toString()



                if (username.isNotEmpty() && password.isNotEmpty()) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    progressBar.visibility = View.VISIBLE

                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        //Starting Write and Read data with URL
                        //Creating array for parameters
                        val field = arrayOfNulls<String>(2)
                        field[0] = "username"
                        field[1] = "password"
                        //Creating array for data
                        val data = arrayOfNulls<String>(2)
                        data[0] = username
                        data[1] = password
                        val putData = PutData(
                            "http://192.168.1.74/FindEstg/login.php", // -- mudar IP --
                            //"http://127.0.0.1/FindEstg/login.php", // -- mudar IP --
                            "POST",
                            field,
                            data
                        )
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                progressBar.visibility = View.GONE
                                val result = putData.result

                                if(result.equals("Login Success")){

                                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@Login,HomePage::class.java)
                                    startActivity(intent)

                                }else{
                                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                }




                            }
                        }
                        //End Write and Read data with URL
                    }


                } else {
                    Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
                }




            }

        })








    }

    fun SignUpHere(view: View) {
        val intent = Intent(this@Login,SignUp::class.java)
        startActivity(intent)
    }
}