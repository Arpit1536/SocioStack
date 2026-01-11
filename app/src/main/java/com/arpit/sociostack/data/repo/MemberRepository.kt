package com.arpit.sociostack.data.repo

import com.arpit.sociostack.data.model.Member
import com.google.firebase.firestore.FirebaseFirestore

class MemberRepository {

    private val db = FirebaseFirestore.getInstance()
    private val membersRef = db.collection("members")

    fun addMember(
        member: Member,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        membersRef.add(member)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener {
                onError(it.message ?: "Failed to add member")
            }
    }

    fun updateMember(
        memberId: String,
        updatedMember: Member,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        membersRef.document(memberId)
            .set(updatedMember)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun fetchMembers(onResult: (List<Member>) -> Unit) {
        membersRef.get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull { doc ->
                    val member = doc.toObject(Member::class.java)
                    member?.copy(id = doc.id)
                }
                onResult(list)
            }
            .addOnFailureListener {
                onResult(emptyList())
            }
    }

    fun deleteMember(
        memberId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        membersRef.document(memberId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }
}
