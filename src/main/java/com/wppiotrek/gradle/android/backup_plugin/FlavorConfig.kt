package com.wppiotrek.gradle.android.backup_plugin


class FlavorConfig(val name: String) {

    val files = ArrayList<String>()

    fun include(vararg files: String) {
        this.files.addAll(files)
    }

}