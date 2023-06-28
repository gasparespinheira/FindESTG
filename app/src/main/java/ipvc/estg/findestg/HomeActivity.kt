package ipvc.estg.findestg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //inicio do menu
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)

        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {
                }
                R.id.nav_lista_favoritos -> {
                    val intent = Intent(this@HomeActivity, FavoritosActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.nav_lista_favoritos -> {
                }
                R.id.nav_logout -> {
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
        //fim do menu
    }

    //metodo do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}