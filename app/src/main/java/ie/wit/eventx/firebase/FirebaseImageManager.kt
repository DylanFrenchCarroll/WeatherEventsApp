package ie.wit.eventx.firebase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import ie.wit.eventx.ui.utils.customTransformation
import timber.log.Timber
import java.io.ByteArrayOutputStream
import com.squareup.picasso.Target
import ie.wit.eventx.models.EventModel
import ie.wit.eventx.ui.event.EventFragment
import ie.wit.eventx.ui.event.EventViewModel
import java.net.URI

object FirebaseImageManager {

    var storage = FirebaseStorage.getInstance().reference
    var imageUri = MutableLiveData<Uri>()
    var eventUploadImageUri = MutableLiveData<Uri>()
    var eventDownloadImageUri = MutableLiveData<Uri>()
    public val status = MutableLiveData<Boolean>()

    fun checkStorageForExistingProfilePic(userid: String) {
        Timber.i("##### FIREBASE - 1 #####")

        val imageRef = storage.child("photos").child("${userid}.jpg")
        val defaultImageRef = storage.child("homer.jpg")

        imageRef.metadata.addOnSuccessListener { //File Exists
            imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!
            }
            //File Doesn't Exist
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }

    fun uploadImageToFirebase(userid: String, bitmap: Bitmap, updating : Boolean) {
        // Get the data from an ImageView as bytes
        Timber.i("##### FIREBASE - 2 #####")

        val imageRef = storage.child("photos").child("${userid}.jpg")
        //val bitmap = (imageView as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        lateinit var uploadTask: UploadTask

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        imageRef.metadata.addOnSuccessListener { //File Exists
            if(updating) // Update existing Image
            {
                uploadTask = imageRef.putBytes(data)
                uploadTask.addOnSuccessListener { ut ->
                    ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        imageUri.value = task.result!!
                    }
                }
            }
        }.addOnFailureListener { //File Doesn't Exist
            uploadTask = imageRef.putBytes(data)
            uploadTask.addOnSuccessListener { ut ->
                ut.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                    imageUri.value = task.result!!
                }
            }
        }
    }

    fun updateUserImage(userid: String, imageUri : Uri?, imageView: ImageView, updating : Boolean) {
        Timber.i("##### FIREBASE - 3 #####")

        Picasso.get().load(imageUri)
            .resize(200, 200)
            .transform(customTransformation())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?,
                                            from: Picasso.LoadedFrom?
                ) {
                    Timber.i("DX onBitmapLoaded $bitmap")
                    uploadImageToFirebase(userid, bitmap!!,updating)
                    imageView.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: java.lang.Exception?,
                                            errorDrawable: Drawable?) {
                    Timber.i("DX onBitmapFailed $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }

    fun updateDefaultImage(userid: String, resource: Int, imageView: ImageView) {
        Timber.i("##### FIREBASE - 4 #####")

        Picasso.get().load(resource)
            .resize(200, 200)
            .transform(customTransformation())
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .centerCrop()
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?,
                                            from: Picasso.LoadedFrom?
                ) {
                    Timber.i("DX onBitmapLoaded $bitmap")
                    uploadImageToFirebase(userid, bitmap!!,false)
                    imageView.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: java.lang.Exception?,
                                            errorDrawable: Drawable?) {
                    Timber.i("DX onBitmapFailed $e")
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
    }

    fun uploadImageEvent (
        uuid: String,
        imageUri: Uri?,
        firebaseUser: MutableLiveData<FirebaseUser>,
        event: EventModel
    ){
       Picasso.get().load(imageUri)
           .memoryPolicy(MemoryPolicy.NO_CACHE)
           .into(object : Target {
               override fun onBitmapLoaded( bitmap: Bitmap?, from: Picasso.LoadedFrom? ) {
                   Timber.i("DX onBitmapLoaded $bitmap")
                   uploadEventImageToFirebase(uuid, bitmap!!, firebaseUser, event)
               }

               override fun onBitmapFailed(e: java.lang.Exception?,
                                           errorDrawable: Drawable?) {
                   Timber.i("DX onBitmapFailed $e")
               }

               override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
           })
   }

    fun uploadEventImageToFirebase(
        photoUUID: String,
        bitmap: Bitmap,
        firebaseUser: MutableLiveData<FirebaseUser>,
        event: EventModel) {

        status.value = false
        val imgRef = storage.child("event-photos").child(photoUUID)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        var uploadTask = imgRef.putBytes(data)


        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->
        }

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            imgRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val uri: Uri = task.result
                event.eventimg = uri.toString()
                FirebaseDBManager.create(firebaseUser,event)
                status.value=true
            } else {
                // Handle failures
                // ...
            }
        }
    }


}