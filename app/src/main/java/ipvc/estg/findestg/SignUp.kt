package ipvc.estg.findestg

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
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
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        textInputEditTextFullname = findViewById(R.id.fullname)
        textInputEditTextUsername = findViewById(R.id.username)
        textInputEditTextPassword = findViewById(R.id.password)
        textInputEditTextEmail = findViewById(R.id.email)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        textViewLogin = findViewById(R.id.loginText)
        progressBar = findViewById(R.id.progress)


        buttonSignUp.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val fullname = textInputEditTextFullname.text.toString()
                val username = textInputEditTextUsername.text.toString()
                val password = textInputEditTextPassword.text.toString()
                val email = textInputEditTextEmail.text.toString()



                if (fullname.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    progressBar.visibility = View.VISIBLE

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
                            "http://192.168.1.74/FindEstg/signup.php", // -- mudar IP --
                            "POST",
                            field,
                            data
                        )
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                progressBar.visibility = View.GONE
                                val result = putData.result

                                if(result.equals("Sign Up Success")){

                                    Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@SignUp,Login::class.java)
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

    fun LoginHere(view: View) {

        val intent = Intent(this@SignUp,Login::class.java)
        startActivity(intent)

    }

}