package com.nasa.nasastarsnews.ui.apod

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nasa.nasastarsnews.MainActivity
import com.nasa.nasastarsnews.R
import com.nasa.nasastarsnews.data.ApodData
import com.squareup.picasso.Picasso
import android.content.Intent
import android.net.Uri
import android.content.ActivityNotFoundException
import com.stfalcon.imageviewer.StfalconImageViewer
import android.content.Context
import android.graphics.Point
import android.view.*


class ApodFragment : Fragment(){

    private lateinit var apodViewModel: ApodViewModel
    private lateinit var apod:ApodData
    private lateinit var imageView:ImageView
    private val typeMediaImage = "image"
    private val typeMediaVideo = "video"
    private var screenHeight:Int = 0
    private var screenWidth :Int = 0
    private var imageViewer:StfalconImageViewer<ImageView>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_apod_layout, container, false)

        apodViewModel =
            ViewModelProviders.of(requireActivity()).get(ApodViewModel::class.java)

        apod = apodViewModel.getApod()

        imageView = root.findViewById(R.id.image_title_apod) // work with image

        val display = (requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y


        when(apod.typeMedia){

            typeMediaImage -> {
                val url = if(MainActivity.isHDImage) apod.hdUrl else apod.url
                val placeholder = creatorDrawable()

                placeholder?.let {
                    picassoLoad(placeholder, imageView, url!!)

                } ?: run {
                    picassoLoad(R.drawable.image_placeholder ,imageView, url!!)
                }


                imageView.setOnClickListener {
                    val list = arrayOf(imageView)

                        imageViewer = StfalconImageViewer.Builder<ImageView>(context, list) { view, image ->
                            Picasso.get()
                                .load(url)
                                .resize(screenWidth, screenHeight)
                                .onlyScaleDown()
                                .centerInside()
                                .into(view)
                        }.build()


                    imageViewer?.show()

                }


            }

            typeMediaVideo -> {

                apod.youtubeId?.let {
                    Picasso.get()
                        .load("https://img.youtube.com/vi/$it/0.jpg")
                        .placeholder(R.drawable.video_grey_placeholder_dr)
                        .into(imageView)

                    root.findViewById<ImageView>(R.id.video_icon_in_fragment).visibility = View.VISIBLE
                } ?: run {
                    Picasso.get()
                        .load(R.drawable.video_grey_placeholder_dr)
                        //  .placeholder(res)
                        //  .error(R.drawable.user_placeholder_error)
                        .into(imageView)
                }

                imageView.setOnClickListener {
                    startVideoIntent(apod.url!!)
                }
            }

        }


        // work with text

        val titleTextViewTranslate = root.findViewById<TextView>(R.id.title_text_apod_translate)
        val textTextViewTranslate = root.findViewById<TextView>(R.id.text_text_apod_translate)

        val textViewService = root.findViewById<TextView>(R.id.translate_sarvice_name)

        if(MainActivity.language == MainActivity.languageDefault){
            textViewService.visibility = View.GONE
            titleTextViewTranslate.text = apod.title
            textTextViewTranslate.text = apod.text
        } else {

            apod.textTranslate?.let {
                textTextViewTranslate.text = apod.textTranslate
                textViewService.visibility = View.VISIBLE
            } ?: run {
                textTextViewTranslate.text = apod.text
                textViewService.visibility = View.GONE
            }
            apod.titleTranslate?.let {
                titleTextViewTranslate.text = apod.titleTranslate
            } ?: run {
                titleTextViewTranslate.text = apod.title
            }
        }

        val linearLayout = root.findViewById<LinearLayout>(R.id.linear_layout_translate)
        if(MainActivity.twoText){

            linearLayout.visibility = View.VISIBLE
            val titleTextView = root.findViewById<TextView>(R.id.title_text_apod)
            val textTextView = root.findViewById<TextView>(R.id.text_text_apod)

            titleTextView.text = apod.title
            textTextView.text = apod.text

        } else {
            linearLayout.visibility = View.GONE
        }

        val textViewDate = root.findViewById<TextView>(R.id.date_in_apod_fragment)
        textViewDate.text = apod.date

        return root
    }




    private fun picassoLoad(res:Int, imageView: ImageView, url:String){
        Picasso.get()
            .load(url)
            .placeholder(res)
            .error(R.drawable.error_placeholder)
            .resize(screenWidth, screenHeight)
            .onlyScaleDown()
            .centerInside()
            .into(imageView)
    }



    private fun picassoLoad(drawable: Drawable, imageView: ImageView, url:String){
        Picasso.get()
            .load(url)
            .placeholder(drawable)
            .error(R.drawable.error_placeholder)
            .resize(screenWidth, screenHeight)
            .onlyScaleDown()
            .centerInside()
            .into(imageView)
    }

    private fun creatorDrawable(): Drawable? {
        apod.height?.let {
            apod.width?.let {
                val bitmap = Bitmap.createBitmap(apod.width!!, apod.height!!, Bitmap.Config.ARGB_8888)

                return BitmapDrawable(resources, bitmap)
            }
        }
        return null
    }


    private fun startVideoIntent(url: String){

            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            try {
                requireContext().startActivity(webIntent)
            } catch (ex: ActivityNotFoundException) {
              //  context!!.startActivity(webIntent)

            }
    }

    override fun onStop() {
        imageViewer?.dismiss()
        super.onStop()
    }
}

