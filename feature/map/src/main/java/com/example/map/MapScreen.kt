package com.example.map

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.map.model.MapUiState
import com.example.model.photo.PhotoInfo
import com.example.utils.location.LocationProvider
import com.google.android.gms.maps.CameraUpdateFactory
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
    var cameraInitialized by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val locationProvider = remember { LocationProvider(context) }
    val mapProperties = MapProperties(
        mapType = MapType.NORMAL
    )

    LaunchedEffect(uiState) {
        if (uiState is MapUiState.Success) {
            val photos = (uiState as MapUiState.Success).photoList
            viewModel.requestPhotoMarker(photos)


            if (!cameraInitialized) {
                val location = locationProvider.getCurrentLocation()
                location?.let {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 15f))
                }
                cameraInitialized = true
            }

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

    val selectedPhoto = (uiState as? MapUiState.Success)?.selectedPhoto

    LaunchedEffect(selectedPhoto) {
        selectedPhoto?.let {
            if (it.latitude != null && it.longitude != null) {
                Log.d("photomap", "*** selected.photo 위경도: ${it.latitude}, ${it.longitude}")
                cameraPositionState.animate(
                    CameraUpdateFactory.newLatLng(
                        LatLng(it.latitude!!, it.longitude!!)
                    )
                )
            }
        }
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
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false
            ),
            onMapClick = {
                viewModel.selectPhoto(null)
            }
        ) {
            if (uiState is MapUiState.Success) {
                val photos = (uiState as MapUiState.Success).photoList
                photos.forEach { photo ->
                    if (photo.latitude != null && photo.longitude != null) {
                        Marker(
                            state = MarkerState(position = LatLng(photo.latitude!!, photo.longitude!!)),
                            icon = photoMarkerIcons.find { it.containsKey(photo.id!!) }?.get(photo.id),
                            zIndex = 999f,
                            onClick = {
                                viewModel.selectPhoto(photo)
                                true
                            }
                        )
                    }

                }
            }


        }

        if (uiState is MapUiState.Success) {
            val selected = (uiState as MapUiState.Success).selectedPhoto
            if (selected != null) {
                PhotoBottomSheetScaffold(photo = selected)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoBottomSheetScaffold(
    photo: PhotoInfo
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 120.dp,
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetDragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 6.dp, bottom = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(4.dp)
                        .width(36.dp)
                        .background(
                            color = Color.LightGray,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .background(color = Color.White)
                    .padding(horizontal = 9.dp, vertical = 4.dp)
            ) {
                TitleAndTag(photo)

                val isExpanded = scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded

                AnimatedContent(
                    targetState = isExpanded,
                    label = "PhotoBottomSheetScaffold"
                ) { expanded ->
                    if (expanded) {
                        AddressAndW3W(photo)
                    }
                }
            }
        }
    ) {

    }
}

@Composable
fun TitleAndTag(
    photo: PhotoInfo
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = photo.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(photo.tags) { tag ->
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.LightGray.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun AddressAndW3W(
    photo: PhotoInfo
) {
    Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
        Text(
            text = "위치: ${photo.latitude}, ${photo.longitude}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "w3w: ${photo.w3w ?: "없음"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
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