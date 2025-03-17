sealed class Item(
    open val id: Int,
    open val title: String,
    open var available: Boolean
) {
    abstract fun getShortInfo(): String
    abstract fun getDetailedInfo(): String
}

data class Book(
    override val id: Int,
    override val title: String,
    override var available: Boolean,
    val author: String,
    val pageCount: Int
) : Item(id, title, available) {
    override fun getShortInfo(): String {
        return "\"$title\" доступна: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "книга: $title ($pageCount стр.) автора: $author с id: $id доступна: ${if (available) "Да" else "Нет"}"
    }
}

data class Newspaper(
    override val id: Int,
    override val title: String,
    override var available: Boolean,
    val issueNumber: Int
) : Item(id, title, available) {
    override fun getShortInfo(): String {
        return "\"$title\" доступна: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "выпуск: $issueNumber газеты $title с id: $id доступен: ${if (available) "Да" else "Нет"}"
    }
}

data class Disc(
    override val id: Int,
    override val title: String,
    override var available: Boolean,
    val type: String // CD или DVD
) : Item(id, title, available) {
    override fun getShortInfo(): String {
        return "\"$title\" доступен: ${if (available) "Да" else "Нет"}"
    }

    override fun getDetailedInfo(): String {
        return "$type $title доступен: ${if (available) "Да" else "Нет"}"
    }
}

fun main() {
    val books = listOf(
        Book(1, "Война и мир", true, "Лев Толстой", 1225),
        Book(2, "1984", true, "Джордж Оруэлл", 328)
    )

    val newspapers = listOf(
        Newspaper(3, "Комсомольская правда", true, 794),
        Newspaper(4, "Известия", false, 1234)
    )

    val discs = listOf(
        Disc(5, "The Dark Side of the Moon", true, "CD"),
        Disc(6, "Thriller", false, "DVD")
    )

    var currentList: List<Item>? = null

    while (true) {
        println("Выберите тип объекта:")
        println("1. Показать книги")
        println("2. Показать газеты")
        println("3. Показать диски")
        println("0. Выход")

        when (readLine()?.toIntOrNull()) {
            1 -> currentList = books
            2 -> currentList = newspapers
            3 -> currentList = discs
            0 -> return
            else -> {
                println("Неверный ввод, попробуйте снова.")
                continue
            }
        }

        if (currentList != null) {
            showItems(currentList!!)
            selectItem(currentList!!)
        }
    }
}

fun showItems(items: List<Item>) {
    println("Список объектов:")
    items.forEachIndexed { index, item ->
        println("${index + 1}. ${item.getShortInfo()}")
    }
}

fun selectItem(items: List<Item>) {
    println("Введите номер объекта для выбора или 0 для возврата:")

    val choice = readLine()?.toIntOrNull()
    if (choice == null || choice < 0 || choice > items.size) {
        println("Неверный ввод, попробуйте снова.")
        return
    }

    if (choice == 0) return

    val selectedItem = items[choice - 1]
    println("Вы выбрали: ${selectedItem.getShortInfo()}")

    while (true) {
        println("Выберите действие:")
        println("1. Взять домой")
        println("2. Читать в читальном зале")
        println("3. Показать подробную информацию")
        println("4. Вернуть")
        println("0. Вернуться к выбору типа объекта")

        when (readLine()?.toIntOrNull()) {
            1 -> takeHome(selectedItem)
            2 -> readInLibrary(selectedItem)
            3 -> showDetails(selectedItem)
            4 -> returnItem(selectedItem)
            0 -> return
            else -> println("Неверный ввод, попробуйте снова.")
        }
    }
}

fun takeHome(item: Item) {
    when (item) {
        is Newspaper -> {
            println("Газеты нельзя брать домой.")
        }
        is Book, is Disc -> {
            if (!item.available) {
                println("${item.title} недоступен(а) для взятия домой.")
                return
            }
            item.available = false
            println("${item::class.simpleName} ${item.id} \"${item.title}\" взяли домой.")
        }
        else -> {
            println("Неизвестный тип объекта.")
        }
    }
}

fun readInLibrary(item: Item) {
    when (item) {
        is Disc -> {
            println("Диски нельзя читать в читальном зале.")
        }
        is Book, is Newspaper -> {
            if (!item.available) {
                println("${item.title} недоступен(а) для чтения в читальном зале.")
                return
            }
            item.available = false
            println("${item::class.simpleName} ${item.id} \"${item.title}\" взяли в читальный зал.")
        }
        else -> {
            println("Неизвестный тип объекта.")
        }
    }
}

fun showDetails(item: Item) {
    println("Подробная информация:")
    println(item.getDetailedInfo())
}

fun returnItem(item: Item) {
    if (item.available) {
        println("${item.title} уже доступен(а).")
        return
    }

    item.available = true
    println("${item::class.simpleName} ${item.id} \"${item.title}\" вернули.")
}

main()