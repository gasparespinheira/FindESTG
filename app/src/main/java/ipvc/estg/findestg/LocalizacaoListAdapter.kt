package ipvc.estg.findestg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocalizacaoListAdapter(private val localizacao_list: ArrayList<Localizacao>) :
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
        }
    }

    override fun getItemCount(): Int {
        return localizacao_list.size
    }

    override fun onBindViewHolder(holder: LocalizacaoViewHolder, position: Int) {
        val currentLocalizacao = localizacao_list[position]
        val isItemSelected = position == selectedItemPosition

        holder._descricao.text = currentLocalizacao.descricao

        if (isItemSelected) {
            holder._linhaOcultaLayout.visibility = View.VISIBLE
        } else {
            holder._linhaOcultaLayout.visibility = View.GONE
        }
    }
}

class LocalizacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val _descricao: TextView
    val _linhaOcultaLayout: LinearLayout

    init {
        _descricao = itemView.findViewById(R.id.descricao_item)
        _linhaOcultaLayout = itemView.findViewById(R.id.linha_oculta)
    }
}

