package com.wppiotrek.gradle.android.backup_plugin

import com.android.build.gradle.api.ApplicationVariant


fun ApplicationVariant.getVariantFiles(): Variant {
    val backupVariant = Variant(this.name.capitalize())

    this.outputs.forEach {
        backupVariant.addFile(it.outputFile.path)
    }

    this.mappingFile?.let {
        if (it.exists()) {
            backupVariant.addFile(it.path)
        }
    }
    return backupVariant
}