package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.response.GenreResponse;
import com.music.music_inventory_api.entity.Genre;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper
{

    GenreResponse toResponse(Genre genre);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Genre toEntity(CreateGenreRequest request);

    Set<GenreResponse> toResponseSet(Set<Genre> genres);

    List<GenreResponse> toResponseList(List<Genre> genres);
}
