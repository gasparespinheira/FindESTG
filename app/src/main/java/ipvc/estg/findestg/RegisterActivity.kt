package ipvc.estg.findestg

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterActivity"
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth

        emailEditText = findViewById(R.id.email_edittext)
        passwordEditText = findViewById(R.id.password_edittext)
        registerButton = findViewById(R.id.register_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                // Sign in success, update UI with the signed-in user's information
                val user = auth.currentUser
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)

                // Add user data to Firestore
                val db = Firebase.firestore
                val data = hashMapOf(
                    "email" to email,
                    "password" to password
                )
                if (user != null) {
                    db.collection("user")
                        .document(authResult.user?.uid ?: "")
                        .set(data)
                        .addOnSuccessListener {
                            Log.d(TAG, "DocumentSnapshot added with ID: ${authResult.user?.uid}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                // If sign in fails, display a message to the user.
                Toast.makeText(
                    baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.w(TAG, "createUserWithEmailAndPassword:failure", e)
            }
    }
            }
