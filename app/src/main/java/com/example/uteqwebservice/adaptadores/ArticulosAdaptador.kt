package com.example.uteqwebservice.adaptadores

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.uteqwebservice.R
import com.example.uteqwebservice.Actividades.ViewPDFActivity
import com.example.uteqwebservice.modelos.ArticuloModelo

class ArticulosAdaptador(
    context: Context,
    val dwlManager: DownloadManager,
    val ArticulosList: ArrayList<ArticuloModelo>,
    var revista: String
) : RecyclerView.Adapter<ArticulosAdaptador.ViewHolder>() {

    val context: Context = context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.crdvwarticulos, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtNombre.text = ArticulosList[position].titulo
        holder.txtdoi.text = ArticulosList[position].doi
        holder.txtfecha.text = ArticulosList[position].fecha
        holder.pdfLink = ArticulosList[position].linkPDF
        holder.htmlLink = ArticulosList[position].linkHTML
        holder.btnDownload.setOnClickListener { v: View ->
            downloadPDF(holder.pdfLink)
        }
    }

    override fun getItemCount(): Int {
        return ArticulosList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var txtNombre: TextView
        var txtdoi: TextView
        var txtfecha: TextView
        var btnDownload: Button
        var pdfLink: String
        var htmlLink: String

        init {
            txtNombre = itemView.findViewById(R.id.txtNombreArticulo)
            txtdoi = itemView.findViewById(R.id.txtDoiArticulo)
            txtfecha = itemView.findViewById(R.id.txtFechaArticulo)
            btnDownload = itemView.findViewById(R.id.btnViewPdf)
            pdfLink = ""
            htmlLink = ""

            itemView.setOnClickListener { v: View ->
                val intent = Intent(v.context, ViewPDFActivity::class.java)
                val bundle = Bundle()
                bundle.putString("title", txtNombre.text.toString())
                bundle.putString("url", htmlLink)
                intent.putExtras(bundle)
                ContextCompat.startActivity(v.context, intent, null)
            }
        }
    }

    fun downloadPDF(linkPDF: String) {
        val request =
            DownloadManager.Request(Uri.parse(linkPDF))
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                .setTitle("DOWNLOAD")
                .setVisibleInDownloadsUi(true)
                .setAllowedOverMetered(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(
                    context.applicationContext,
                    Environment.DIRECTORY_DOWNLOADS,
                    "downloadable.pdf"
                )

        try {
            var download: Long = dwlManager.enqueue(request)
            context.registerReceiver(
                broadcastReceiver(download), IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE
                )
            )
        } catch (e: Exception) {
            Toast.makeText(context.applicationContext, "Error: " + e.message, Toast.LENGTH_LONG)
                .show()
        }
    }

    class broadcastReceiver(var download: Long): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == download)
                Toast.makeText(context,  "Descarga completa", Toast.LENGTH_LONG).show()
        }
    }

}