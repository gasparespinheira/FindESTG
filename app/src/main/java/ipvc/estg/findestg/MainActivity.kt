package ipvc.estg.findestg

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var localizacaoList: ArrayList<Localizacao>
    private lateinit var localizacao_recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localizacao_recycler_view = findViewById<RecyclerView>(R.id.localizacoes_recyclerview)
        localizacao_recycler_view.layoutManager = LinearLayoutManager(this)

        localizacaoList = arrayListOf()

        getDataLocalizacoes()
    }

    fun getLocalizacaoSalas(view: View?) {

    }
    fun getLocalizacaoAuditorios(view: View) {

    }
    fun getLocalizacaoServicos(view: View) {

    }

    private fun getDataLocalizacoes() {
        db = FirebaseFirestore.getInstance()
        db.collection("localizacoes")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                    for(data in it.documents) {
                        val localizacao: Localizacao? = data.toObject(Localizacao::class.java)
                        if (localizacao != null) {
                            localizacaoList.add(localizacao)
                        }
                    }
                localizacao_recycler_view.adapter = LocalizacaoListAdapter(localizacaoList)
            }

            .addOnFailureListener {
                Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }
}

