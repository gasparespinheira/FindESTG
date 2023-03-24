package ipvc.estg.findestg

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.vishnusivadas.advanced_httpurlconnection.PutData


class SignUp : AppCompatActivity() {

    private lateinit var textInputEditTextFullname: TextInputEditText
    private lateinit var textInputEditTextUsername: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var buttonSignUp: Button
    private lateinit var textViewLogin:  TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textInputEditTextFullname = findViewById(R.id.fullname)
        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        textInputEditTextEmail = findViewById(R.id.email)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        textViewLogin = findViewById(R.id.loginText)


        buttonSignUp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val fullname = textInputEditTextFullname.text.toString()
                val username = textInputEditTextUsername.text.toString()
                val password = textInputEditTextPassword.text.toString()
                val email = textInputEditTextEmail.text.toString()



                if (fullname.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {



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
                        val data = arrayOfNulls<String>(4)
                        data[0] = fullname
                        data[1] = username
                        data[2] = password
                        data[3] = email
                        val putData = PutData(
                            "https://localhost/FindEstg/signup.php",
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


                } else {
                    Toast.makeText(applicationContext, "All fields required", Toast.LENGTH_SHORT).show()
                }




            }

        })








    }

}