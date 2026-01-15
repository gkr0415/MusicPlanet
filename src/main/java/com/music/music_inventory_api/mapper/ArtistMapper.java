package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateArtistRequest;
import com.music.music_inventory_api.dto.request.UpdateArtistRequest;
import com.music.music_inventory_api.dto.response.ArtistDetailResponse;
import com.music.music_inventory_api.dto.response.ArtistResponse;
import com.music.music_inventory_api.entity.Artist;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses =
{AlbumMapper.class})
public interface ArtistMapper
{

    ArtistResponse toResponse(Artist artist);

    @Mapping(target = "albums", ignore = true)
    ArtistDetailResponse toDetailResponse(Artist artist);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Artist toEntity(CreateArtistRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(UpdateArtistRequest request, @MappingTarget Artist artist);

    List<ArtistResponse> toResponseList(List<Artist> artists);
}
