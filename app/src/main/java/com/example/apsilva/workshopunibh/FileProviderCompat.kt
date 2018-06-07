package com.example.apsilva.workshopunibh

import android.database.Cursor
import android.net.Uri
import android.support.v4.content.FileProvider

class FileProviderCompat : FileProvider() {

    override  fun query(uri: Uri, projection: Array<String>, selection: String, selectionArgs: Array<String>, sortOrder: String): Cursor {
        return FileProviderCompat(super.query(uri, projection, selection, selectionArgs, sortOrder))
    }
}
