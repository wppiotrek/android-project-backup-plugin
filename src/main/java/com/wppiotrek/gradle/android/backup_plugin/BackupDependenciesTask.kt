package com.wppiotrek.gradle.android.backup_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File


open class BackupDependenciesTask : DefaultTask() {

    @TaskAction
    fun taskAction() {
        val android = project.extensions.getByType(AppExtension::class.java)

        println("Project name: $project.name")

        project.configurations.forEach {
            println("|-- configuration [${it.name}]")
        }

        val compileConfiguration = project.configurations.getByName("compile")

        val dest = File(project.buildDir, "/dependencies/")
        if (!dest.exists())
            dest.mkdir()


        if (android.applicationVariants.size > 1) {


            android.applicationVariants.forEach {
                if (!it.name.contains("Release"))
                    return@forEach

                val configurationName = "${it.name.capitalize()}ReleaseCompile"
            }
        }

        compileConfiguration?.let {
            val resolvedConfiguration = it.resolvedConfiguration
            val resolvedArtifacts = resolvedConfiguration.resolvedArtifacts
            resolvedArtifacts.forEach {
                println(it.file.path)

                it.file.copyTo(File(dest, it.file.name), true)

            }
        }


    }
}