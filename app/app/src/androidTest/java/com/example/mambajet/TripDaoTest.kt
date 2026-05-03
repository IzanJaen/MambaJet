package com.example.mambajet

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mambajet.data.local.db.AppDatabase
import com.example.mambajet.data.local.db.dao.TripDao
import com.example.mambajet.data.local.db.entity.TripEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * T5.1 — Tests de integración del TripDao usando una base de datos Room en memoria.
 * Cubre todas las operaciones CRUD y la validación de nombres duplicados (T5.2).
 *
 * NOTA: Este test usa AndroidJUnit4 y ApplicationProvider, por lo que DEBE residir
 * en src/androidTest/ (no en src/test/).
 */
@RunWith(AndroidJUnit4::class)
class TripDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var tripDao: TripDao

    private val testUserId = "user_test_123"

    private fun makeTrip(
        id: String = "trip_1",
        title: String = "Roma",
        userId: String = testUserId,
        startDate: String = "01/06/2026",
        endDate: String = "07/06/2026"
    ) = TripEntity(
        id = id,
        userId = userId,
        title = title,
        startDate = startDate,
        endDate = endDate,
        description = "Descripción de prueba",
        totalBudget = 1000.0,
        spentBudget = 0.0
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        tripDao = db.tripDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // ─── INSERT ───────────────────────────────────────────────────────────────

    @Test
    fun insertTrip_and_retrieveByUser() = runBlocking {
        val trip = makeTrip()
        tripDao.insertTrip(trip)

        val result = tripDao.getTripsByUser(testUserId).first()
        assertEquals(1, result.size)
        assertEquals("Roma", result[0].title)
        assertEquals(testUserId, result[0].userId)
    }

    @Test
    fun insertTrip_otherUser_notReturnedForCurrentUser() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_A", userId = "user_A"))
        tripDao.insertTrip(makeTrip(id = "trip_B", userId = "user_B"))

        val resultA = tripDao.getTripsByUser("user_A").first()
        assertEquals(1, resultA.size)

        val resultB = tripDao.getTripsByUser("user_B").first()
        assertEquals(1, resultB.size)
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @Test
    fun updateTrip_modifiesTitleAndBudget() = runBlocking {
        val trip = makeTrip()
        tripDao.insertTrip(trip)

        val updated = trip.copy(title = "Roma Actualizada", totalBudget = 2000.0)
        tripDao.updateTrip(updated)

        val result = tripDao.getTripsByUser(testUserId).first()
        assertEquals("Roma Actualizada", result[0].title)
        assertEquals(2000.0, result[0].totalBudget, 0.0)
    }

    @Test
    fun updateSpentBudget_updatesOnlySpentBudget() = runBlocking {
        val trip = makeTrip()
        tripDao.insertTrip(trip)

        tripDao.updateSpentBudget(trip.id, 350.0)

        val result = tripDao.getTripsByUser(testUserId).first()
        assertEquals(350.0, result[0].spentBudget, 0.0)
        assertEquals(1000.0, result[0].totalBudget, 0.0) // no modificado
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @Test
    fun deleteTrip_removesFromDb() = runBlocking {
        val trip = makeTrip()
        tripDao.insertTrip(trip)

        tripDao.deleteTripById(trip.id)

        val result = tripDao.getTripsByUser(testUserId).first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun deleteTrip_doesNotAffectOtherTrips() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma"))
        tripDao.insertTrip(makeTrip(id = "trip_2", title = "Paris"))

        tripDao.deleteTripById("trip_1")

        val result = tripDao.getTripsByUser(testUserId).first()
        assertEquals(1, result.size)
        assertEquals("Paris", result[0].title)
    }

    // ─── DUPLICATE NAME (T5.2) ────────────────────────────────────────────────

    @Test
    fun countByTitleAndUser_detectsDuplicate() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma"))

        val count = tripDao.countByTitleAndUser("Roma", testUserId, excludeId = null)
        assertEquals(1, count)
    }

    @Test
    fun countByTitleAndUser_caseInsensitiveDuplicate() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma"))

        val count = tripDao.countByTitleAndUser("roma", testUserId, excludeId = null)
        assertEquals(1, count)
    }

    @Test
    fun countByTitleAndUser_excludesOwnId_onEdit() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma"))

        // Al editar el mismo viaje, no debe contar como duplicado
        val count = tripDao.countByTitleAndUser("Roma", testUserId, excludeId = "trip_1")
        assertEquals(0, count)
    }

    @Test
    fun countByTitleAndUser_differentUser_notDuplicate() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma", userId = "user_other"))

        val count = tripDao.countByTitleAndUser("Roma", testUserId, excludeId = null)
        assertEquals(0, count)
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────

    @Test
    fun getTripById_returnsCorrectTrip() = runBlocking {
        tripDao.insertTrip(makeTrip(id = "trip_1", title = "Roma"))
        tripDao.insertTrip(makeTrip(id = "trip_2", title = "Paris"))

        val found = tripDao.getTripById("trip_2")
        assertNotNull(found)
        assertEquals("Paris", found!!.title)
    }

    @Test
    fun getTripById_returnsNull_whenNotFound() = runBlocking {
        val found = tripDao.getTripById("non_existing_id")
        assertNull(found)
    }
}