package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.ArtistDetailResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface ArtistMapper {

    ArtistResponse toResponse(Artist artist);

    ArtistDetailResponse toDetailResponse(Artist artist);

    Artist toEntity(CreateArtistRequest request);

    void updateEntityFromRequest(UpdateArtistRequest request, @MappingTarget Artist artist);
}
