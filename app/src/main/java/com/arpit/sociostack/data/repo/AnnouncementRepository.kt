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
        val announcement = Announcement(
            title = title,
            message = message,
            priority = priority,
            timestamp = Timestamp.now()
        )

        announcementsRef
            .add(announcement)
            .addOnSuccessListener { onSuccess() }
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

                val list = snapshot.documents.mapNotNull {
                    it.toObject(Announcement::class.java)
                }

                onResult(list)
            }
    }
}
