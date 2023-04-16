package ipvc.estg.findestg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocalizacaoListAdapter (val localizacao_list: ArrayList<Localizacao>):RecyclerView.Adapter<LocalizacaoViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocalizacaoViewHolder {

        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_localizacao_list, parent, false);
        return LocalizacaoViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return localizacao_list.size
    }

    override fun onBindViewHolder(holder: LocalizacaoViewHolder, position: Int) {
        val currentLocalizacao = localizacao_list[position]

        holder._descricao.text = currentLocalizacao.descricao
    }
}

class LocalizacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    val _descricao: TextView

    init {
        _descricao = itemView.findViewById(R.id.descricao_item)
    }
}