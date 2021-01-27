package com.huawei.hwsqlite;

import android.database.Cursor;
import android.os.CancellationSignal;
import com.huawei.hwsqlite.SQLiteDatabase;

public final class SQLiteDirectCursorDriver implements SQLiteCursorDriver {
    private final CancellationSignal mCancellationSignal;
    private final SQLiteDatabase mDatabase;
    private final String mEditTable;
    private SQLiteQuery mQuery;
    private final String mSql;

    public SQLiteDirectCursorDriver(SQLiteDatabase db, String sql, String editTable, CancellationSignal cancellationSignal) {
        this.mDatabase = db;
        this.mEditTable = editTable;
        this.mSql = sql;
        this.mCancellationSignal = cancellationSignal;
    }

    @Override // com.huawei.hwsqlite.SQLiteCursorDriver
    public Cursor query(SQLiteDatabase.CursorFactory factory, String[] selectionArgs) {
        Cursor cursor;
        SQLiteQuery query = new SQLiteQuery(this.mDatabase, this.mSql, this.mCancellationSignal);
        try {
            query.bindAllArgsAsStrings(selectionArgs);
            if (factory == null) {
                cursor = new SQLiteCursor(this, this.mEditTable, query);
            } else {
                cursor = factory.newCursor(this.mDatabase, this, this.mEditTable, query);
            }
            this.mQuery = query;
            return cursor;
        } catch (RuntimeException ex) {
            query.close();
            throw ex;
        }
    }

    @Override // com.huawei.hwsqlite.SQLiteCursorDriver
    public void cursorClosed() {
    }

    @Override // com.huawei.hwsqlite.SQLiteCursorDriver
    public void setBindArguments(String[] bindArgs) {
        SQLiteQuery sQLiteQuery = this.mQuery;
        if (sQLiteQuery != null) {
            sQLiteQuery.bindAllArgsAsStrings(bindArgs);
        }
    }

    @Override // com.huawei.hwsqlite.SQLiteCursorDriver
    public void cursorDeactivated() {
    }

    @Override // com.huawei.hwsqlite.SQLiteCursorDriver
    public void cursorRequeried(Cursor cursor) {
    }

    public String toString() {
        return "SQLiteDirectCursorDriver: " + this.mSql;
    }
}