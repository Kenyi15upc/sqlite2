package com.example.sqlite.data.model

import com.google.gson.annotations.SerializedName

data class Employee(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("nombre")
    val nombre: String = "",
    @SerializedName("apellidos")
    val apellidos: String = "",
    @SerializedName("edad")
    val edad: Int = 0,
    @SerializedName("profesion")
    val profesion: String = "",
    @SerializedName("cargo")
    val cargo: String = "",
    @SerializedName("experiencia")
    val experiencia: Int = 0,
    @SerializedName("email")
    val email: String = "",
    @SerializedName("telefono")
    val telefono: String = "",
    @SerializedName("direccion")
    val direccion: String = "",
    @SerializedName("distrito")
    val distrito: String = "",
    @SerializedName("ciudad")
    val ciudad: String = "",
    @SerializedName("pais")
    val pais: String = "",
    @SerializedName("estado_civil")
    val estadoCivil: String = "",
    @SerializedName("genero")
    val genero: String = "",
    @SerializedName("salario")
    val salario: Double = 0.0,
    @SerializedName("departamento")
    val departamento: String = "",
    @SerializedName("foto")
    val foto: String = ""
) {
    val idAsInt: Int
        get() = id.toIntOrNull() ?: 0

    val fullName: String
        get() = "$nombre $apellidos".trim()
}
