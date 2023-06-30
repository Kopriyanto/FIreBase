package com.example.firebase.Fitur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.firebase.Mahasiswa
import com.example.firebase.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Insert : AppCompatActivity() {
    private lateinit var inputNim: TextInputEditText
    private lateinit var inputNama: TextInputEditText
    private lateinit var inputTelepon: TextInputEditText
    private lateinit var insertButton: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        inputNim = findViewById(R.id.inputNimIns)
        inputNama = findViewById(R.id.inputNamaIns)
        inputTelepon = findViewById(R.id.inputTeleponIns)
        insertButton = findViewById(R.id.btnTambah)

        database = FirebaseDatabase.getInstance().getReference("mahasiswa")
        insertButton.setOnClickListener {
            val nim = inputNim.text.toString().trim()
            val nama = inputNama.text.toString().trim()
            val telepon = inputTelepon.text.toString().trim()
            if (nim.isNotEmpty() && nama.isNotEmpty() && telepon.isNotEmpty()) {
                tambahDataMahasiswa(nama, nim, telepon)
            } else {
                Toast.makeText(this@Insert, "Data gagal dimasukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun tambahDataMahasiswa(nama: String, nim: String, telepon: String) {
        val mahasiswaId = database.push().key
        val mahasiswa = Mahasiswa(nama, nim, telepon)
        if (mahasiswaId != null) {
            database.child(mahasiswaId).setValue(mahasiswa)
                .addOnCompleteListener {
                    Toast.makeText(this@Insert, "Tambah Data Baru Berhasil", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@Insert, "Tambah Data Baru Gagal", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
