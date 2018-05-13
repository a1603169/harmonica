package com.improve_future.harmonica.plugin

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.util.*

open class MigrationCreate: AbstractTask() {
    @Input
    val migrationName: String = ""

    private val dateFormat: SimpleDateFormat =
            SimpleDateFormat("yyyyMMddHHmmssSSS")

    private fun composeName(): String {
        if (migrationName.isNotBlank())
            return migrationName

        return "Migration"
    }

    @TaskAction
    fun createMigration() {
        val version = dateFormat.format(Date())
        val migrationFile = Paths.get(
                findMigrationDir().absolutePath,
                "${version}_" + composeName() + ".kts").toFile()
        migrationFile.parentFile.mkdirs()
        migrationFile.createNewFile()
        migrationFile.writeText("""import com.improve_future.harmonica.core.AbstractMigration

object : AbstractMigration() {
    override fun up() {
        createTable("table_name") {
            integer("column_1")
            varchar("column_2")
        }
    }

    override fun down() {
        dropTable("table_name")
    }
}""")
        println("Created ${migrationFile.absolutePath}")
    }
}