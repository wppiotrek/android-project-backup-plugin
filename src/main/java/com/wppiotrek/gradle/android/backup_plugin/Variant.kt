package com.wppiotrek.gradle.android.backup_plugin


class Variant(val name: String) {

    val files = ArrayList<String>()

    fun addFile(file: String) {
        files.add(file)
    }

}