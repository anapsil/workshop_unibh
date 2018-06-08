package com.example.apsilva.workshopunibh

import android.database.Cursor
import android.net.Uri
import android.support.v4.content.FileProvider

class FileProviderCompat : FileProvider() {

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {
        return CursorCompatWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder))
    }

}
