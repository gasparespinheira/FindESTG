package ipvc.estg.findestg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.google.firebase.auth.FirebaseAuth
import ipvc.estg.findestg.databinding.ActivityLoginBinding
import ipvc.estg.findestg.databinding.ActivityLogoutBinding

class Logout : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        user = FirebaseAuth.getInstance()

        if (user.currentUser != null) {
            user.currentUser?.let {

                binding.textViewEmail.text = it.email

            }

        }

        binding.buttonLogout.setOnClickListener{
            user.signOut()
            startActivity(
                Intent(
                    this,
                    Register::class.java
                )
            )
            finish()
        }


    }
}