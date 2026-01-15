package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateSongRequest;
import com.music.music_inventory_api.dto.request.UpdateSongRequest;
import com.music.music_inventory_api.dto.response.SongResponse;
import com.music.music_inventory_api.entity.Song;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SongMapper {

    @Mapping(source = "album.id", target = "albumId")
    SongResponse toResponse(Song song);

    @Mapping(source = "albumId", target = "album.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Song toEntity(CreateSongRequest request);

    @Mapping(source = "albumId", target = "album.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateSongRequest request, @MappingTarget Song song);

    List<SongResponse> toResponseList(List<Song> songs);
}
