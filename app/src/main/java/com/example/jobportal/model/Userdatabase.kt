package com.example.jobportal.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Ensure all models that are @Entity are listed here.
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            // Use the safe-call operator with synchronization
            return INSTANCE ?: synchronized(this) {
                // Check instance again inside synchronized block
                INSTANCE ?: Room.databaseBuilder(
                    // CRITICAL: Always use applicationContext for singletons like Room
                    context.applicationContext,
                    UserDatabase::class.java,
                    "job_portal_user_db" // Use a unique name
                )
                    // This is generally needed for database migrations/upgrades, but good practice to include
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it } // Assign the built instance to INSTANCE
            }
        }
    }
}
