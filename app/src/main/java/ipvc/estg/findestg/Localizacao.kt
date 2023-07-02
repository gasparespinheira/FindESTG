package ipvc.estg.findestg

data class Localizacao(
    var id: String,
    var descricao: String,
    var tipo: String,
    var isExpandable: Boolean = false,
    var isFavorite: Boolean = false
) {
    constructor() : this("", "", "")
}