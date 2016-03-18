package com.elpassion.android.sharedpreferences

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.Mockito.`when` as on

@RunWith(AndroidJUnit4::class)
class CachedSharedPreferencesTestCase {

    val repositoryMock = Mockito.mock(SharedPreferenceRepository::class.java) as SharedPreferenceRepository<SimpleStructure>
    val cachingRepository = CachingSharedPreferenceRepository(repositoryMock)

    @Test
    fun twoReadsShouldCallRepositoryOnlyOnce() {
        cachingRepository.read("key")
        cachingRepository.read("key")

        verify(repositoryMock, times(1)).read(anyString())
    }

    @Test
    fun twoCallsShouldReturnSameObject() {
        val value = SimpleStructure(0)
        on(repositoryMock.read("key")).thenReturn(value)

        assertSame(value, cachingRepository.read("key"))
        assertSame(value, cachingRepository.read("key"))
    }

    @Test
    fun shouldCacheItemsWithDifferentKeys() {
        val value0 = SimpleStructure(0)
        val value1 = SimpleStructure(1)

        on(repositoryMock.read("key0")).thenReturn(value0)
        on(repositoryMock.read("key1")).thenReturn(value1)

        assertSame(value0, cachingRepository.read("key0"))
        assertSame(value1, cachingRepository.read("key1"))
    }

    data class SimpleStructure(val value: Int)
}