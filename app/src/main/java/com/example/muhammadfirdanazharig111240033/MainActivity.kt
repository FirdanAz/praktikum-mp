package com.example.muhammadfirdanazharig111240033

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    fun getMD5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray())
        // Konversi ke Hexa
        val hexString = StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(0xFF and bytes[i].toInt())
            if (hex.length == 1) {
                hexString.append('0')
            }
            hexString.append(hex)
        }
        return hexString.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi Objek Activity
        val inputNIM = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val inputPassword = findViewById<EditText>(R.id.editTextTextPassword)
        val btnMasuk = findViewById<Button>(R.id.btnSubmit)
        val btnDaftar = findViewById<Button>(R.id.button2)
        val btnTutup = findViewById<Button>(R.id.button3)

        databaseHelper = DBHelper(this)

        btnMasuk.setOnClickListener {
            val dataNIM: String = inputNIM.text.toString()
            val dataPassword: String = inputPassword.text.toString()
            // Tampilkan Hasil
            // Toast.makeText(applicationContext, "$dataNIM $dataPassword", Toast.LENGTH_SHORT).show()

            // Ubah Password ke MD5
            val hashedPassword = getMD5Hash(dataPassword)
            // Buat Kueri
            val query = "SELECT * FROM TBL_MHS WHERE nim = '"+dataNIM+"' " +
                    "AND password = '"+hashedPassword+"'"
            // Buka Akses DB
            val db = databaseHelper.readableDatabase
            val cur = db.rawQuery("SELECT nim, nama, email, password FROM TBL_MHS", null)
            while (cur.moveToNext()) {
                Log.e("CHECK_DB", "nim=${cur.getString(0)}, nama=${cur.getString(1)}, email=${cur.getString(2)}, pass=${cur.getString(3)}")
            }
            cur.close()

            val cursor = db.rawQuery(query, null)
            // Cek Hasil Kueri
            val result = cursor.moveToFirst()
            if(result)
            {
                val dataNama = cursor.getString(cursor
                    .getColumnIndexOrThrow("nama"))
                val dataEmail = cursor.getString(cursor
                    .getColumnIndexOrThrow("email"))

                // Kirimkan data via Intent
                var intent: Intent = Intent(this, ProfileMahasiswaActivity::class.java)
                intent.putExtra("nim",dataNIM)
                intent.putExtra("nama",dataNama)
                intent.putExtra("email",dataEmail)
                startActivity(intent)
            }
            else {
                Toast.makeText(
                    applicationContext, result.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                inputNIM.setText("")
                inputPassword.setText("")
            }
        }

        btnDaftar.setOnClickListener {
            val intent = Intent(this, DaftarMahasiswaActivity::class.java)
            startActivity(intent)
        }

        btnTutup.setOnClickListener {
            // Perintahkan Activity untuk Selesai
            finish()
        }
    }
}