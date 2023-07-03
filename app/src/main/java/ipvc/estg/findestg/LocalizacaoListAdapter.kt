package ipvc.estg.findestg

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LocalizacaoListAdapter(
    private val localizacaoList: List<Localizacao>,
    private val homeActivity: HomeActivity) :
    RecyclerView.Adapter<LocalizacaoViewHolder>() {

    private var selectedItemPosition = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalizacaoViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_localizacao_list, parent, false)

        return LocalizacaoViewHolder(itemView).apply {
            itemView.setOnClickListener {
                val previousSelectedPosition = selectedItemPosition
                selectedItemPosition = adapterPosition

                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(selectedItemPosition)
            }

            _addFavoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val localizacao = localizacaoList[position]
                    val db = FirebaseFirestore.getInstance()
                    val favoriteRef = db.collection("favoritos").document()

                    val favoriteData = hashMapOf(
                        "id_localizacao" to localizacao.id,
                        "id_user" to FirebaseAuth.getInstance().currentUser?.uid
                    )

                    favoriteRef.set(favoriteData)
                        .addOnSuccessListener {
                            Toast.makeText(itemView.context, "Localização adicionado com sucesso aos favoritos", Toast.LENGTH_SHORT).show()

                            homeActivity.getDataLocalizacoes()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(itemView.context, "Falha a adicionar a localização aos favoritos", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            _deleteFavoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val localizacao = localizacaoList[position]
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

                                        homeActivity.getDataLocalizacoes()
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
        return localizacaoList.size
    }

    override fun onBindViewHolder(holder: LocalizacaoViewHolder, position: Int) {
        val currentLocalizacao = localizacaoList[position]
        val isItemSelected = position == selectedItemPosition

        holder.bind(currentLocalizacao, isItemSelected)
    }
}

class LocalizacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val _descricao: TextView = itemView.findViewById(R.id.descricao_item)
    val _linhaOcultaLayout: LinearLayout = itemView.findViewById(R.id.linha_oculta)
    val _verMaisButton: Button = itemView.findViewById(R.id.ver_mais_button)
    val _mapButton: ImageButton = itemView.findViewById(R.id.map_button)
    val _addFavoriteButton: ImageButton = itemView.findViewById(R.id.add_favorite_button)
    val _deleteFavoriteButton: ImageButton = itemView.findViewById(R.id.delete_favorite_button)

    fun bind(localizacao: Localizacao, isItemSelected: Boolean) {
        _descricao.text = localizacao.descricao

        if (isItemSelected) {
            _linhaOcultaLayout.visibility = View.VISIBLE
        } else {
            _linhaOcultaLayout.visibility = View.GONE
        }

        if (localizacao.isFavorite) {
            _addFavoriteButton.visibility = View.GONE
            _deleteFavoriteButton.visibility = View.VISIBLE
        } else {
            _addFavoriteButton.visibility = View.VISIBLE
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
            val localizacaoId = localizacao.id
            val descricao = localizacao.descricao

            // Show Toast
            Toast.makeText(itemView.context, "Localizacao ID: $localizacaoId", Toast.LENGTH_SHORT).show()

            when (descricao) {
                "Serviços Académicos" -> {
                    val intent = Intent(itemView.context, InfoAcademicos::class.java)
                    intent.putExtra("localizacaoId", localizacaoId)
                    itemView.context.startActivity(intent)
                }
                "Bar" -> {
                    val intent = Intent(itemView.context, InfoBar::class.java)
                    intent.putExtra("localizacaoId", localizacaoId)
                    itemView.context.startActivity(intent)
                }
                "Cantina" -> {
                    val intent = Intent(itemView.context, InfoCantina::class.java)
                    intent.putExtra("localizacaoId", localizacaoId)
                    itemView.context.startActivity(intent)
                }
            }
            //val localizacaoId = intent.getIntExtra("localizacaoId", -1)
        }
    }
}


