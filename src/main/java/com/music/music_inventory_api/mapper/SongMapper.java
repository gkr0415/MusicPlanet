package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(source = "album.id", target = "albumId")
    SongResponse toResponse(Song song);

    @Mapping(source = "albumId", target = "album.id")
    Song toEntity(CreateSongRequest request);
}
