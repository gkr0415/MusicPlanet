package com.music.music_inventory_api.mapper;

import com.music.music_inventory_api.dto.request.CreateGenreRequest;
import com.music.music_inventory_api.dto.response.GenreResponse;
import com.music.music_inventory_api.entity.Genre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    GenreResponse toResponse(Genre genre);

    Genre toEntity(CreateGenreRequest request);
}
