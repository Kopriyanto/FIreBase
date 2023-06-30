package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.Fitur.Edit
import com.example.firebase.Fitur.Insert
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var adapter: MahasiswaAdapter
    private lateinit var userList: ArrayList<Mahasiswa>
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val insertButton: Button = findViewById(R.id.insertButton)
        insertButton.setOnClickListener {
            insertMahasiswa()
        }
        userRecyclerView = findViewById(R.id.recyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()
        adapter = MahasiswaAdapter(userList, this, ::editMahasiswa, ::deleteMahasiswa)
        userRecyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("mahasiswa")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                userList
                for (snapshot in dataSnapshot.children) {
                    val nim = snapshot.child("nim").getValue(String::class.java)
                    val mahasiswa = snapshot.getValue(Mahasiswa::class.java)
                    mahasiswa?.let {
                        it.nim = nim
                        it.nim?.let { nim -> // Check if nim is not
                            null
                            it.nim = nim
                            userList.add(it)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun editMahasiswa(mahasiswa: Mahasiswa) {
        val intent = Intent(this, Edit::class.java)
        intent.putExtra("nama", mahasiswa.nama)
        intent.putExtra("nim", mahasiswa.nim)
        intent.putExtra("telp", mahasiswa.telp)
        startActivity(intent)
    }
    private fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        val userId = mahasiswa.nim
        userId?.let {
            database.child(it).removeValue().addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(this@MainActivity, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Data Gagal Dihapus", Toast.LENGTH_SHORT).show()
                }
            } }
    }
    private fun insertMahasiswa() {
        val intent = Intent(this, Insert::class.java)
        startActivity(intent)
    }
}
