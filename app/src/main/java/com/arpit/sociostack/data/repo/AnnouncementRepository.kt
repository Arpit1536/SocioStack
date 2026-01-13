package com.arpit.sociostack.data.repo

import com.arpit.sociostack.data.model.Announcement
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AnnouncementRepository {

    private val db = FirebaseFirestore.getInstance()
    private val announcementsRef = db.collection("announcements")

    fun addAnnouncement(
        title: String,
        message: String,
        priority: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val announcement = hashMapOf(
            "title" to title,
            "message" to message,
            "priority" to priority,
            "timestamp" to Timestamp.now()
        )

        announcementsRef
            .add(announcement)
            .addOnSuccessListener { documentReference ->
                documentReference.update("id", documentReference.id)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    fun fetchAnnouncements(
        onResult: (List<Announcement>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        announcementsRef
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    onResult(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot.documents.mapNotNull { document ->
                    document.toObject(Announcement::class.java)?.copy(
                        id = document.id
                    )
                }

                onResult(list)
            }
    }
}