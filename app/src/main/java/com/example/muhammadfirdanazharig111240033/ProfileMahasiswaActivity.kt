package com.example.muhammadfirdanazharig111240033

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileMahasiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile_mahasiswa)

        // Inisialisasi Objek
        var txtNIM = findViewById<TextView>(R.id.txtNIM)
        var txtNama = findViewById<TextView>(R.id.txtNama)
        var txtEmail = findViewById<TextView>(R.id.txtEmail)
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnKeluar = findViewById<Button>(R.id.btnKeluar)

        // Ambil Data Intent
        val dataNIM:String = intent.getStringExtra("nim").toString()
        val dataNama:String = intent.getStringExtra("nama").toString()
        val dataEmail:String = intent.getStringExtra("email").toString()
        // Tampilkan Data Intent
        txtNIM.setText("NIM : " + dataNIM)
        txtNama.setText("Nama : " + dataNama)
        txtEmail.setText("E-Mail : " + dataEmail)

        // Button Ubah
        btnEdit.setOnClickListener{
            var intent: Intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("nim",dataNIM)
            intent.putExtra("nama",dataNama)
            intent.putExtra("email",dataEmail)
            startActivity(intent)
        }
        // Button Keluar
        btnKeluar.setOnClickListener {
            finish()
        }

    }
}