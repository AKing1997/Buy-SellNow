package com.example.buy_sellnow.Model

import com.google.android.gms.maps.model.LatLng

data class tempUser(
    var userId: String = "",
    var userImage: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var userToken: String = "",
    var direccion: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {
    constructor() : this("", "", "", "", "", "", 0.0, 0.0)
}
