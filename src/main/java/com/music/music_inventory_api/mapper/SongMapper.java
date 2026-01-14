package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Song;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {

    SongResponse toResponse(Song song);

    Song toEntity(CreateSongRequest request);
}
