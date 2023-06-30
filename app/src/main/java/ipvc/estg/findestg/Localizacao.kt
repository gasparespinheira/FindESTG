package ipvc.estg.findestg

data class Localizacao(val id: String,
                       var descricao : String,
                       var tipo : String,
                       var isExpandable: Boolean = false){
                       constructor(): this("", "", "")
                       }