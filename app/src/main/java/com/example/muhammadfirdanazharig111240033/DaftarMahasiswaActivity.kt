package com.example.muhammadfirdanazharig111240033

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import java.security.MessageDigest

class DaftarMahasiswaActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    // Fungsi validasi email
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Fungsi hash MD5
    private fun getMD5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray())
        val hexString = StringBuilder()
        for (byte in bytes) {
            val hex = Integer.toHexString(0xFF and byte.toInt())
            if (hex.length == 1) hexString.append('0')
            hexString.append(hex)
        }
        return hexString.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_mahasiswa)

        // Inisialisasi objek dari layout
        val inputDataNIM: EditText = findViewById(R.id.inputDaftarNIM)
        val inputDataNama: EditText = findViewById(R.id.inputDaftarNama)
        val inputDataEmail: EditText = findViewById(R.id.inputDaftarEmail)
        val inputDataPassword: EditText = findViewById(R.id.inputDaftarPassword)
        val btnAksiDaftar: Button = findViewById(R.id.btnAksiDaftar)
        val btnAksiBersih: Button = findViewById(R.id.btnAksiBersih)
        val btnAksiBatal: Button = findViewById(R.id.btnAksiBatal)

        databaseHelper = DBHelper(this)

        // Tombol Daftar
        btnAksiDaftar.setOnClickListener {
            val NIM = inputDataNIM.text.toString()
            val Nama = inputDataNama.text.toString()
            val Email = inputDataEmail.text.toString()
            val Password = inputDataPassword.text.toString()

            if (NIM.isEmpty() || Nama.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
                Toast.makeText(this, "Input Data Masih Kosong", Toast.LENGTH_SHORT).show()
            } else if (!isValidEmail(Email)) {
                Toast.makeText(this, "E-Mail Tidak Valid", Toast.LENGTH_SHORT).show()
            } else {
                val hashedPassword = getMD5Hash(Password)

                // Buka Akses DB
                val db = databaseHelper.writableDatabase
                val insertValues = ContentValues().apply {
                    put("nim", NIM)
                    put("nama", Nama)
                    put("email",Email)
                    put("password",hashedPassword)
                }
                val result = db.insert("TBL_MHS", null, insertValues)
                db.close()

                // Cek jika Kueri Sukses
                if(result !=-1L)
                {
                    Toast.makeText(applicationContext,"Kueri Sukses",Toast.LENGTH_SHORT).show()
                    finish()
                }
                else
                    Toast.makeText(applicationContext,"Kueri Gagal",Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "NIM: $NIM\nNama: $Nama", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Email: $Email\nPassword (MD5): $hashedPassword", Toast.LENGTH_SHORT).show()
            }
        }

        // Tombol Bersihkan
        btnAksiBersih.setOnClickListener {
            inputDataNIM.setText("")
            inputDataNama.setText("")
            inputDataEmail.setText("")
            inputDataPassword.setText("")
        }

        // Tombol Batal
        btnAksiBatal.setOnClickListener {
            finish()
        }
    }
}