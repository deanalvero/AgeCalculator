package com.lowbottgames.agecalculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.lowbottgames.agecalculator.database.PersonDatabase
import com.lowbottgames.agecalculator.database.PersonDatabaseDao
import com.lowbottgames.agecalculator.database.PersonModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersonDatabaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var personDao: PersonDatabaseDao
    private lateinit var db: PersonDatabase

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, PersonDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        personDao = db.personDatabaseDao
    }

    @After
    fun closeDatabase() {
        db.close()
    }

    @Test
    fun insertAndGetPerson() {
        val person = PersonModel(name = "Person")
        personDao.insert(person)

        val persons = personDao.getAllPersons()
        assertEquals(1, persons.getOrAwaitValue().size)
        assertEquals("Person", persons.getOrAwaitValue().get(0).name)
    }

}