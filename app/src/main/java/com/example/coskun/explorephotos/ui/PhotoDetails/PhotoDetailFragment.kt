package com.example.coskun.explorephotos.ui.PhotoDetails


import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.transition.TransitionInflater
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.View
import com.example.coskun.explorephotos.BaseFragment
import com.example.coskun.explorephotos.R
import com.example.coskun.explorephotos.entensions.toByteArray
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class PhotoDetailFragment() : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_photo_detail

    override  fun supportBackButton() = true

    override fun getToolbarTitle() = R.string.toolbar_title_photo_details

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
            exitTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactionName = arguments?.getString(ARG_TRANSACTION_NAME)!!
        val imageBitmap = arguments?.getParcelable<Bitmap>(ARG_BITMAP)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            img_detail.transitionName = transactionName
            startPostponedEnterTransition()
        }
        img_detail.setImageBitmap(imageBitmap)
        fab_share.setOnClickListener { share(imageBitmap) }
    }

    private fun share(bitmap: Bitmap){
        val imageFile = File(context!!.cacheDir, "images/image.png")
        imageFile.parentFile.mkdirs()
        imageFile.writeBytes(bitmap.toByteArray())
        val contentUri = FileProvider.getUriForFile(context!!, "com.example.coskun.explorephotos.fileprovider", imageFile)
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(contentUri, activity!!.contentResolver.getType(contentUri))
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivity(Intent.createChooser(intent, "Share image via"))

    }



    companion object {

        private const val ARG_TRANSACTION_NAME = "arg1"
        private const val ARG_BITMAP = "arg2"

        fun newInstance(transactionName: String, bitmap: Bitmap) : PhotoDetailFragment{
            val fragment = PhotoDetailFragment()
            val args = Bundle()
            args.putString(ARG_TRANSACTION_NAME, transactionName)
            args.putParcelable(ARG_BITMAP, bitmap)
            fragment.arguments = args
            return fragment
        }
    }



}
