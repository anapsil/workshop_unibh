package com.example.apsilva.workshopunibh

import android.database.Cursor
import android.database.CursorWrapper
import android.net.Uri
import android.provider.MediaStore
import java.util.Arrays


class CursorCompatWrapper : CursorWrapper{
    private var fakeDataColumn: Int = 0
    private var fakeMimeTypeColumn: Int = 0
    private var mimeType: String? = null
    private var uriForDataColumn: Uri? = null

    /**
     * Constructor.
     *
     * @param cursor the Cursor to be wrapped
     */

    constructor(cursor: Cursor): this(cursor, null)

    /**
     * Constructor.
     *
     * @param cursor the Cursor to be wrapped
     * @param mimeType the MIME type of the content represented
     * by the Uri that generated this Cursor, should
     * we need it
     */
    constructor(cursor: Cursor, mimeType: String?): this(cursor, mimeType, null)

    /**
     * Constructor.
     *
     * @param cursor the Cursor to be wrapped
     * @param mimeType the MIME type of the content represented
     * by the Uri that generated this Cursor, should
     * we need it
     * @param uriForDataColumn Uri to return for the _DATA column
     */
    constructor(cursor: Cursor, mimeType: String?,
                                  uriForDataColumn: Uri?): super(cursor) {

        this.uriForDataColumn = uriForDataColumn

        fakeDataColumn = if (cursor.getColumnIndex(MediaStore.MediaColumns.DATA) >= 0) {
            -1
        } else {
            cursor.getColumnCount()
        }

        fakeMimeTypeColumn = if (cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE) >= 0) {
            -1
        } else if (fakeDataColumn == -1) {
            cursor.getColumnCount()
        } else {
            fakeDataColumn + 1
        }

        this.mimeType = mimeType
    }

    /**
     * {@inheritDoc}
     */
    override fun getColumnCount(): Int {
        var count = super.getColumnCount()

        if (!cursorHasDataColumn()) {
            count += 1
        }

        if (!cursorHasMimeTypeColumn()) {
            count += 1
        }

        return count
    }

    /**
     * {@inheritDoc}
     */
    override fun getColumnIndex(columnName: String): Int {
        if (!cursorHasDataColumn() && MediaStore.MediaColumns.DATA == columnName) {
            return fakeDataColumn
        }

        return if (!cursorHasMimeTypeColumn() && MediaStore.MediaColumns.MIME_TYPE == columnName) {
            fakeMimeTypeColumn
        } else super.getColumnIndex(columnName)

    }

    /**
     * {@inheritDoc}
     */
    override fun getColumnName(columnIndex: Int): String {
        if (columnIndex == fakeDataColumn) {
            return MediaStore.MediaColumns.DATA
        }

        return if (columnIndex == fakeMimeTypeColumn) {
            MediaStore.MediaColumns.MIME_TYPE
        } else super.getColumnName(columnIndex)

    }

    /**
     * {@inheritDoc}
     */
    override fun getColumnNames(): Array<String> {
        if (cursorHasDataColumn() && cursorHasMimeTypeColumn()) {
            return super.getColumnNames()
        }

        val orig = super.getColumnNames()
        val result = Arrays.copyOf(orig, columnCount)

        if (!cursorHasDataColumn()) {
            result[fakeDataColumn] = MediaStore.MediaColumns.DATA
        }

        if (!cursorHasMimeTypeColumn()) {
            result[fakeMimeTypeColumn] = MediaStore.MediaColumns.MIME_TYPE
        }

        return result
    }

    /**
     * {@inheritDoc}
     */
    override fun getString(columnIndex: Int): String? {
        if (!cursorHasDataColumn() && columnIndex == fakeDataColumn) {
            return if (uriForDataColumn != null) {
                uriForDataColumn!!.toString()
            } else null

        }

        return if (!cursorHasMimeTypeColumn() && columnIndex == fakeMimeTypeColumn) {
            mimeType
        } else super.getString(columnIndex)

    }

    /**
     * {@inheritDoc}
     */
    override fun getType(columnIndex: Int): Int {
        if (!cursorHasDataColumn() && columnIndex == fakeDataColumn) {
            return Cursor.FIELD_TYPE_STRING
        }

        return if (!cursorHasMimeTypeColumn() && columnIndex == fakeMimeTypeColumn) {
            Cursor.FIELD_TYPE_STRING
        } else super.getType(columnIndex)

    }

    /**
     * @return true if the Cursor has a _DATA column, false otherwise
     */
    private fun cursorHasDataColumn(): Boolean {
        return fakeDataColumn == -1
    }

    /**
     * @return true if the Cursor has a MIME_TYPE column, false
     * otherwise
     */
    private fun cursorHasMimeTypeColumn(): Boolean {
        return fakeMimeTypeColumn == -1
    }
}