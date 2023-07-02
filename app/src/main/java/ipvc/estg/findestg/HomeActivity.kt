package ipvc.estg.findestg


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private var db = Firebase.firestore
    private lateinit var localizacaoList: ArrayList<Localizacao>
    private lateinit var localizacaoFavoritoList: ArrayList<Localizacao>
    private lateinit var localizacao_recycler_view: RecyclerView
    private var valor_botao_lista_localizacoes: Int? = null
    private var valor_botao_lista_favoritos: Int? = null

    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        localizacao_recycler_view = findViewById<RecyclerView>(R.id.localizacoes_recyclerview)
        localizacao_recycler_view.layoutManager = LinearLayoutManager(this)

        localizacaoList = arrayListOf()
        localizacaoFavoritoList = arrayListOf()

        getDataLocalizacoes()

        Toast.makeText(this@HomeActivity, "Listagem localizações", Toast.LENGTH_LONG).show()

        user = FirebaseAuth.getInstance()
    }

    fun getLocalizacaoSalas(view: View?) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()

            valor_botao_lista_favoritos = 1
            getDataFavoritosUser()
            Toast.makeText(this@HomeActivity, "Listagem localizações favoritas filtrada pelo tipo sala", Toast.LENGTH_LONG).show()
        }

        if (localizacaoList.isNotEmpty()) {
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 1
            getDataLocalizacoes()
            Toast.makeText(this@HomeActivity, "Listagem localizações filtrada pelo tipo sala", Toast.LENGTH_LONG).show()
        }
    }

    fun getLocalizacaoAuditorios(view: View) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()

            valor_botao_lista_favoritos = 2
            getDataFavoritosUser()
            Toast.makeText(this@HomeActivity, "Listagem localizações favoritas filtrada pelo tipo auditório", Toast.LENGTH_LONG).show()
        }

        if (localizacaoList.isNotEmpty()) {
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 2
            getDataLocalizacoes()
            Toast.makeText(this@HomeActivity, "Listagem localizações filtrada pelo tipo auditório", Toast.LENGTH_LONG).show()
        }
    }

    fun getLocalizacaoServicos(view: View) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()
            valor_botao_lista_favoritos = 3
            getDataFavoritosUser()
            Toast.makeText(this@HomeActivity, "Listagem localizações favoritas filtrada pelo tipo serviço", Toast.LENGTH_LONG).show()
        }

        if (localizacaoList.isNotEmpty()){
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 3
            getDataLocalizacoes()
            Toast.makeText(this@HomeActivity, "Listagem localizações filtrada pelo tipo serviço", Toast.LENGTH_LONG).show()
        }
    }

    fun getTodasLocalizacoes(view: View) {
        localizacaoFavoritoList.clear()
        localizacaoList.clear()
        valor_botao_lista_localizacoes = null
        getDataLocalizacoes()
        Toast.makeText(this@HomeActivity, "Listagem localizações", Toast.LENGTH_LONG).show()
    }

    fun getTodasLocalizacoesFavoritos(view: View) {
        localizacaoFavoritoList.clear()
        localizacaoList.clear()
        valor_botao_lista_favoritos = null
        getDataFavoritosUser()
        Toast.makeText(this@HomeActivity, "Listagem localizações favoritas", Toast.LENGTH_SHORT).show()
    }

    fun getDataLocalizacoes() {
        localizacaoFavoritoList.clear()
        localizacaoList.clear()

        db = FirebaseFirestore.getInstance()
        db.collection("localizacoes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (data in querySnapshot.documents) {
                        val localizacao: Localizacao? = data.toObject(Localizacao::class.java)

                        if (localizacao != null) {
                            localizacao.id = data.id
                            checkIfLocalizacaoIsFavorite(localizacao)
                            localizacaoList.add(localizacao)
                        }

                        if (valor_botao_lista_localizacoes == 1) {
                            val filteredList = localizacaoList.filter { localizacao ->
                                localizacao.tipo == "sala"
                            }
                            // Create the adapter with the filtered list
                            val adapter = LocalizacaoListAdapter(filteredList, this)
                            localizacao_recycler_view.adapter = adapter

                        } else if (valor_botao_lista_localizacoes == 2) {
                            val filteredList = localizacaoList.filter { localizacao ->
                                localizacao.tipo == "auditorio"
                            }

                            // Create the adapter with the filtered list
                            val adapter = LocalizacaoListAdapter(filteredList, this)
                            localizacao_recycler_view.adapter = adapter
                        } else if (valor_botao_lista_localizacoes == 3) {
                            val filteredList = localizacaoList.filter { localizacao ->
                                localizacao.tipo == "servico"
                            }

                            // Create the adapter with the filtered list
                            val adapter = LocalizacaoListAdapter(filteredList, this)
                            localizacao_recycler_view.adapter = adapter
                        } else {
                            val adapter = LocalizacaoListAdapter(localizacaoList, this)
                            localizacao_recycler_view.adapter = adapter
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@HomeActivity, "Falha na listagem das localizações", Toast.LENGTH_LONG).show()
            }
    }

    fun getDataFavoritosUser() {
        localizacaoList.clear()
        localizacaoFavoritoList.clear()

        val userId = user.currentUser?.uid

        if (userId != null) {
            db.collection("favoritos")
                .whereEqualTo("id_user", userId)
                .get()
                .addOnSuccessListener { favoritosDocuments ->
                    for (favoritosDocument in favoritosDocuments) {
                        val idLocalizacao = favoritosDocument.getString("id_localizacao")

                        if (idLocalizacao != null) {
                            db.collection("localizacoes")
                                .document(idLocalizacao)
                                .get()
                                .addOnSuccessListener { localizacaoDoc ->
                                    val id = localizacaoDoc.id
                                    val descricao = localizacaoDoc.getString("descricao")
                                    val tipo = localizacaoDoc.getString("tipo")
                                    Log.d("FirestoreData", "Tipo: $tipo")

                                    if (id != null && descricao != null && tipo != null) {
                                        val localizacao = Localizacao(id, descricao, tipo)
                                        checkIfLocalizacaoIsFavorite(localizacao)
                                        localizacaoFavoritoList.add(localizacao)
                                    }

                                    if (valor_botao_lista_favoritos == 1) {
                                        val filteredList = localizacaoFavoritoList.filter { localizacao ->
                                            localizacao.tipo == "sala"
                                        }
                                        // Create the adapter with the filtered list
                                        val adapter = LocalizacaoFavoritoListAdapter(filteredList, this)
                                        localizacao_recycler_view.adapter = adapter

                                    } else if (valor_botao_lista_favoritos == 2) {
                                        val filteredList = localizacaoFavoritoList.filter { localizacao ->
                                            localizacao.tipo == "auditorio"
                                        }

                                        // Create the adapter with the filtered list
                                        val adapter = LocalizacaoFavoritoListAdapter(filteredList, this)
                                        localizacao_recycler_view.adapter = adapter

                                    } else if (valor_botao_lista_favoritos == 3) {
                                        val filteredList = localizacaoFavoritoList.filter { localizacao ->
                                            localizacao.tipo == "servico"
                                        }

                                        // Create the adapter with the filtered list
                                        val adapter = LocalizacaoFavoritoListAdapter(filteredList, this)
                                        localizacao_recycler_view.adapter = adapter

                                    } else {
                                        val adapter = LocalizacaoFavoritoListAdapter(localizacaoFavoritoList, this)
                                        localizacao_recycler_view.adapter = adapter
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(this@HomeActivity, "Erro a obter localização", Toast.LENGTH_LONG).show()
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@HomeActivity, "Erro ao obter os favoritos", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun checkIfLocalizacaoIsFavorite(localizacao: Localizacao) {
        val userId = user.currentUser?.uid

        if (userId != null) {
            db.collection("favoritos")
                .whereEqualTo("id_user", userId)
                .whereEqualTo("id_localizacao", localizacao.id)
                .get()
                .addOnSuccessListener { favoritosDocuments ->
                    if (!favoritosDocuments.isEmpty) {
                        localizacao.isFavorite = true
                    } else {
                        localizacao.isFavorite = false
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this@HomeActivity, "Erro a verificar se localização está na listagem de favoritos", Toast.LENGTH_LONG).show()
                }
        }
    }
}

