package com.swaptech.meet.presentation

import org.osmdroid.tileprovider.tilesource.TileSourcePolicy
import org.osmdroid.tileprovider.tilesource.XYTileSource

//TODO: Change URLs!!!!
const val BASE_URL = "http://192.168.0.4:8000/api/"
const val MEET_POINTS_WS_URL = "ws://192.168.0.4:8000/api/meet/ws"
const val SHARED_PREFS_FILE_NAME = "preferences"
const val AUTH_TOKEN_KEY = "AUTH_TOKEN"
const val USER_DB_NAME = "user_db"
const val SPLASH_SCREEN_WAIT_TIME_MS = 250L
const val WORLD_LEVEL_ZOOM = 4.0
const val CITY_LEVEL_ZOOM = 14.0
const val MAX_EMAIL_LENGTH = 320
const val MAX_NAME_SURNAME_LENGTH = 20
const val MIN_PASSWORD_LENGTH = 8
const val MAX_PASSWORD_LENGTH = 40
const val MAX_CITY_NAME_LENGTH = 200
const val MAX_MEET_POINT_NAME_LENGTH = 15
const val MAX_MEET_POINT_DESCRIPTION_LENGTH = 140


val MAPNIK_512 = XYTileSource(
    "Mapnik",
    0, 19, 512, ".png", arrayOf(
        "https://a.tile.openstreetmap.org/",
        "https://b.tile.openstreetmap.org/",
        "https://c.tile.openstreetmap.org/"
    ), "© OpenStreetMap contributors",
    TileSourcePolicy(
        2,
        TileSourcePolicy.FLAG_NO_BULK
                or TileSourcePolicy.FLAG_NO_PREVENTIVE
                or TileSourcePolicy.FLAG_USER_AGENT_MEANINGFUL
                or TileSourcePolicy.FLAG_USER_AGENT_NORMALIZED
    )
)
