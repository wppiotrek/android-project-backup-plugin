package com.wppiotrek.gradle.android.backup_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File


open class PrepareBackupTask : DefaultTask() {

    lateinit var zipTask: org.gradle.api.tasks.bundling.Zip
    val variants = ArrayList<Variant>()

    val prefix = "|-- "
    val emptyPrefix = "    "


    @TaskAction
    fun taskAction() {

        val config = project.extensions.getByType(ConfigExtensions::class.java)

        val android = project.extensions.getByType(AppExtension::class.java)

        val backupDir = createDirIfNotExist(project.buildDir, "preparedForBackup", true)

        prepareVariants(android)
        copyFiles(backupDir, config)
        addToZipTask(backupDir, config)
    }

    private fun prepareVariants(android: AppExtension) {
        android.applicationVariants.forEach { variant ->
            if (variant.name.contains("release", true))
                variants.add(variant.getVariantFiles())
        }
    }

    private fun copyFiles(backupDir: File, config: ConfigExtensions) {
        logInfo("Backup package contains files:")
        copyFiles(backupDir, config.getFilesToBackup())

        if (variants.size == 1) {
            variants.first().let {
                logInfo("${it.name.capitalize()} variant:", prefix)
                copyFiles(backupDir, it.files, emptyPrefix)
            }
        } else {
            variants.forEach { variant ->
                logInfo("${variant.name.capitalize()} variant:", prefix)
                val variantDir = createDirIfNotExist(backupDir, variant.name)
                copyFiles(variantDir, variant.files, emptyPrefix)

                config.flavors?.find { variant.name.contains(it.name, true) }?.let { flavor ->
                    copyFiles(variantDir, flavor.files.map { it }, emptyPrefix)
                }
            }
        }

    }

    private fun addToZipTask(backupDir: File, config: ConfigExtensions) {
        zipTask.from(backupDir.path)
        config.archiveName?.let {
            zipTask.archiveName = it
        }
    }

    fun createDirIfNotExist(parentDir: File, name: String, clearDir: Boolean = false): File {
        val file = File(parentDir, name)
        if (clearDir && file.exists())
            file.deleteRecursively()
        if (!file.exists())
            file.mkdir()
        return file
    }

    fun copyFiles(dstDir: File, files: List<String>, logPrefix: String = "") {
        project.copy { copy ->
            files.forEach { file ->
                val f = File(file)
                if (!f.isDirectory) {
                    logInfo("$prefix$file", logPrefix)
                    copy.from(file)
                }
            }
            copy.into(dstDir)
        }
        files.forEach {
            val file = File(it)
            if (file.isDirectory) {
                logInfo(file.name, logPrefix + prefix)
                val dir = createDirIfNotExist(dstDir, file.name)
                copyFiles(dir, file.listFiles().map { it.path }, emptyPrefix + logPrefix)
            }
        }
    }

    fun logInfo(content: String, prefix: String = "") {
        logger.info("$prefix$content")
    }

}