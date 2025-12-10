package com.example.muhammadfirdanazharig111240033

import android.content.ContentValues
import android.os.Bundle
import android.util.Patterns
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import java.security.MessageDigest

class EditProfileActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DBHelper

    fun getMD5Hash(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(input.toByteArray())
// convert the byte array to hexadecimal string
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
    fun isValidEmail(email: String): Boolean {
        val result:Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        return result
    }

    private lateinit var txtUbahProfile: TextView
    private lateinit var editNIM: TextInputEditText
    private lateinit var editNama: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var txtVerifikasi: TextView
    private lateinit var editPassword: TextInputEditText
    private lateinit var btnKonfirmasi: MaterialButton
    private lateinit var btnBatal: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        txtUbahProfile = findViewById(R.id.txtUbahProfile)
        editNIM = findViewById(R.id.editNIM)
        editNama = findViewById(R.id.editNama)
        editEmail = findViewById(R.id.editEmail)
        txtVerifikasi = findViewById(R.id.txtVerifikasi)
        editPassword = findViewById(R.id.editPassword)
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi)
        btnBatal = findViewById(R.id.btnBatal)

        val dataNIM:String = intent.getStringExtra("nim").toString()
        val dataNama:String = intent.getStringExtra("nama").toString()
        val dataEmail:String = intent.getStringExtra("email").toString()

        databaseHelper = DBHelper(this)

        btnKonfirmasi.setOnClickListener {
            val newNama:String = editNama.text.toString()
            val newEmail:String = editEmail.text.toString()
            val dataPassword:String = editPassword.text.toString()

            // Verifikasi Data
            if(newNama.equals("")||newEmail.equals("")||dataPassword.equals(""))
            {
                Toast.makeText(applicationContext,"Kolom Kosong",
                    Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(isValidEmail(newEmail))
                {
                    val hashedPassword:String = getMD5Hash(dataPassword)
// Buka Akses Tulis DB
                    val db = databaseHelper.writableDatabase
                    val values = ContentValues().apply {
                        put("nama", newNama)
                        put("email", newEmail)
                    }
                    val result = db.update("TBL_MHS", values,
                        "nim = ? AND password = ?", arrayOf(dataNIM,hashedPassword))
                    print(result.toString());
                    db.close()

                    // Cek Sukses Kueri
                    if(result > 0)
                    {
                        Toast.makeText(applicationContext,"Update Berhasil",
                            Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    else
                        Toast.makeText(applicationContext,"Update Gagal",
                            Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(applicationContext,"E-Mail Tidak Valid",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        btnBatal.setOnClickListener {
            finish()
        }


    }
}