package com.uniandes.vinilosapp.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.uniandes.vinilosapp.models.Album
import com.uniandes.vinilosapp.models.AlbumDetails
import com.uniandes.vinilosapp.models.Band
import com.uniandes.vinilosapp.models.Collector
import com.uniandes.vinilosapp.models.GENRE
import com.uniandes.vinilosapp.models.Musician
import com.uniandes.vinilosapp.models.Performer
import com.uniandes.vinilosapp.models.RECORD_LABEL
import com.uniandes.vinilosapp.models.Track
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import org.json.JSONArray
import org.json.JSONObject

class NetworkServiceAdapter constructor(context: Context) {
        companion object {
                const val BASE_URL = "https://vynils-backend-2a8a921f0d28.herokuapp.com/"
                var instance: NetworkServiceAdapter? = null
                fun getInstance(context: Context) =
                        instance
                                ?: synchronized(this) {
                                        instance
                                                ?: NetworkServiceAdapter(context).also {
                                                        instance = it
                                                }
                                }
        }
        private val requestQueue: RequestQueue by lazy {
                Volley.newRequestQueue(context.applicationContext)
        }
        suspend fun getAlbums() =
                suspendCoroutine<List<Album>> { cont ->
                        val list = mutableListOf<Album>()
                        requestQueue.add(
                                getRequest(
                                        "albums",
                                        { response ->
                                                val resp = JSONArray(response)
                                                for (i in 0 until resp.length()) {
                                                        val item = resp.getJSONObject(i)
                                                        val album =
                                                                Album(
                                                                        albumId = item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        cover =
                                                                                item.getString(
                                                                                        "cover"
                                                                                ),
                                                                        recordLabel =
                                                                                RECORD_LABEL
                                                                                        .fromString(
                                                                                                item.getString(
                                                                                                        "recordLabel"
                                                                                                )
                                                                                        ),
                                                                        releaseDate =
                                                                                item.getString(
                                                                                        "releaseDate"
                                                                                ),
                                                                        genre =
                                                                                GENRE.fromString(
                                                                                        item.getString(
                                                                                                "genre"
                                                                                        )
                                                                                ),
                                                                        description =
                                                                                item.getString(
                                                                                        "description"
                                                                                )
                                                                )
                                                        list.add(i, album)
                                                }
                                                cont.resume(list)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getAlbum(albumId: Int) =
                suspendCoroutine<AlbumDetails> { cont ->
                        requestQueue.add(
                                getRequest(
                                        "albums/$albumId",
                                        { response ->
                                                val item = JSONObject(response)
                                                val arrayPerformer =
                                                        JSONArray(item.getString("performers"))
                                                val arrayTracks =
                                                        JSONArray(item.getString("tracks"))
                                                val listperformer = mutableListOf<Performer>()
                                                val listtracks = mutableListOf<Track>()
                                                for (i in 0 until arrayTracks.length()) {
                                                        val item = arrayTracks.getJSONObject(i)
                                                        val track =
                                                                Track(
                                                                        trackId = item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        duration =
                                                                                item.getString(
                                                                                        "duration"
                                                                                )
                                                                )
                                                        listtracks.add(i, track)
                                                }
                                                for (i in 0 until arrayPerformer.length()) {
                                                        val item = arrayPerformer.getJSONObject(i)
                                                        val perform =
                                                                Performer(
                                                                        performerID =
                                                                                item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        image =
                                                                                item.getString(
                                                                                        "image"
                                                                                ),
                                                                        description =
                                                                                item.getString(
                                                                                        "description"
                                                                                )
                                                                )
                                                        listperformer.add(i, perform)
                                                }

                                                val album =
                                                        AlbumDetails(
                                                                albumId = item.getInt("id"),
                                                                name = item.getString("name"),
                                                                cover = item.getString("cover"),
                                                                recordLabel =
                                                                        item.getString(
                                                                                "recordLabel"
                                                                        ),
                                                                releaseDate =
                                                                        item.getString(
                                                                                "releaseDate"
                                                                        ),
                                                                genre = item.getString("genre"),
                                                                description =
                                                                        item.getString(
                                                                                "description"
                                                                        ),
                                                                performers = listperformer,
                                                                tracks = listtracks
                                                        )
                                                cont.resume(album)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getCollectors() =
                suspendCoroutine<List<Collector>> { cont ->
                        val list = mutableListOf<Collector>()
                        requestQueue.add(
                                getRequest(
                                        "collectors",
                                        { response ->
                                                val resp = JSONArray(response)
                                                for (i in 0 until resp.length()) {
                                                        val item = resp.getJSONObject(i)
                                                        val collector =
                                                                Collector(
                                                                        collectorID =
                                                                                item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        telephone =
                                                                                item.getString(
                                                                                        "telephone"
                                                                                ),
                                                                        email =
                                                                                item.getString(
                                                                                        "email"
                                                                                )
                                                                )
                                                        list.add(collector)
                                                }
                                                cont.resume(list)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getPerformers() =
                suspendCoroutine<List<Performer>> { cont ->
                        val list = mutableListOf<Performer>()
                        requestQueue.add(
                                getRequest(
                                        "performers",
                                        { response ->
                                                val resp = JSONArray(response)
                                                for (i in 0 until resp.length()) {
                                                        val item = resp.getJSONObject(i)
                                                        val performer =
                                                                Performer(
                                                                        performerID =
                                                                                item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        image =
                                                                                item.getString(
                                                                                        "image"
                                                                                ),
                                                                        description =
                                                                                item.getString(
                                                                                        "description"
                                                                                )
                                                                )
                                                        list.add(performer)
                                                }
                                                cont.resume(list)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getMusicians() =
                suspendCoroutine<List<Performer>> { cont ->
                        val list = mutableListOf<Performer>()
                        requestQueue.add(
                                getRequest(
                                        "musicians",
                                        { response ->
                                                val resp = JSONArray(response)
                                                for (i in 0 until resp.length()) {
                                                        val item = resp.getJSONObject(i)
                                                        val performer =
                                                                Performer(
                                                                        performerID =
                                                                                item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        image =
                                                                                item.getString(
                                                                                        "image"
                                                                                ),
                                                                        description =
                                                                                item.getString(
                                                                                        "description"
                                                                                )
                                                                )
                                                        list.add(performer)
                                                }
                                                cont.resume(list)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getBands() =
                suspendCoroutine<List<Performer>> { cont ->
                        val list = mutableListOf<Performer>()
                        requestQueue.add(
                                getRequest(
                                        "bands",
                                        { response ->
                                                val resp = JSONArray(response)
                                                for (i in 0 until resp.length()) {
                                                        val item = resp.getJSONObject(i)
                                                        val performer =
                                                                Performer(
                                                                        performerID =
                                                                                item.getInt("id"),
                                                                        name =
                                                                                item.getString(
                                                                                        "name"
                                                                                ),
                                                                        image =
                                                                                item.getString(
                                                                                        "image"
                                                                                ),
                                                                        description =
                                                                                item.getString(
                                                                                        "description"
                                                                                )
                                                                )
                                                        list.add(performer)
                                                }
                                                cont.resume(list)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getMusician(musicianId: Int) =
                suspendCoroutine<Musician> { cont ->
                        requestQueue.add(
                                getRequest(
                                        "musicians/$musicianId",
                                        { response ->
                                                val item = JSONObject(response)
                                                val musician =
                                                        Musician(
                                                                performerID = item.getInt("id"),
                                                                name = item.getString("name"),
                                                                image = item.getString("image"),
                                                                description =
                                                                        item.getString(
                                                                                "description"
                                                                        ),
                                                                birthDate =
                                                                        item.optString(
                                                                                "birthDate",
                                                                                ""
                                                                        ),
                                                                band = null // We could fetch the
                                                                // band data if
                                                                // needed
                                                                )
                                                cont.resume(musician)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun getBand(bandId: Int) =
                suspendCoroutine<Band> { cont ->
                        requestQueue.add(
                                getRequest(
                                        "bands/$bandId",
                                        { response ->
                                                val item = JSONObject(response)

                                                // Parse musicians if they're included in the
                                                // response
                                                val musiciansList = mutableListOf<Musician>()
                                                if (item.has("musicians")) {
                                                        val musiciansArray =
                                                                item.getJSONArray("musicians")
                                                        for (i in 0 until musiciansArray.length()) {
                                                                val musicianItem =
                                                                        musiciansArray
                                                                                .getJSONObject(i)
                                                                val musician =
                                                                        Musician(
                                                                                performerID =
                                                                                        musicianItem
                                                                                                .getInt(
                                                                                                        "id"
                                                                                                ),
                                                                                name =
                                                                                        musicianItem
                                                                                                .getString(
                                                                                                        "name"
                                                                                                ),
                                                                                image =
                                                                                        musicianItem
                                                                                                .getString(
                                                                                                        "image"
                                                                                                ),
                                                                                description =
                                                                                        musicianItem
                                                                                                .getString(
                                                                                                        "description"
                                                                                                ),
                                                                                birthDate =
                                                                                        musicianItem
                                                                                                .optString(
                                                                                                        "birthDate",
                                                                                                        ""
                                                                                                )
                                                                        )
                                                                musiciansList.add(musician)
                                                        }
                                                }

                                                val band =
                                                        Band(
                                                                performerID = item.getInt("id"),
                                                                name = item.getString("name"),
                                                                image = item.getString("image"),
                                                                description =
                                                                        item.getString(
                                                                                "description"
                                                                        ),
                                                                creationDate =
                                                                        item.optString(
                                                                                "creationDate",
                                                                                ""
                                                                        ),
                                                                musicians = musiciansList
                                                        )
                                                cont.resume(band)
                                        },
                                        { throw it }
                                )
                        )
                }

        suspend fun createAlbum(album: Album) =
                suspendCoroutine<Album> { cont ->
                        val body =
                                JSONObject().apply {
                                        put("name", album.name)
                                        put("cover", album.cover)
                                        put("releaseDate", album.releaseDate)
                                        put("description", album.description)
                                        put("genre", album.genre.toString())
                                        put("recordLabel", album.recordLabel.toString())
                                }

                        requestQueue.add(
                                postRequest(
                                        "albums",
                                        body,
                                        { response ->
                                                val album =
                                                        Album(
                                                                albumId = response.getInt("id"),
                                                                name = response.getString("name"),
                                                                cover = response.getString("cover"),
                                                                recordLabel =
                                                                        RECORD_LABEL.fromString(
                                                                                response.getString(
                                                                                        "recordLabel"
                                                                                )
                                                                        ),
                                                                releaseDate =
                                                                        response.getString(
                                                                                "releaseDate"
                                                                        ),
                                                                genre =
                                                                        GENRE.fromString(
                                                                                response.getString(
                                                                                        "genre"
                                                                                )
                                                                        ),
                                                                description =
                                                                        response.getString(
                                                                                "description"
                                                                        )
                                                        )
                                                cont.resume(album)
                                        },
                                        { error -> cont.resumeWithException(error) }
                                )
                        )
                }

        suspend fun createTrack(albumId: Int, name: String, duration: String) =
                suspendCoroutine<Track> { cont ->
                        val body =
                                JSONObject().apply {
                                        put("name", name)
                                        put("duration", duration)
                                }

                        requestQueue.add(
                                postRequest(
                                        "albums/$albumId/tracks",
                                        body,
                                        { response ->
                                                val track =
                                                        Track(
                                                                trackId = response.getInt("id"),
                                                                name = response.getString("name"),
                                                                duration =
                                                                        response.getString(
                                                                                "duration"
                                                                        )
                                                        )
                                                cont.resume(track)
                                        },
                                        { error -> cont.resumeWithException(error) }
                                )
                        )
                }

        private fun getRequest(
                path: String,
                responseListener: Response.Listener<String>,
                errorListener: Response.ErrorListener
        ): StringRequest {
                return StringRequest(
                        Request.Method.GET,
                        BASE_URL + path,
                        responseListener,
                        errorListener
                )
        }
        private fun postRequest(
                path: String,
                body: JSONObject,
                responseListener: Response.Listener<JSONObject>,
                errorListener: Response.ErrorListener
        ): JsonObjectRequest {
                return JsonObjectRequest(
                        Request.Method.POST,
                        BASE_URL + path,
                        body,
                        responseListener,
                        errorListener
                )
        }
        private fun putRequest(
                path: String,
                body: JSONObject,
                responseListener: Response.Listener<JSONObject>,
                errorListener: Response.ErrorListener
        ): JsonObjectRequest {
                return JsonObjectRequest(
                        Request.Method.PUT,
                        BASE_URL + path,
                        body,
                        responseListener,
                        errorListener
                )
        }
}
