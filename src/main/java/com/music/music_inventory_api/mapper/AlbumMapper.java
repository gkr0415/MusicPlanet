package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateAlbumRequest;
import com.music.music_inventory_api.dto.request.UpdateAlbumRequest;
import com.music.music_inventory_api.dto.response.AlbumDetailResponse;
import com.music.music_inventory_api.dto.response.AlbumResponse;
import com.music.music_inventory_api.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    AlbumResponse toResponse(Album album);

    AlbumDetailResponse toDetailResponse(Album album);

    Album toEntity(CreateAlbumRequest request);

    void updateEntityFromRequest(UpdateAlbumRequest request, @MappingTarget Album album);
}
