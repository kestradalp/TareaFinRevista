package com.example.uteqwebservice.modelos

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ArticuloModelo(a: JSONObject, var revista: String) {
    var id_ediciones: String
    var titulo: String
    var doi: String
    var fecha: String
    var galeys: JSONArray
    var linkHTML: String
    var linkPDF: String

    companion object {
        @Throws(JSONException::class)
        fun JsonObjectsBuild(
            datos: JSONArray, revista: String
        ): java.util.ArrayList<ArticuloModelo> {
            val articulos: ArrayList<ArticuloModelo> = ArrayList()
            var i = 0
            while (i < datos.length()) {
                articulos.add(ArticuloModelo(datos.getJSONObject(i), revista))
                i++
            }
            return articulos
        }
    }


    init {
        id_ediciones = a.getString("publication_id")
        titulo = a.getString("title")
        doi = a.getString("doi")
        fecha = a.getString("date_published")
        galeys = a.getJSONArray("galeys")
        linkHTML = galeys.getJSONObject(0).getString("UrlViewGalley")
        linkPDF = createLinkPdf(a)

    }

    fun createLinkPdf(a: JSONObject): String {

        var submission_id = a.getString("submission_id").toString()
        var galley_id = galeys.getJSONObject(0).getString("galley_id")
        var file_id = galeys.getJSONObject(0).getString("file_id")

        return "https://revistas.uteq.edu.ec/index.php/$revista/article/download/$submission_id/$galley_id/$file_id.pdf"
    }
}