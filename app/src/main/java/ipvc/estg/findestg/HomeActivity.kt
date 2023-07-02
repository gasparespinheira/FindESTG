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

        user = FirebaseAuth.getInstance()
    }

    fun getLocalizacaoSalas(view: View?) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()

            valor_botao_lista_favoritos = 1
            getDataFavoritosUser()
        }

        if (localizacaoList.isNotEmpty()) {
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 1
            getDataLocalizacoes()
        }
    }

    fun getLocalizacaoAuditorios(view: View) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()

            valor_botao_lista_favoritos = 2
            getDataFavoritosUser()
        }

        if (localizacaoList.isNotEmpty()) {
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 2
            getDataLocalizacoes()
        }
    }

    fun getLocalizacaoServicos(view: View) {

        if (localizacaoFavoritoList.isNotEmpty()) {
            localizacaoFavoritoList.clear()
            valor_botao_lista_favoritos = 3
            getDataFavoritosUser()
        }

        if (localizacaoList.isNotEmpty()){
            localizacaoList.clear()
            valor_botao_lista_localizacoes = 3
            getDataLocalizacoes()
        }
    }

    fun getTodasLocalizacoes(view: View) {
        localizacaoFavoritoList.clear()
        localizacaoList.clear()
        valor_botao_lista_localizacoes = null
        getDataLocalizacoes()
    }

    fun getTodasLocalizacoesFavoritos(view: View) {
        localizacaoFavoritoList.clear()
        localizacaoList.clear()
        valor_botao_lista_favoritos = null
        getDataFavoritosUser()
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

                for (i in 0 until localizacao_recycler_view.childCount) {
                    val viewHolder = localizacao_recycler_view.findViewHolderForAdapterPosition(i) as? LocalizacaoViewHolder
                    if (viewHolder != null) {
                        val currentLocalizacao = localizacaoList.getOrNull(i)
                        if (currentLocalizacao != null) {
                            if (currentLocalizacao.isFavorite) {
                                viewHolder._addFavoriteButton.visibility = View.GONE
                                viewHolder._deleteFavoriteButton.visibility = View.VISIBLE
                            } else {
                                viewHolder._addFavoriteButton.visibility = View.VISIBLE
                                viewHolder._deleteFavoriteButton.visibility = View.GONE
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                Toast.makeText(this@HomeActivity, it.toString(), Toast.LENGTH_LONG).show()
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
                                    Log.d("FirestoreData", "Error getting localizacao document: ", exception)
                                }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("FirestoreData", "Error getting favoritos documents: ", exception)
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
                    Log.d("FirestoreData", "Error checking if localizacao is favorite: ", exception)
                }
        }
    }
}

