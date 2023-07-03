package ipvc.estg.findestg

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

object MenuHelper {
    private lateinit var user: FirebaseAuth

    fun setupMenu(activity: AppCompatActivity) {

        user = FirebaseAuth.getInstance()

        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawer_layout)
        val navView: NavigationView = activity.findViewById(R.id.nav_view)

        if (user.currentUser != null) {
            user.currentUser?.let {
                val includedView = navView.getHeaderView(0)
                val email_utilizador = includedView.findViewById<TextView>(R.id.email_utilizador)
                email_utilizador.text = it.email
            }
        }

        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                    activity.startActivity(Intent(activity, HomeActivity::class.java))
                    activity.finish()
                }
                R.id.nav_logout -> {
                    user.signOut()
                    activity.startActivity(Intent(activity, Register::class.java))
                    activity.finish()
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}


