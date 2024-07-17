package com.luissdev.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDataDTO(
        @JsonAlias("title") String title,
        @JsonAlias("authors") List<AuthorDataDTO> author,
        @JsonAlias("languages") List<String> language,
        @JsonAlias("download_count") Double downloads
) {
}
