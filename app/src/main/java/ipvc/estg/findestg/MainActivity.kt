package ipvc.estg.findestg

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private var valor_botao: String? = null
    private lateinit var localizacaoList: ArrayList<Localizacao>
    private lateinit var localizacao_recycler_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        localizacao_recycler_view = findViewById<RecyclerView>(R.id.localizacoes_recyclerview)
        localizacao_recycler_view.layoutManager = LinearLayoutManager(this)

        localizacaoList = arrayListOf()

        db = FirebaseFirestore.getInstance()
        db.collection("localizacoes")
            .get()
            .addOnSuccessListener {
                if(!it.isEmpty)
                    for(data in it.documents) {
                        val localizacao: Localizacao? = data.toObject(Localizacao::class.java)
                        if(localizacao != null) {
                            if (valor_botao == "salas"){
                                if (localizacao.tipo == "sala"){
                                    localizacaoList.add(localizacao)
                                }
                            } else if (valor_botao == "auditorios"){
                                if (localizacao.tipo == "auditorio"){
                                    localizacaoList.add(localizacao)
                                }
                            } else if (valor_botao == "servicos"){
                                if (localizacao.tipo == "servico"){
                                    localizacaoList.add(localizacao)
                                }
                            }
                        }
                    }
                    localizacao_recycler_view.adapter = LocalizacaoListAdapter(localizacaoList)
            }

            .addOnFailureListener {
                Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
            }
    }

    fun getLocalizacaoSalas(view: View?) {
        valor_botao = "salas"
    }
    fun getLocalizacaoAuditorios(view: View) {
        valor_botao = "auditorios"
    }
    fun getLocalizacaoServicos(view: View) {
        valor_botao = "servicos"
    }
}

