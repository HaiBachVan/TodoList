package app.todo.item

class ItemPopup {
    var title: String? = null
    var image: Int = 0

    constructor(title: String, image: Int) {
        this.title = title
        this.image = image
    }

    constructor(title: String) {
        this.title = title
    }

}
