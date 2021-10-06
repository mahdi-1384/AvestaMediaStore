package ir.avesta.lastavestamediastore

import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Cursor?.mGetString(column: String): String? {
    return this?.getString(this.getColumnIndex(column))
}

const val THIS_COLUMN_IS_DEPRECATED = "This column is deprecated"

fun getAllProjectionByUri(uri: Uri): Array<String>? {

    val result = when (uri) {

        MediaStore.Files.getContentUri("external") -> MediaStore.Files.FileColumns::class.java
        MediaStore.Files.getContentUri("internal") -> MediaStore.Files.FileColumns::class.java


        MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI -> MediaStore.Images.Thumbnails::class.java
        MediaStore.Images.Thumbnails.INTERNAL_CONTENT_URI -> MediaStore.Images.Thumbnails::class.java

        MediaStore.Images.Media.EXTERNAL_CONTENT_URI -> MediaStore.Images.ImageColumns::class.java
        MediaStore.Images.Media.INTERNAL_CONTENT_URI -> MediaStore.Images.ImageColumns::class.java


        MediaStore.Video.Media.EXTERNAL_CONTENT_URI -> MediaStore.Video.VideoColumns::class.java
        MediaStore.Video.Media.INTERNAL_CONTENT_URI -> MediaStore.Video.VideoColumns::class.java

        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI -> MediaStore.Video.Thumbnails::class.java
        MediaStore.Video.Thumbnails.INTERNAL_CONTENT_URI -> MediaStore.Video.Thumbnails::class.java


        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI -> MediaStore.Audio.AudioColumns::class.java
        MediaStore.Audio.Media.INTERNAL_CONTENT_URI -> MediaStore.Audio.AudioColumns::class.java

        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI -> MediaStore.Audio.AlbumColumns::class.java
        MediaStore.Audio.Albums.INTERNAL_CONTENT_URI -> MediaStore.Audio.AlbumColumns::class.java

        MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI -> MediaStore.Audio.ArtistColumns::class.java
        MediaStore.Audio.Artists.INTERNAL_CONTENT_URI -> MediaStore.Audio.ArtistColumns::class.java

        MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI -> MediaStore.Audio.GenresColumns::class.java
        MediaStore.Audio.Genres.INTERNAL_CONTENT_URI -> MediaStore.Audio.GenresColumns::class.java

        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI -> MediaStore.Audio.PlaylistsColumns::class.java
        MediaStore.Audio.Playlists.INTERNAL_CONTENT_URI -> MediaStore.Audio.PlaylistsColumns::class.java

        //MediaStore.Downloads.INTERNAL_CONTENT_URI
        //MediaStore.Downloads.EXTERNAL_CONTENT_URI
        else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.DownloadColumns::class.java else null

    }

    return result?.fields?.map {
        it.name
    }?.toTypedArray()
}