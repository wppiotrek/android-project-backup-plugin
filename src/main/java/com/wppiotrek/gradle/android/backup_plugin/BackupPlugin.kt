package com.wppiotrek.gradle.android.backup_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Zip


class BackupPlugin : Plugin<Project> {
    val CONFIG_EXTENSIONS = "backupPluginConfig"

    private val GROUP = "backup plugin"

    override fun apply(project: Project) {

        val android = project.extensions.getByType(AppExtension::class.java)
        setupExtensions(project)

        val zipTask = project.tasks.create("zipBackup", Zip::class.java).apply {
            group = GROUP
        }

        val backupTask = project.tasks.create("prepareBackup", PrepareBackupTask::class.java).apply {
            this.zipTask = zipTask
            group = GROUP
        }


        zipTask.dependsOn(backupTask)
    }

    private fun setupExtensions(project: Project) {
        project.extensions.create(CONFIG_EXTENSIONS, ConfigExtensions::class.java).apply {
            flavors = project.container(FlavorConfig::class.java)
        }
    }
}