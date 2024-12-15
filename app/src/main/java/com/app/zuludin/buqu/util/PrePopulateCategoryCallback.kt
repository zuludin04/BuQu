package com.app.zuludin.buqu.util

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class PrePopulateCategoryCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        db.execSQL(
            """
            INSERT INTO [Category] ([categoryId],[name],[color],[type])
            VALUES
            ('a76c5015-34c7-4a54-bdfb-c5ed2010b7c9','Motivation','03A9F4','Quote'),
            ('a7fbe08b-74d1-4158-8dc4-5631ad102794','Character','F44336','Quote'),
            ('ca7fa67a-f11f-42f1-90fc-597924679e77','Inspiration','CDDC39','Quote'),
            ('a27e43a0-c21e-475a-8ba4-2df060379591','Funny','009688','Quote');

        """.trimIndent()
        )
    }
}