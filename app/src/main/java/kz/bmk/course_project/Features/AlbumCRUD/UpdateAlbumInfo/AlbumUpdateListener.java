package kz.bmk.course_project.Features.AlbumCRUD.UpdateAlbumInfo;

import kz.bmk.course_project.Features.AlbumCRUD.CreateAlbum.Album;

public interface AlbumUpdateListener {
    void onAlbumInfoUpdate(Album album, int position);
}
