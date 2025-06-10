package com.example.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.map.model.MapUiState
import com.example.model.photo.PhotoInfo
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

@Composable
fun MapScreen(
    onEditClick: (PhotoInfo) -> Unit,
    padding: PaddingValues,
    viewModel: MapViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val photoMarkerIcons by viewModel.markerIcon.collectAsState()
    val cameraPositionState = rememberCameraPositionState()
    val mapProperties = MapProperties(
        mapType = MapType.NORMAL
    )
    val context = LocalContext.current

    LaunchedEffect(uiState) {
        if (uiState is MapUiState.Success) {
            val photos = (uiState as MapUiState.Success).photoList
            viewModel.requestPhotoMarker(photos)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.requestedPhotoMarker
            .onEach { photos ->
                val icons = mutableListOf<Map<Long, BitmapDescriptor>>()
                photos.map { photo ->
                    val icon = createMarkerBitmapDescriptor(
                        context = context,
                        photoUri = photo.photoUri
                    )
                    icons.add(mapOf(photo.id!! to icon))
                }
                viewModel.setMarkerIcons(icons)
            }
            .launchIn(this)
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                compassEnabled = false,
                mapToolbarEnabled = false
            ),
//            onMapClick = {
//                viewModel.updateSelectedState(BottomBarState.Default)
//                showAlarmCircle = false
//            }
        ) {
            if (uiState is MapUiState.Success) {
                val photos = (uiState as MapUiState.Success).photoList
                photos.forEach { photo ->
                    if (photo.latitude != null && photo.longitude != null) {
                        Marker(
                            state = MarkerState(position = LatLng(photo.latitude!!, photo.longitude!!)),
                            icon = photoMarkerIcons.find { it.containsKey(photo.id!!) }?.get(photo.id)
                        )
                    }

                }
            }


        }
    }

}

@SuppressLint("MissingInflatedId")
suspend fun createMarkerBitmapDescriptor(
    context: Context,
    photoUri: String
) : BitmapDescriptor = withContext(Dispatchers.IO) {
    val inflater = LayoutInflater.from(context)
    val view = inflater.inflate(R.layout.marker_photo, null)

    val imageView = view.findViewById<ImageView>(R.id.imageView)

    val drawable = try {
        val request = ImageRequest.Builder(context)
            .data(photoUri)
            .transformations(CircleCropTransformation())
            .listener(
                onSuccess = { _, _ ->
                    imageView.visibility = View.VISIBLE
                },
                onError = { _, result -> }
            )
            .build()

        ImageLoader(context).execute(request).drawable
    } catch (e: Exception) {
        null
    }

    imageView.setImageDrawable(drawable)

    view.measure(
        View.MeasureSpec.UNSPECIFIED,
        View.MeasureSpec.UNSPECIFIED,
    )
    view.layout(0, 0, view.measuredWidth, view.measuredHeight)

    val bitmap = Bitmap.createBitmap(
        view.measuredWidth,
        view.measuredHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    view.draw(canvas)

    BitmapDescriptorFactory.fromBitmap(bitmap)
}