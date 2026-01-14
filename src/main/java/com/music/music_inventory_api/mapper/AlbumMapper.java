package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {GenreMapper.class, SongMapper.class, ArtistMapper.class})
public interface AlbumMapper {

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "artist.name", target = "artistName")
    AlbumResponse toResponse(Album album);

    AlbumDetailResponse toDetailResponse(Album album);

    @Mapping(source = "artistId", target = "artist.id")
    Album toEntity(CreateAlbumRequest request);

    void updateEntityFromRequest(UpdateAlbumRequest request, @MappingTarget Album album);
}
