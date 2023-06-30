package ipvc.estg.findestg

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import ipvc.estg.findestg.databinding.ActivityLogoutBinding

class HomeActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var localizacaoList: ArrayList<Localizacao>
    private lateinit var localizacao_recycler_view: RecyclerView
    private var valor_botao: Int? = null

    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        localizacao_recycler_view = findViewById<RecyclerView>(R.id.localizacoes_recyclerview)
        localizacao_recycler_view.layoutManager = LinearLayoutManager(this)

        localizacaoList = arrayListOf()

        getDataLocalizacoes()

        user = FirebaseAuth.getInstance()

        if (user.currentUser != null) {
            user.currentUser?.let {

            }
        }
    }

    fun getLocalizacaoSalas(view: View?) {
        valor_botao = 1
        localizacaoList.clear()
        getDataLocalizacoes()
    }
    fun getLocalizacaoAuditorios(view: View) {
        valor_botao = 2
        localizacaoList.clear()
        getDataLocalizacoes()
    }
    fun getLocalizacaoServicos(view: View) {
        valor_botao = 3
        localizacaoList.clear()
        getDataLocalizacoes()
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
                            if (valor_botao == 1) {
                                if (localizacao.tipo == "sala") {
                                    localizacaoList.add(localizacao)
                                }
                            } else if (valor_botao == 2) {
                                if (localizacao.tipo == "auditorio") {
                                    localizacaoList.add(localizacao)
                                }
                            } else if (valor_botao == 3) {
                                if (localizacao.tipo == "servico") {
                                    localizacaoList.add(localizacao)
                                }
                            } else {
                                   localizacaoList.add(localizacao)
                            }
                        }
                    }
                localizacao_recycler_view.adapter = LocalizacaoListAdapter(localizacaoList)
            }
            .addOnFailureListener {
                Toast.makeText(this@HomeActivity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }
}

