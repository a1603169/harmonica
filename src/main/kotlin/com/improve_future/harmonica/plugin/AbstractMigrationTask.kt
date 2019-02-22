/*
 * This file is part of Harmonica.
 *
 * Harmonica is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Harmonica is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Harmonica.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.improve_future.harmonica.plugin

import com.improve_future.harmonica.core.*
import com.improve_future.harmonica.service.VersionService
import org.gradle.api.tasks.Input

abstract class AbstractMigrationTask : AbstractHarmonicaTask() {
    /** The table name to store executed migration version IDs. */
    private val migrationTableName: String = "harmonica_migration"
    protected val versionService: VersionService

    init {
        versionService = VersionService(migrationTableName)
    }

    @Input
    var dbms: Dbms = Dbms.PostgreSQL

    protected fun readMigration(script: String): AbstractMigration {
        return engine.eval(removePackageStatement(script)) as AbstractMigration
    }

    protected fun createConnection(): Connection {
        return Connection(loadConfigFile())
    }

    protected companion object {
        protected fun removePackageStatement(script: String) =
            script.replace(Regex("^\\s*package\\s+.+"), "")
    }
}