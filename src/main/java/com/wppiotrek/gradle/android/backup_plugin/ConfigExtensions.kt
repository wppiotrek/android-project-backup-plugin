package com.wppiotrek.gradle.android.backup_plugin

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer


open class ConfigExtensions {

    var archiveName: String? = null
    var flavors: NamedDomainObjectContainer<FlavorConfig>? = null

    val additionalFiles = ArrayList<String>()

    fun include(vararg files: String) {
        this.additionalFiles.addAll(files)
    }

    fun getFilesToBackup() = additionalFiles


    fun flavors(action: Action<NamedDomainObjectContainer<FlavorConfig>>) {
        action.execute(this.flavors)
    }
}