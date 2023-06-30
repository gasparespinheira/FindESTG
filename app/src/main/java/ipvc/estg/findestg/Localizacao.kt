package ipvc.estg.findestg

data class Localizacao(var descricao : String,
                       var tipo : String,
                       var isExpandable: Boolean = false){
                       constructor(): this("", "")
                       }