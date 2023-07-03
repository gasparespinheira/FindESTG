package ipvc.estg.findestg

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LocalizacaoFavoritoListAdapter(

    private val localizacaoFavoritoList: List<Localizacao>,
    private val homeActivity: HomeActivity) :
    RecyclerView.Adapter<LocalizacaoFavoritoViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalizacaoFavoritoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_localizacao_favorito_list, parent, false)

        return LocalizacaoFavoritoViewHolder(itemView).apply {
            itemView.setOnClickListener {
                val previousSelectedPosition = selectedItemPosition
                selectedItemPosition = adapterPosition

                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedItemPosition)
            }

            _deleteFavoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val localizacao = localizacaoFavoritoList[position]
                    val db = FirebaseFirestore.getInstance()
                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

                    val query = db.collection("favoritos")
                        .whereEqualTo("id_localizacao", localizacao.id)
                        .whereEqualTo("id_user", currentUserUid)

                    query.get()
                        .addOnSuccessListener { snapshot ->
                            for (document in snapshot.documents) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        Toast.makeText(itemView.context, "Localização removida com sucesso dos favoritos", Toast.LENGTH_SHORT).show()
                                        homeActivity.getDataFavoritosUser()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(itemView.context, "Falha na remoção da localização dos favoritos", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(itemView.context, "Falha ao receber a listagem dos favoritos", Toast.LENGTH_SHORT).show()
                        }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return localizacaoFavoritoList.size
    }

    override fun onBindViewHolder(holder: LocalizacaoFavoritoViewHolder, position: Int) {
        val currentLocalizacao = localizacaoFavoritoList[position]
        val isItemSelected = position == selectedItemPosition

        holder.bind(currentLocalizacao, isItemSelected)
    }
}

class LocalizacaoFavoritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val _descricao: TextView = itemView.findViewById(R.id.descricao_item)
    val _linhaOcultaLayout: LinearLayout = itemView.findViewById(R.id.linha_oculta)
    val _verMaisButton: Button = itemView.findViewById(R.id.ver_mais_button)
    val _mapButton: ImageButton = itemView.findViewById(R.id.map_button)
    val _deleteFavoriteButton: ImageButton = itemView.findViewById(R.id.delete_favorite_button)

    fun bind(localizacao: Localizacao, isItemSelected: Boolean) {
        _descricao.text = localizacao.descricao

        if (isItemSelected) {
            _linhaOcultaLayout.visibility = View.VISIBLE
        } else {
            _linhaOcultaLayout.visibility = View.GONE
        }

        if (localizacao.isFavorite) {
            _deleteFavoriteButton.visibility = View.VISIBLE
        } else {
            _deleteFavoriteButton.visibility = View.GONE
        }

        _verMaisButton.setOnClickListener {
            val descricao = localizacao.descricao

            when (descricao) {
                "Serviços Académicos" -> {
                    val intent = Intent(itemView.context, InfoAcademicos::class.java)
                    itemView.context.startActivity(intent)
                }
                "Bar" -> {
                    val intent = Intent(itemView.context, InfoBar::class.java)
                    itemView.context.startActivity(intent)
                }
                "Cantina" -> {
                    val intent = Intent(itemView.context, InfoCantina::class.java)
                    itemView.context.startActivity(intent)
                }
            }
        }

        _mapButton.setOnClickListener {
            val descricao_localizacao = localizacao.descricao

            // Salvar descricao_localizacao no SharedPreferences
            val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.context)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("descricao_localizacao", descricao_localizacao)
            editor.apply()

            when (descricao_localizacao) {
                "Serviços Académicos" -> {
                    val intent = Intent(itemView.context, QrCode_MainActivity::class.java)
                    intent.putExtra("descricao_localizacao", descricao_localizacao)
                    itemView.context.startActivity(intent)
                }
                "Bar" -> {
                    val intent = Intent(itemView.context,  QrCode_MainActivity::class.java)
                    intent.putExtra("descricao_localizacao", descricao_localizacao)
                    itemView.context.startActivity(intent)
                }
                "Cantina" -> {
                    val intent = Intent(itemView.context, QrCode_MainActivity::class.java)
                    intent.putExtra("descricao_localizacao", descricao_localizacao)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}