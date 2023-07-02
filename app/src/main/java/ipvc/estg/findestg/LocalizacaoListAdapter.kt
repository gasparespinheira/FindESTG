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

                    // Add the localizacao to favorites in Firestore
                    favoriteRef.set(favoriteData)
                        .addOnSuccessListener {
                            // Handle successful addition to favorites
                            // Update the UI accordingly
                            Toast.makeText(itemView.context, "Added to favorites", Toast.LENGTH_SHORT).show()

                            homeActivity.getDataLocalizacoes()
                        }
                        .addOnFailureListener { e ->
                            // Handle failure to add to favorites
                            // Update the UI accordingly
                            Toast.makeText(itemView.context, "Failed to add to favorites: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }

            _deleteFavoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val localizacao = localizacaoList[position]
                    val db = FirebaseFirestore.getInstance()
                    val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

                    // Query the "favoritos" collection based on the conditions
                    val query = db.collection("favoritos")
                        .whereEqualTo("id_localizacao", localizacao.id)
                        .whereEqualTo("id_user", currentUserUid)

                    // Delete the matching document from "favoritos" collection
                    query.get()
                        .addOnSuccessListener { snapshot ->
                            for (document in snapshot.documents) {
                                document.reference.delete()
                                    .addOnSuccessListener {
                                        // Handle successful deletion from favorites
                                        // Update the UI accordingly
                                        Toast.makeText(itemView.context, "Deleted from favorites", Toast.LENGTH_SHORT).show()

                                        homeActivity.getDataLocalizacoes()
                                    }
                                    .addOnFailureListener { e ->
                                        // Handle failure to delete from favorites
                                        // Update the UI accordingly
                                        Toast.makeText(itemView.context, "Failed to delete from favorites: ${e.message}", Toast.LENGTH_SHORT).show()
                                        Log.e(TAG, "Failed to delete from favorites", e)
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            // Handle failure to retrieve matching documents
                            Toast.makeText(itemView.context, "Failed to retrieve favorites: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "Failed to retrieve favorites", e)
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
                else -> {
                    // Handle other cases if needed
                }
            }
        }
    }
}


