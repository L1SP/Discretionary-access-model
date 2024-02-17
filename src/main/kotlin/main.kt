import cli.CLI
import controller.AccessTableController
import repository.AccessTableRepository
import service.AccessTableService

fun main() {
    val repository = AccessTableRepository()
    val service = AccessTableService(repository)
    val controller = AccessTableController(service)
    val cli = CLI(controller)
    cli.run()
}